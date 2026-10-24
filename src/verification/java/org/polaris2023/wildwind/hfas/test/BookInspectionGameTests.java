package org.polaris2023.wildwind.hfas.test;

import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.polaris2023.wildwind.core.inspection.BookInspectionHandler;
import org.polaris2023.wildwind.core.inspection.InspectionReport;
import org.polaris2023.wildwind.core.inspection.SulfurProfile;

public final class BookInspectionGameTests {
    private static void check(GameTestHelper h, boolean ok, String message) { h.assertTrue(ok, Component.literal(message)); }
    private static void field(GameTestHelper h, List<String> report, String row) { check(h, report.contains(row), "Missing report row: " + row + " in " + report); }

    static void blocks(GameTestHelper h) {
        var level = h.getLevel();
        var pos = h.absolutePos(new BlockPos(2, 2, 2));
        level.setBlockAndUpdate(pos, Blocks.OAK_PLANKS.defaultBlockState());
        var report = InspectionReport.block(level, pos);
        for (String row : List.of("方块ID:minecraft:oak_planks", "硬度:2", "爆炸抗性:3", "引燃几率:5", "烧毁几率:20",
                "熔岩可燃性:是", "乐器:贝斯", "烧炼时间:15秒（300刻）", "最大堆叠:64", "可再生:是", "物品ID:minecraft:oak_planks")) field(h, report, row);
        check(h, report.stream().anyMatch(row -> row.startsWith("合适挖掘工具:") && row.contains("斧")), "Planks use axes");
        level.setBlockAndUpdate(pos, Blocks.COBBLESTONE.defaultBlockState());
        report = InspectionReport.block(level, pos);
        field(h, report, "引燃几率:×");
        field(h, report, "烧炼时间:×");
        field(h, report, "烧炼加工时间:10秒（200刻）");
        level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        field(h, InspectionReport.block(level, pos), "物品ID:×");
        field(h, InspectionReport.block(level, pos), "最大堆叠:×");
        level.setBlockAndUpdate(pos, Blocks.DIAMOND_BLOCK.defaultBlockState());
        field(h, InspectionReport.block(level, pos), "可再生:否");
        level.setBlockAndUpdate(pos, Blocks.BARRIER.defaultBlockState());
        field(h, InspectionReport.block(level, pos), "可再生:×");
        level.setBlockAndUpdate(pos, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS.get().defaultBlockState());
        report = InspectionReport.block(level, pos);
        field(h, report, "方块ID:wild_wind_core:azalea_planks");
        field(h, report, "可再生:是");
        field(h, report, "烧炼时间:15秒（300刻）");
        for (var block : List.of(Blocks.OBSIDIAN, Blocks.CHEST, Blocks.BEDROCK)) {
            check(h, InspectionReport.piston(block.defaultBlockState(), level, pos).equals("不可推动，不可拉动"), "Special immovable block: " + block);
        }
        check(h, InspectionReport.piston(Blocks.STONE.defaultBlockState(), level, pos).equals("可推动，可拉动"), "Stone is movable");
        check(h, InspectionReport.piston(Blocks.TORCH.defaultBlockState(), level, pos).equals("被推动破坏，不可拉动"), "Torch breaks when pushed");
        check(h, InspectionReport.piston(Blocks.GLAZED_TERRACOTTA.white().defaultBlockState(), level, pos).equals("可推动，不可拉动"), "Glazed terracotta push-only");
        check(h, InspectionReport.piston(Blocks.PISTON.defaultBlockState().setValue(PistonBaseBlock.EXTENDED, true), level, pos).equals("不可推动，不可拉动"), "Extended piston immovable");
        h.succeed();
    }

    static void interactions(GameTestHelper h) {
        var player = h.makeMockServerPlayerInLevel();
        var pos = h.absolutePos(new BlockPos(2, 2, 2));
        h.getLevel().setBlockAndUpdate(pos, Blocks.CHEST.defaultBlockState());
        var book = new ItemStack(Items.WRITABLE_BOOK);
        var note = new Filterable<>("已有笔记，请保留", java.util.Optional.of("过滤后的笔记"));
        book.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(List.of(note)));
        player.setItemInHand(InteractionHand.MAIN_HAND, book);
        var hit = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false);
        var normal = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, InteractionHand.MAIN_HAND, pos, hit));
        check(h, !normal.isCanceled() && book.get(DataComponents.WRITABLE_BOOK_CONTENT).pages().size() == 1, "Normal use must remain unchanged");
        player.setShiftKeyDown(true);
        var event = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, InteractionHand.MAIN_HAND, pos, hit));
        check(h, event.isCanceled() && event.getCancellationResult() == InteractionResult.SUCCESS, "Inspector consumes interaction before opening chest/book");
        var signed = player.getItemInHand(InteractionHand.MAIN_HAND);
        check(h, signed.is(Items.WRITTEN_BOOK) && !signed.has(DataComponents.WRITABLE_BOOK_CONTENT), "Held book becomes signed book without editable pages");
        var content = signed.get(DataComponents.WRITTEN_BOOK_CONTENT);
        var pages = content.pages();
        check(h, pages.size() > 1 && pages.getFirst().equals(note.map(Component::literal)), "Signing preserves raw and filtered notes");
        check(h, content.author().equals(player.getGameProfile().name()) && content.generation() == 0 && content.resolved(), "Player is author of original literal snapshot");
        String chestName = Blocks.CHEST.getName().getString() + "(minecraft:chest)";
        check(h, content.title().raw().equals(chestName) && signed.getHoverName().getString().equals(chestName), "Signed title includes block name and ID");
        check(h, pages.stream().allMatch(p -> p.raw().getString().length() <= 1024 && p.raw().getString().split("\n", -1).length <= 12), "Pages fit book layout limits");
        var repeated = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, InteractionHand.MAIN_HAND, pos, hit));
        check(h, !repeated.isCanceled() && player.getItemInHand(InteractionHand.MAIN_HAND) == signed, "Signed book is never inspected again");
        var full = new WritableBookContent(IntStream.range(0, 100).mapToObj(i -> Filterable.passThrough("note " + i)).toList());
        book.set(DataComponents.WRITABLE_BOOK_CONTENT, full);
        player.setItemInHand(InteractionHand.MAIN_HAND, book);
        NeoForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, InteractionHand.MAIN_HAND, pos, hit));
        check(h, player.getItemInHand(InteractionHand.MAIN_HAND) == book && book.get(DataComponents.WRITABLE_BOOK_CONTENT).equals(full), "Full held book is unchanged");
        var almostFull = new WritableBookContent(full.pages().subList(0, 99));
        book.set(DataComponents.WRITABLE_BOOK_CONTENT, almostFull);
        check(h, BookInspectionHandler.sign(book, List.of("one", "two"), Component.literal("test"), "author").isEmpty()
                && book.get(DataComponents.WRITABLE_BOOK_CONTENT).equals(almostFull), "Insufficient space never partially appends a report");
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        var offhand = new ItemStack(Items.WRITABLE_BOOK);
        player.setItemInHand(InteractionHand.OFF_HAND, offhand);
        var cow = h.spawn(EntityTypes.COW, 3, 2, 3);
        var entityEvent = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(player, InteractionHand.OFF_HAND, cow, Vec3.ZERO));
        var signedEntity = player.getItemInHand(InteractionHand.OFF_HAND);
        check(h, entityEvent.isCanceled() && signedEntity.is(Items.WRITTEN_BOOK)
                && !signedEntity.get(DataComponents.WRITTEN_BOOK_CONTENT).pages().isEmpty()
                && signedEntity.getHoverName().getString().equals(cow.getType().getDescription().getString() + "(minecraft:cow)")
                && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Offhand entity interaction replaces only the offhand book");
        var report = InspectionReport.entity(h.getLevel(), cow);
        field(h, report, "实体ID:minecraft:cow"); field(h, report, "方块ID:×"); field(h, report, "硬度:×"); field(h, report, "最大生命值:10");
        field(h, report, "物品ID:×"); field(h, report, "可再生:是");
        var observer = h.makeMockServerPlayer(GameType.SPECTATOR);
        observer.setShiftKeyDown(true);
        observer.setItemInHand(InteractionHand.OFF_HAND, offhand);
        var spectator = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(observer, InteractionHand.OFF_HAND, cow, Vec3.ZERO));
        check(h, !spectator.isCanceled(), "Spectators never write");
        var dropped = new net.minecraft.world.entity.item.ItemEntity(h.getLevel(), 0, 0, 0, new ItemStack(Items.COBBLESTONE, 12));
        var itemReport = InspectionReport.entity(h.getLevel(), dropped);
        field(h, itemReport, "实体ID:minecraft:item");
        field(h, itemReport, "物品ID:minecraft:cobblestone");
        field(h, itemReport, "最大堆叠:64");
        field(h, itemReport, "生命值:×");
        check(h, BookInspectionHandler.sign(signedEntity, List.of("overwrite"), Component.literal("test"), "author").isEmpty(), "Signed books cannot be changed");
        var longName = Component.literal("杜鹃木悬挂式告示牌(wild_wind_core:azalea_hanging_sign)");
        var longBook = BookInspectionHandler.sign(offhand, List.of("report"), longName, "author");
        check(h, longBook.getHoverName().equals(longName), "Full name and long ID are preserved for display");
        var longContent = longBook.get(DataComponents.WRITTEN_BOOK_CONTENT);
        check(h, longContent.title().raw().length() <= 32
                && net.minecraft.world.item.component.WrittenBookContent.CODEC.encodeStart(
                    h.getLevel().registryAccess().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE), longContent).isSuccess(),
                "Long titles serialize within vanilla limits");
        var unicodeBook = BookInspectionHandler.sign(offhand, List.of("report"), Component.literal("x".repeat(31) + "😀(minecraft:stone)"), "author");
        check(h, unicodeBook.get(DataComponents.WRITTEN_BOOK_CONTENT).title().raw().equals("x".repeat(31)), "Title truncation preserves surrogate pairs");
        h.succeed();
    }

    static void sulfur(GameTestHelper h) {
        var level = h.getLevel();
        var examples = List.of(Items.OAK_PLANKS, Items.MUD, Items.IRON_BLOCK, Items.MOSS_BLOCK, Items.WOOL.white(),
                Items.PACKED_ICE, Items.RED_MUSHROOM_BLOCK, Items.STONE, Items.HONEYCOMB_BLOCK, Items.SOUL_SAND, Items.TNT, Items.MAGMA_BLOCK);
        var names = List.of("弹性（橡胶球）", "普通（足球）", "缓慢平移（药球）", "快速平移（高尔夫球）", "轻盈（沙滩球）",
                "快速滑行（冰球）", "缓慢滑行（冰壶）", "缓慢弹性", "黏性", "高阻力", "爆炸", "高温");
        var cubes = new java.util.ArrayList<net.minecraft.world.entity.monster.cubemob.SulfurCube>();
        for (int i = 0; i < examples.size(); i++) {
            var stack = examples.get(i).getDefaultInstance();
            var profile = SulfurProfile.of(level, stack);
            check(h, profile.name().equals(names.get(i)), "Vanilla archetype membership for " + stack + ": " + profile.name());
            var cube = h.spawn(EntityTypes.SULFUR_CUBE, 2, 2, 2);
            cube.setNoAi(true);
            cube.setItemSlot(EquipmentSlot.BODY, stack);
            cubes.add(cube);
        }
        check(h, SulfurProfile.of(level, Items.STICK.getDefaultInstance()).name().equals("默认"), "Unmatched items use default archetype");
        h.runAfterDelay(3, () -> {
            for (var cube : cubes) {
                var profile = SulfurProfile.of(level, cube.getItemBySlot(EquipmentSlot.BODY));
                field(h, InspectionReport.entity(level, cube), "硫方怪原型:" + profile.name());
                for (var attribute : List.of(Attributes.KNOCKBACK_RESISTANCE, Attributes.BOUNCINESS, Attributes.FRICTION_MODIFIER, Attributes.AIR_DRAG_MODIFIER)) {
                    check(h, Math.abs(profile.attribute(attribute) - cube.getAttributeValue(attribute)) < 0.0001, "Inspector physics agrees with actual equipped sulfur cube: " + profile.name());
                }
            }
            h.succeed();
        });
    }
}
