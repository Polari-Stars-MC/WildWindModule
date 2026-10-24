package org.polaris2023.wildwind.wilderness.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.wilderness.WildernessMod;
import org.polaris2023.wildwind.wilderness.worldgen.ModTreeGrowers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 方块注册类
 * 使用NeoForge 26.1的DeferredRegister方式注册方块
 * 注意：方块和物品分开注册，避免延迟注册bug
 *
 * @author baka4n
 * @since 2026/04/15
 */
//TODO 考虑迁移至registry目录下，或将registry目录下的注册条目分散到各目录下
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WildernessMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WildernessMod.MOD_ID);
    public static final BlockSetType BLAZE_BLOCK_SET = BlockSetType.register(new BlockSetType("blaze"));

    public static final WoodType BLAZE_WOOD_TYPE = WoodType.register(new WoodType("blaze", BLAZE_BLOCK_SET));

    public static final BlockSetType SOUL_BLOCK_SET = BlockSetType.register(new BlockSetType("soul"));

    public static final WoodType SOUL_WOOD_TYPE = WoodType.register(new WoodType("soul", SOUL_BLOCK_SET));

    public static final DeferredBlock<RotatedPillarBlock> BLAZE_LOG = BLOCKS.registerBlock(
            "blaze_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> BLAZE_WOOD = BLOCKS.registerBlock(
            "blaze_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_BLAZE_LOG = BLOCKS.registerBlock(
            "stripped_blaze_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_BLAZE_WOOD = BLOCKS.registerBlock(
            "stripped_blaze_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<Block> BLAZE_PLANKS = BLOCKS.registerBlock(
            "blaze_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<StairBlock> BLAZE_STAIRS = BLOCKS.registerBlock(
            "blaze_stairs",
            p -> new StairBlock(BLAZE_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<SlabBlock> BLAZE_SLAB = BLOCKS.registerBlock(
            "blaze_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceBlock> BLAZE_FENCE = BLOCKS.registerBlock(
            "blaze_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceGateBlock> BLAZE_FENCE_GATE = BLOCKS.registerBlock(
            "blaze_fence_gate",
            p -> new FenceGateBlock(BLAZE_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<DoorBlock> BLAZE_DOOR = BLOCKS.registerBlock(
            "blaze_door",
            p -> new DoorBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<TrapDoorBlock> BLAZE_TRAPDOOR = BLOCKS.registerBlock(
            "blaze_trapdoor",
            p -> new TrapDoorBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<PressurePlateBlock> BLAZE_PRESSURE_PLATE = BLOCKS.registerBlock(
            "blaze_pressure_plate",
            p -> new PressurePlateBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ButtonBlock> BLAZE_BUTTON = BLOCKS.registerBlock(
            "blaze_button",
            p -> new ButtonBlock(BLAZE_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<LeavesBlock> BLAZE_LEAVES = BLOCKS.registerBlock(
            "blaze_leaves",
            p -> new UntintedParticleLeavesBlock(0.01F,
                    net.minecraft.core.particles.ColorParticleOption.create(net.minecraft.core.particles.ParticleTypes.TINTED_LEAVES, 0xFFFF8000), p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    public static final DeferredBlock<SaplingBlock> BLAZE_SAPLING = BLOCKS.registerBlock(
            "blaze_sapling",
            p -> new SaplingBlock(ModTreeGrowers.BLAZE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    public static final DeferredBlock<RotatedPillarBlock> SOUL_LOG = BLOCKS.registerBlock(
            "soul_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> SOUL_WOOD = BLOCKS.registerBlock(
            "soul_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SOUL_LOG = BLOCKS.registerBlock(
            "stripped_soul_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SOUL_WOOD = BLOCKS.registerBlock(
            "stripped_soul_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<Block> SOUL_PLANKS = BLOCKS.registerBlock(
            "soul_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<StairBlock> SOUL_STAIRS = BLOCKS.registerBlock(
            "soul_stairs",
            p -> new StairBlock(SOUL_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<SlabBlock> SOUL_SLAB = BLOCKS.registerBlock(
            "soul_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceBlock> SOUL_FENCE = BLOCKS.registerBlock(
            "soul_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceGateBlock> SOUL_FENCE_GATE = BLOCKS.registerBlock(
            "soul_fence_gate",
            p -> new FenceGateBlock(SOUL_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<DoorBlock> SOUL_DOOR = BLOCKS.registerBlock(
            "soul_door",
            p -> new DoorBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<TrapDoorBlock> SOUL_TRAPDOOR = BLOCKS.registerBlock(
            "soul_trapdoor",
            p -> new TrapDoorBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<PressurePlateBlock> SOUL_PRESSURE_PLATE = BLOCKS.registerBlock(
            "soul_pressure_plate",
            p -> new PressurePlateBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ButtonBlock> SOUL_BUTTON = BLOCKS.registerBlock(
            "soul_button",
            p -> new ButtonBlock(SOUL_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<LeavesBlock> SOUL_LEAVES = BLOCKS.registerBlock(
            "soul_leaves",
            p -> new UntintedParticleLeavesBlock(0.01F,
                    net.minecraft.core.particles.ColorParticleOption.create(net.minecraft.core.particles.ParticleTypes.TINTED_LEAVES, 0xFF00FFFF), p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    public static final DeferredBlock<SaplingBlock> SOUL_SAPLING = BLOCKS.registerBlock(
            "soul_sapling",
            p -> new SaplingBlock(ModTreeGrowers.SOUL, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    public static final DeferredBlock<Block> SCORCHED_GRASS_BLOCK = BLOCKS.registerBlock(
            "scorched_grass_block",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.6F)
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    public static final DeferredBlock<Block> SCORCHED_DIRT = BLOCKS.registerBlock(
            "scorched_dirt",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL))
    );

    public static final DeferredBlock<Block> SCORCHED_GRASS = BLOCKS.registerBlock(
            "scorched_grass",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY))
    );

    public static final DeferredBlock<Block> SCORCHED_TWIG = BLOCKS.registerBlock(
            "scorched_twig",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY))
    );

    private static void blockItems() {
        ITEMS.registerSimpleBlockItem(BLAZE_LOG);
        ITEMS.registerSimpleBlockItem(BLAZE_WOOD);
        ITEMS.registerSimpleBlockItem(STRIPPED_BLAZE_LOG);
        ITEMS.registerSimpleBlockItem(STRIPPED_BLAZE_WOOD);
        ITEMS.registerSimpleBlockItem(BLAZE_PLANKS);
        ITEMS.registerSimpleBlockItem(BLAZE_STAIRS);
        ITEMS.registerSimpleBlockItem(BLAZE_SLAB);
        ITEMS.registerSimpleBlockItem(BLAZE_FENCE);
        ITEMS.registerSimpleBlockItem(BLAZE_FENCE_GATE);
        ITEMS.registerSimpleBlockItem(BLAZE_DOOR);
        ITEMS.registerSimpleBlockItem(BLAZE_TRAPDOOR);
        ITEMS.registerSimpleBlockItem(BLAZE_PRESSURE_PLATE);
        ITEMS.registerSimpleBlockItem(BLAZE_BUTTON);
        ITEMS.registerSimpleBlockItem(BLAZE_LEAVES);
        ITEMS.registerSimpleBlockItem(BLAZE_SAPLING);
        ITEMS.registerSimpleBlockItem(SOUL_LOG);
        ITEMS.registerSimpleBlockItem(SOUL_WOOD);
        ITEMS.registerSimpleBlockItem(STRIPPED_SOUL_LOG);
        ITEMS.registerSimpleBlockItem(STRIPPED_SOUL_WOOD);
        ITEMS.registerSimpleBlockItem(SOUL_PLANKS);
        ITEMS.registerSimpleBlockItem(SOUL_STAIRS);
        ITEMS.registerSimpleBlockItem(SOUL_SLAB);
        ITEMS.registerSimpleBlockItem(SOUL_FENCE);
        ITEMS.registerSimpleBlockItem(SOUL_FENCE_GATE);
        ITEMS.registerSimpleBlockItem(SOUL_DOOR);
        ITEMS.registerSimpleBlockItem(SOUL_TRAPDOOR);
        ITEMS.registerSimpleBlockItem(SOUL_PRESSURE_PLATE);
        ITEMS.registerSimpleBlockItem(SOUL_BUTTON);
        ITEMS.registerSimpleBlockItem(SOUL_LEAVES);
        ITEMS.registerSimpleBlockItem(SOUL_SAPLING);
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS_BLOCK);
        ITEMS.registerSimpleBlockItem(SCORCHED_DIRT);
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS);
        ITEMS.registerSimpleBlockItem(SCORCHED_TWIG);
    }
    public static void register(IEventBus bus) { BLOCKS.register(bus); blockItems(); ITEMS.register(bus); }
}
