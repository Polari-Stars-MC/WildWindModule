package org.polaris2023.wildwind.structures.test;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.structures.worldgen.ConfiguredStructure;
import org.polaris2023.wildwind.structures.worldgen.ConfiguredTemplatePiece;
import org.polaris2023.wildwind.structures.worldgen.GeneratedStructureIndex;

public final class StructureGameTests {
    private StructureGameTests() {}

    public static void register(IEventBus bus) {
        DeferredRegister<Consumer<GameTestHelper>> functions = DeferredRegister.create(Registries.TEST_FUNCTION, "wild_wind_tests");
        functions.register("generation", () -> StructureGameTests::generation);
        functions.register(bus);
    }

    private static void generation(GameTestHelper helper) {
        helper.assertTrue(net.neoforged.fml.ModList.get().isLoaded("wild_wind_core")
                        && !net.neoforged.fml.ModList.get().isLoaded("ww_structures"),
                Component.literal("Structures and items must load from the core mod"));
        for (var item : org.polaris2023.wildwind.core.registry.ModItems.MATERIALS) {
            helper.assertTrue(net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(item.getId())
                            && item.get().getClass() == net.minecraft.world.item.Item.class
                            && !item.toStack().has(net.minecraft.core.component.DataComponents.FOOD),
                    Component.literal("Plain material must be available alongside structures: " + item.getId()));
        }
        var level = helper.getLevel();
        var id = Identifier.parse("ww_structures:smoke");
        var holder = level.registryAccess().lookupOrThrow(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, id));
        helper.assertTrue(holder.value() instanceof ConfiguredStructure, Component.literal("Config must become a registered structure"));
        for (String rejected : new String[]{"blocked", "disabled"}) {
            var rejectedHolder = level.registryAccess().lookupOrThrow(Registries.STRUCTURE).getOrThrow(
                    ResourceKey.create(Registries.STRUCTURE, Identifier.parse("ww_structures:" + rejected)));
            helper.assertTrue(level.getChunkSource().getGenerator().findNearestMapStructure(level, HolderSet.direct(rejectedHolder),
                    new BlockPos(2048, 0, 2048), 2, false) == null, Component.literal(rejected + " must not generate"));
        }
        // Far from the test arena; generate through the actual ChunkGenerator, not /place.
        var result = level.getChunkSource().getGenerator().findNearestMapStructure(level, HolderSet.direct(holder),
                new BlockPos(2048, 0, 2048), 4, false);
        helper.assertTrue(result != null, Component.literal("Native locate must discover the configured structure"));
        var pos = result.getFirst();
        var chunk = level.getChunk(pos);
        var start = chunk.getStartForStructure(holder.value());
        helper.assertTrue(start != null && start.isValid(), Component.literal("Generated chunk must contain a valid start"));
        var piece = (ConfiguredTemplatePiece) start.getPieces().getFirst();
        var box = piece.getBoundingBox();
        helper.assertTrue(box.getXSpan() == 19 && box.getZSpan() == 23, Component.literal("Rotation must swap template footprint"));
        BlockPos floor = new BlockPos(box.minX(), box.minY(), box.minZ());
        // Load every intersecting chunk so that the cross-chunk template has been placed completely.
        for (int x = box.minX() >> 4; x <= box.maxX() >> 4; x++) {
            for (int z = box.minZ() >> 4; z <= box.maxZ() >> 4; z++) level.getChunk(x, z);
        }
        helper.assertTrue(level.getBlockState(floor).is(Blocks.MOSSY_COBBLESTONE), Component.literal("Template foundation must generate"));
        helper.assertTrue(level.getBlockState(floor.above(1)).is(Blocks.STONE_BRICKS), Component.literal("Template pillar must generate"));
        for (int x : new int[]{box.minX(), box.maxX()}) {
            for (int z : new int[]{box.minZ(), box.maxZ()}) {
                helper.assertTrue(level.getBlockState(new BlockPos(x, box.minY(), z)).is(Blocks.MOSSY_COBBLESTONE),
                        Component.literal("All corners must generate across chunk boundaries"));
                helper.assertTrue(level.getBlockState(new BlockPos(x, box.minY() + 1, z)).is(Blocks.STONE_BRICKS),
                        Component.literal("Rotated pillars must generate across chunk boundaries"));
            }
        }
        var serialization = StructurePieceSerializationContext.fromLevel(level);
        CompoundTag saved = piece.createTag(serialization);
        var restored = new ConfiguredTemplatePiece(serialization, saved);
        helper.assertTrue(restored.templatePosition().equals(piece.templatePosition()) && restored.getRotation() == piece.getRotation(),
                Component.literal("Piece origin and rotation must survive chunk NBT round trip"));
        helper.runAfterDelay(2, () -> {
            var index = level.getDataStorage().computeIfAbsent(GeneratedStructureIndex.TYPE);
            helper.assertTrue(index.nearest(id, pos).isPresent(), Component.literal("Full chunk must enter the generated structure index"));
            var source = level.getServer().createCommandSourceStack().withLevel(level);
            var dispatcher = level.getServer().getCommands().getDispatcher();
            try {
                helper.assertTrue(dispatcher.execute("wwstructure locate smoke", source.withPosition(Vec3.atCenterOf(pos))) == 1,
                        Component.literal("Generated locate command must succeed"));
                helper.assertTrue(dispatcher.execute("wwstructure search smoke 2", source.withPosition(Vec3.atCenterOf(pos))) == 1,
                        Component.literal("Native search command must succeed"));
                helper.assertTrue(dispatcher.execute("wwstructure locate missing", source) == 0,
                        Component.literal("Unknown structure must report no result"));
            } catch (com.mojang.brigadier.exceptions.CommandSyntaxException exception) {
                throw new IllegalStateException(exception);
            }
            helper.succeed();
        });
    }
}
