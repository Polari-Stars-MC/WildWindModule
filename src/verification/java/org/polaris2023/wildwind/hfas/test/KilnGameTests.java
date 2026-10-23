package org.polaris2023.wildwind.hfas.test;

import java.util.List;
import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.LockCode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.polaris2023.wildwind.core.worldgen.MasonKilnProcessor;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.polaris2023.wildwind.core.block.entity.KilnBlockEntity;
import org.polaris2023.wildwind.core.recipe.KilnRecipes;

final class KilnGameTests {
    private KilnGameTests() {}

    private static void check(GameTestHelper h, boolean ok, String message) {
        h.assertTrue(ok, Component.literal(message));
    }

    static void cooking(GameTestHelper h) {
        var pos = h.absolutePos(new net.minecraft.core.BlockPos(2, 1, 2));
        h.setBlock(2, 1, 2, org.polaris2023.wildwind.core.block.ModBlocks.KILN.get());
        BlockEntity raw = h.getLevel().getBlockEntity(pos);
        check(h, raw instanceof KilnBlockEntity, "Kiln must create its block entity");
        var kiln = (KilnBlockEntity) raw;
        var state = kiln.getBlockState();
        check(h, state.getDestroySpeed(h.getLevel(), pos) == 3.5F && org.polaris2023.wildwind.core.block.ModBlocks.KILN.get().getExplosionResistance() == 3.5F,
                "Kiln hardness and blast resistance must both be 3.5");
        check(h, state.getPistonPushReaction() == net.minecraft.world.level.material.PushReaction.BLOCK
                && state.isRedstoneConductor(h.getLevel(), pos) && state.isSuffocating(h.getLevel(), pos),
                "Kiln must be a solid conductor that pistons cannot move");
        var recipeInput = net.minecraft.world.item.crafting.CraftingInput.of(3, 3, List.of(
                ItemStack.EMPTY, new ItemStack(Items.BRICK), ItemStack.EMPTY,
                new ItemStack(Items.BRICK), new ItemStack(Items.FURNACE), new ItemStack(Items.BRICK),
                new ItemStack(Items.PACKED_MUD), new ItemStack(Items.PACKED_MUD), new ItemStack(Items.PACKED_MUD)));
        check(h, h.getLevel().recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING, recipeInput, h.getLevel())
                .orElseThrow().value().assemble(recipeInput).is(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get().asItem()), "Requested kiln crafting pattern must work");
        kiln.setItem(0, new ItemStack(Items.CACTUS, 2));
        kiln.setItem(1, new ItemStack(Items.COAL));
        check(h, KilnRecipes.find(h.getLevel(), kiln.getItem(0)).isPresent(), "Cactus must be kiln-smeltable");
        check(h, KilnRecipes.find(h.getLevel(), new ItemStack(Items.IRON_ORE)).isEmpty(), "Ore must be excluded");
        for (var denied : List.of(Items.RAW_IRON, Items.IRON_PICKAXE, Items.GOLDEN_CHESTPLATE, Items.ANCIENT_DEBRIS)) {
            check(h, KilnRecipes.find(h.getLevel(), new ItemStack(denied)).isEmpty(), "Metal/mineral must be excluded: " + denied);
            check(h, !kiln.canPlaceItem(0, new ItemStack(denied)), "Automated input must reject excluded materials");
        }
        for (var allowed : List.of(Items.SAND, Items.CLAY_BALL, Items.COBBLESTONE, Items.OAK_LOG, Items.WET_SPONGE, Items.POTATO)) {
            check(h, KilnRecipes.find(h.getLevel(), new ItemStack(allowed)).isPresent(), "Non-mineral smelting recipe must work: " + allowed);
        }
        tick(h, kiln, 99);
        check(h, kiln.getItem(2).isEmpty(), "Nothing may finish before tick 100");
        var ticker = org.polaris2023.wildwind.core.block.ModBlocks.KILN.get().getTicker(h.getLevel(), state,
                org.polaris2023.wildwind.core.block.entity.ModBlockEntities.KILN.get());
        check(h, ticker != null, "Kiln must have a registered server ticker");
        ticker.tick(h.getLevel(), pos, h.getLevel().getBlockState(pos), kiln);
        check(h, kiln.getItem(0).getCount() == 1, "Kiln must consume exactly one input after 100 ticks");
        check(h, kiln.getItem(2).is(Items.DYE.green()) && kiln.getItem(2).getCount() == 1,
                "Kiln must produce one cactus green dye");
        check(h, kiln.getItem(1).isEmpty(), "One coal must be consumed when the kiln starts");
        check(h, h.getLevel().getBlockState(pos).getValue(AbstractFurnaceBlock.LIT),
                "Kiln must be lit while its fuel remains");
        check(h, h.getLevel().getBlockState(pos).getLightEmission() == 13, "Burning kiln must emit light 13");
        var saved = kiln.saveWithFullMetadata(h.getLevel().registryAccess());
        var loaded = (KilnBlockEntity) BlockEntity.loadStatic(pos, kiln.getBlockState(), saved, h.getLevel().registryAccess());
        check(h, loaded != null && loaded.getItem(2).is(Items.DYE.green()), "Contents must survive reload");
        loaded.setLevel(h.getLevel());
        tick(h, loaded, 100);
        check(h, loaded.getItem(2).getCount() == 2 && loaded.getItem(0).isEmpty(), "Fuel/progress must continue after reload");
        kiln.setItem(0, new ItemStack(Items.CACTUS));
        kiln.setItem(2, new ItemStack(Items.DYE.green(), 64));
        tick(h, kiln, 100);
        check(h, kiln.getItem(0).getCount() == 1 && kiln.getItem(2).getCount() == 64, "Full output must prevent consumption");
        kiln.setItem(2, new ItemStack(Items.STONE));
        tick(h, kiln, 100);
        check(h, kiln.getItem(0).getCount() == 1 && kiln.getItem(2).is(Items.STONE), "Different output must prevent cooking");
        kiln.setItem(0, new ItemStack(Items.IRON_ORE));
        kiln.setItem(2, ItemStack.EMPTY);
        tick(h, kiln, 100);
        check(h, kiln.getItem(2).isEmpty() && kiln.getItem(0).is(Items.IRON_ORE), "Forced excluded input must not cook");
        kiln.setItem(0, new ItemStack(Items.WET_SPONGE));
        kiln.setItem(1, new ItemStack(Items.BUCKET));
        tick(h, kiln, 100);
        check(h, kiln.getItem(2).is(Items.SPONGE) && kiln.getItem(1).is(Items.WATER_BUCKET), "Wet sponge must fill the fuel-slot bucket");
        var longFuel = kiln.saveWithFullMetadata(h.getLevel().registryAccess());
        longFuel.putInt("lit_time_remaining", 50000);
        longFuel.putInt("lit_total_time", 50000);
        var longBurn = (KilnBlockEntity) BlockEntity.loadStatic(pos, kiln.getBlockState(), longFuel, h.getLevel().registryAccess());
        longBurn.setLevel(h.getLevel());
        tick(h, longBurn, 1);
        check(h, longBurn.saveCustomOnly(h.getLevel().registryAccess()).getIntOr("lit_time_remaining", 0) == 49999,
                "Long modded fuel timers must not be truncated by network short scaling");
        h.succeed();
    }

    private static void tick(GameTestHelper h, KilnBlockEntity kiln, int ticks) {
        for (int i = 0; i < ticks; i++) KilnBlockEntity.tick(h.getLevel(), kiln.getBlockPos(), h.getLevel().getBlockState(kiln.getBlockPos()), kiln);
    }

    static void inventory(GameTestHelper h) {
        var level = h.getLevel();
        var pos = h.absolutePos(new BlockPos(3, 2, 3));
        h.setBlock(3, 2, 3, org.polaris2023.wildwind.core.block.ModBlocks.KILN.get());
        var kiln = (KilnBlockEntity) level.getBlockEntity(pos);
        check(h, Arrays.equals(kiln.getSlotsForFace(Direction.UP), new int[]{0})
                && Arrays.equals(kiln.getSlotsForFace(Direction.DOWN), new int[]{2, 1})
                && Arrays.equals(kiln.getSlotsForFace(Direction.EAST), new int[]{1}), "Sided slots must match the specification");
        h.setBlock(3, 3, 3, Blocks.HOPPER);
        var top = (HopperBlockEntity) level.getBlockEntity(pos.above());
        top.setItem(0, new ItemStack(Items.CACTUS));
        HopperBlockEntity.pushItemsTick(level, top.getBlockPos(), top.getBlockState(), top);
        check(h, kiln.getItem(0).is(Items.CACTUS), "Top hopper must insert ingredients");
        h.setBlock(4, 2, 3, Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.WEST));
        var side = (HopperBlockEntity) level.getBlockEntity(pos.east());
        side.setItem(0, new ItemStack(Items.LAVA_BUCKET));
        HopperBlockEntity.pushItemsTick(level, side.getBlockPos(), side.getBlockState(), side);
        check(h, kiln.getItem(1).is(Items.LAVA_BUCKET), "Side hopper must insert fuel");
        tick(h, kiln, 100);
        check(h, kiln.getItem(1).is(Items.BUCKET), "Lava fuel must return an empty bucket");
        h.setBlock(3, 1, 3, Blocks.HOPPER);
        var bottom = (HopperBlockEntity) level.getBlockEntity(pos.below());
        check(h, HopperBlockEntity.suckInItems(level, bottom), "Bottom hopper must pull output");
        check(h, bottom.getItem(0).is(Items.DYE.green()) && kiln.getItem(1).is(Items.BUCKET), "Output must be pulled before the bucket");
        check(h, HopperBlockEntity.suckInItems(level, bottom) && kiln.getItem(1).isEmpty(), "Bottom hopper must next pull the bucket");
        check(h, kiln.saveCustomOnly(level.registryAccess()).getCompound("RecipesUsed").orElseThrow().size() > 0,
                "Hopper extraction must retain stored experience");
        kiln.clearContent();
        check(h, AbstractContainerMenu.getRedstoneSignalFromBlockEntity(kiln) == 0, "Empty kiln comparator must be zero");
        kiln.setItem(0, new ItemStack(Items.CACTUS, 64));
        kiln.setItem(1, new ItemStack(Items.COAL, 64));
        kiln.setItem(2, new ItemStack(Items.DYE.green(), 64));
        check(h, kiln.getBlockState().getAnalogOutputSignal(level, pos, Direction.NORTH) == 15, "Comparator must count all three slots");
        kiln.clearContent();
        var player = connectedPlayer(h);
        var named = new ItemStack(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get());
        named.set(DataComponents.CUSTOM_NAME, Component.literal("Test kiln"));
        named.set(DataComponents.LOCK, new LockCode(ItemPredicate.Builder.item()
                .of(level.registryAccess().lookupOrThrow(Registries.ITEM), Items.BRICK).build()));
        kiln.applyComponentsFromItemStack(named);
        check(h, kiln.getName().getString().equals("Test kiln") && !kiln.canOpen(player), "Custom name and lock must apply");
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BRICK));
        check(h, kiln.canOpen(player), "Matching key item must unlock kiln");
        var menu = kiln.createMenu(1, player.getInventory(), player);
        check(h, menu != null && !menu.getSlot(0).mayPlace(new ItemStack(Items.IRON_ORE)), "GUI must reject excluded input");
        check(h, !menu.getSlot(2).mayPlace(new ItemStack(Items.STONE)), "Output slot must reject insertion");
        kiln.setItem(0, new ItemStack(Items.CACTUS));
        tick(h, kiln, 100);
        var outputSlot = menu.getSlot(2);
        var result = outputSlot.remove(1);
        outputSlot.onTake(player, result);
        check(h, kiln.saveCustomOnly(level.registryAccess()).getCompound("RecipesUsed").orElseThrow().isEmpty(),
                "Manual extraction must release and clear stored experience");
        check(h, !level.getEntitiesOfClass(ExperienceOrb.class, new AABB(player.blockPosition()).inflate(2)).isEmpty(), "Manual extraction must spawn stored experience");
        kiln.setItem(0, new ItemStack(Items.CACTUS));
        tick(h, kiln, 100);
        var state = kiln.getBlockState();
        check(h, Block.getDrops(state, level, pos, kiln, player, new ItemStack(Items.WOODEN_PICKAXE)).stream()
                .anyMatch(stack -> stack.is(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get().asItem())), "Wooden pickaxe must allow kiln loot");
        check(h, !player.hasCorrectToolForDrops(state), "A held brick must not qualify for kiln drops");
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WOODEN_PICKAXE));
        check(h, player.hasCorrectToolForDrops(state), "Wooden pickaxe must qualify for kiln drops");
        level.destroyBlock(pos, true, player);
        var dropped = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2));
        check(h, dropped.stream().anyMatch(e -> e.getItem().is(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get().asItem()))
                && dropped.stream().anyMatch(e -> e.getItem().is(Items.DYE.green())), "Breaking must drop the kiln and its inventory");
        check(h, !level.getEntitiesOfClass(ExperienceOrb.class, new AABB(pos).inflate(2)).isEmpty(), "Breaking must release stored experience");
        h.succeed();
    }

    private static ServerPlayer connectedPlayer(GameTestHelper h) {
        var player = (ServerPlayer) h.makeMockServerPlayer(GameType.SURVIVAL);
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection);
        h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player,
                net.minecraft.server.network.CommonListenerCookie.createInitial(player.getGameProfile(), false));
        player.setPos(h.absoluteVec(new Vec3(6.5, 2, 6.5)));
        return player;
    }

    static void villages(GameTestHelper h) {
        var level = h.getLevel();
        var templates = level.getStructureManager();
        int index = 0;
        for (var house : MasonKilnProcessor.HOUSES.entrySet()) {
            for (var rotation : Rotation.values()) {
                var origin = h.absolutePos(new BlockPos(30 + index++ * 20, 3, 30));
                var template = templates.getOrCreate(house.getKey());
                var settings = new StructurePlaceSettings().setRotation(rotation);
                var box = template.getBoundingBox(settings, origin);
                var pool = StructurePoolElement.legacy(house.getKey().toString()).apply(StructureTemplatePool.Projection.RIGID);
                check(h, pool.place(templates, level, level.structureManager(), level.getChunkSource().getGenerator(),
                        origin, origin, rotation, box, level.getRandom(), LiquidSettings.IGNORE_WATERLOGGING, false),
                        "Mason house must place through the real pool path: " + house.getKey());
                var kilnPos = net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
                        .calculateRelativePosition(settings, house.getValue().target()).offset(origin);
                check(h, level.getBlockState(kilnPos).is(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get()) && level.getBlockEntity(kilnPos) instanceof KilnBlockEntity,
                        "Every mason house rotation must contain a functional kiln: " + house.getKey() + " " + rotation);
                check(h, level.getBlockState(kilnPos).getValue(AbstractFurnaceBlock.FACING) == rotation.rotate(house.getValue().facing()),
                        "Kiln facing must rotate with its house");
                for (var cutter : template.filterBlocks(origin, settings, Blocks.STONECUTTER)) {
                    check(h, level.getBlockState(cutter.pos()).is(Blocks.STONECUTTER), "Mason workstation must be preserved");
                }
            }
        }
        h.succeed();
    }
}
