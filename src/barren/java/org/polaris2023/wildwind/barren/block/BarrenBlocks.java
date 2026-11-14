package org.polaris2023.wildwind.barren.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.worldgen.BarrenTreeGrowers;

/**
 * 瘠土更新的方块注册：砂岩 / 沙 / 流沙 / 焦土 / 下界木质套件。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class BarrenBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WildWindBarren.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WildWindBarren.MOD_ID);

    // ==================== 木质类型 ====================

    public static final BlockSetType CINDER_BLOCK_SET = BlockSetType.register(new BlockSetType("cinder"));
    // 告示牌编辑界面用 WoodType.name() 拼贴图路径，必须带命名空间才能落到 ww_barren 资源下
    public static final WoodType CINDER_WOOD_TYPE = WoodType.register(new WoodType(WildWindBarren.MOD_ID + ":cinder", CINDER_BLOCK_SET));

    public static final BlockSetType EMBER_BLOCK_SET = BlockSetType.register(new BlockSetType("ember"));
    public static final WoodType EMBER_WOOD_TYPE = WoodType.register(new WoodType(WildWindBarren.MOD_ID + ":ember", EMBER_BLOCK_SET));

    // ==================== 焚烬木套件 (cinder_wood) ====================

    public static final DeferredBlock<RotatedPillarBlock> CINDER_LOG = BLOCKS.registerBlock(
            "cinder_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> CINDER_WOOD = BLOCKS.registerBlock(
            "cinder_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CINDER_LOG = BLOCKS.registerBlock(
            "stripped_cinder_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CINDER_WOOD = BLOCKS.registerBlock(
            "stripped_cinder_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<Block> CINDER_PLANKS = BLOCKS.registerBlock(
            "cinder_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<StairBlock> CINDER_STAIRS = BLOCKS.registerBlock(
            "cinder_stairs",
            p -> new StairBlock(CINDER_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<SlabBlock> CINDER_SLAB = BLOCKS.registerBlock(
            "cinder_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceBlock> CINDER_FENCE = BLOCKS.registerBlock(
            "cinder_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceGateBlock> CINDER_FENCE_GATE = BLOCKS.registerBlock(
            "cinder_fence_gate",
            p -> new FenceGateBlock(CINDER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<DoorBlock> CINDER_DOOR = BLOCKS.registerBlock(
            "cinder_door",
            p -> new DoorBlock(CINDER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<TrapDoorBlock> CINDER_TRAPDOOR = BLOCKS.registerBlock(
            "cinder_trapdoor",
            p -> new TrapDoorBlock(CINDER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<PressurePlateBlock> CINDER_PRESSURE_PLATE = BLOCKS.registerBlock(
            "cinder_pressure_plate",
            p -> new PressurePlateBlock(CINDER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ButtonBlock> CINDER_BUTTON = BLOCKS.registerBlock(
            "cinder_button",
            p -> new ButtonBlock(CINDER_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ShelfBlock> CINDER_SHELF = BLOCKS.registerBlock(
            "cinder_shelf",
            p -> new ShelfBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sound(SoundType.SHELF)
                    .ignitedByLava()
                    .strength(2.0F, 3.0F))
    );

    public static final DeferredBlock<StandingSignBlock> CINDER_SIGN = BLOCKS.registerBlock(
            "cinder_sign",
            p -> new StandingSignBlock(CINDER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<WallSignBlock> CINDER_WALL_SIGN = BLOCKS.registerBlock(
            "cinder_wall_sign",
            p -> new WallSignBlock(CINDER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<CeilingHangingSignBlock> CINDER_HANGING_SIGN = BLOCKS.registerBlock(
            "cinder_hanging_sign",
            p -> new CeilingHangingSignBlock(CINDER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<WallHangingSignBlock> CINDER_WALL_HANGING_SIGN = BLOCKS.registerBlock(
            "cinder_wall_hanging_sign",
            p -> new WallHangingSignBlock(CINDER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<LeavesBlock> CINDER_LEAVES = BLOCKS.registerBlock(
            "cinder_leaves",
            p -> new TintedParticleLeavesBlock(0.01F, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    public static final DeferredBlock<SaplingBlock> CINDER_SAPLING = BLOCKS.registerBlock(
            "cinder_sapling",
            p -> new SaplingBlock(BarrenTreeGrowers.CINDER, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_CINDER_SAPLING = BLOCKS.registerBlock(
            "potted_cinder_sapling",
            p -> new FlowerPotBlock(
                    () -> (FlowerPotBlock) Blocks.FLOWER_POT,
                    CINDER_SAPLING,
                    p.instabreak()
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY))
    );

    // ==================== 灵焰木套件 (ember_wood) ====================

    public static final DeferredBlock<RotatedPillarBlock> EMBER_LOG = BLOCKS.registerBlock(
            "ember_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> EMBER_WOOD = BLOCKS.registerBlock(
            "ember_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EMBER_LOG = BLOCKS.registerBlock(
            "stripped_ember_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EMBER_WOOD = BLOCKS.registerBlock(
            "stripped_ember_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<Block> EMBER_PLANKS = BLOCKS.registerBlock(
            "ember_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<StairBlock> EMBER_STAIRS = BLOCKS.registerBlock(
            "ember_stairs",
            p -> new StairBlock(EMBER_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<SlabBlock> EMBER_SLAB = BLOCKS.registerBlock(
            "ember_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceBlock> EMBER_FENCE = BLOCKS.registerBlock(
            "ember_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceGateBlock> EMBER_FENCE_GATE = BLOCKS.registerBlock(
            "ember_fence_gate",
            p -> new FenceGateBlock(EMBER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<DoorBlock> EMBER_DOOR = BLOCKS.registerBlock(
            "ember_door",
            p -> new DoorBlock(EMBER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<TrapDoorBlock> EMBER_TRAPDOOR = BLOCKS.registerBlock(
            "ember_trapdoor",
            p -> new TrapDoorBlock(EMBER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<PressurePlateBlock> EMBER_PRESSURE_PLATE = BLOCKS.registerBlock(
            "ember_pressure_plate",
            p -> new PressurePlateBlock(EMBER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ButtonBlock> EMBER_BUTTON = BLOCKS.registerBlock(
            "ember_button",
            p -> new ButtonBlock(EMBER_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ShelfBlock> EMBER_SHELF = BLOCKS.registerBlock(
            "ember_shelf",
            p -> new ShelfBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .instrument(NoteBlockInstrument.BASS)
                    .sound(SoundType.SHELF)
                    .ignitedByLava()
                    .strength(2.0F, 3.0F))
    );

    public static final DeferredBlock<StandingSignBlock> EMBER_SIGN = BLOCKS.registerBlock(
            "ember_sign",
            p -> new StandingSignBlock(EMBER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<WallSignBlock> EMBER_WALL_SIGN = BLOCKS.registerBlock(
            "ember_wall_sign",
            p -> new WallSignBlock(EMBER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<CeilingHangingSignBlock> EMBER_HANGING_SIGN = BLOCKS.registerBlock(
            "ember_hanging_sign",
            p -> new CeilingHangingSignBlock(EMBER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<WallHangingSignBlock> EMBER_WALL_HANGING_SIGN = BLOCKS.registerBlock(
            "ember_wall_hanging_sign",
            p -> new WallHangingSignBlock(EMBER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava())
    );

    public static final DeferredBlock<LeavesBlock> EMBER_LEAVES = BLOCKS.registerBlock(
            "ember_leaves",
            p -> new TintedParticleLeavesBlock(0.01F, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    public static final DeferredBlock<SaplingBlock> EMBER_SAPLING = BLOCKS.registerBlock(
            "ember_sapling",
            p -> new SaplingBlock(BarrenTreeGrowers.EMBER, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_EMBER_SAPLING = BLOCKS.registerBlock(
            "potted_ember_sapling",
            p -> new FlowerPotBlock(
                    () -> (FlowerPotBlock) Blocks.FLOWER_POT,
                    EMBER_SAPLING,
                    p.instabreak()
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY))
    );

    // ==================== 焦土 / 瘠地装饰 ====================

    /** 焦灰草方块 */
    public static final DeferredBlock<Block> SCORCHED_GRASS_BLOCK = BLOCKS.registerBlock(
            "scorched_grass_block",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.6F)
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    /** 焦土 */
    public static final DeferredBlock<Block> SCORCHED_DIRT = BLOCKS.registerBlock(
            "scorched_dirt",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL))
    );

    /** 焦灰草丛 */
    public static final DeferredBlock<Block> SCORCHED_GRASS = BLOCKS.registerBlock(
            "scorched_grass",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY))
    );

    /** 焦灰枝条 */
    public static final DeferredBlock<Block> SCORCHED_TWIG = BLOCKS.registerBlock(
            "scorched_twig",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY))
    );

    /** 仙人球 */
    public static final DeferredBlock<Block> TINY_CACTUS = BLOCKS.registerBlock(
            "tiny_cactus",
            p -> new Block(p
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY))
    );

    /** 流沙 */
    public static final DeferredBlock<Block> QUICKSAND = BLOCKS.registerBlock(
            "quicksand",
            p -> new Block(p
                    .mapColor(MapColor.SAND)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    /** 红色流沙 */
    public static final DeferredBlock<Block> RED_QUICKSAND = BLOCKS.registerBlock(
            "red_quicksand",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    /** 可疑的红沙 */
    public static final DeferredBlock<Block> SUSPICIOUS_RED = BLOCKS.registerBlock(
            "suspicious_red_sand",
            p -> new BrushableBlock(
                    Blocks.RED_SAND,
                    SoundEvents.BRUSH_SAND,
                    SoundEvents.BRUSH_SAND_COMPLETED, p
                    .mapColor(MapColor.SAND)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    // ==================== 砂岩 / 沙 ====================

    public static final DeferredBlock<Block> CHISELED_QUARTZ_SANDSTONE = BLOCKS.registerBlock(
            "chiseled_quartz_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> CHISELED_SOUL_SANDSTONE = BLOCKS.registerBlock(
            "chiseled_soul_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> CUT_QUARTZ_SANDSTONE = BLOCKS.registerBlock(
            "cut_quartz_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> CUT_SOUL_SANDSTONE = BLOCKS.registerBlock(
            "cut_soul_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> NITRIFIED_QUARTZ_SANDSTONE = BLOCKS.registerBlock(
            "nitrified_quartz_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> NITRIFIED_RED_SANDSTONE = BLOCKS.registerBlock(
            "nitrified_red_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<NitrifiedSandstoneBlock> NITRIFIED_SANDSTONE = BLOCKS.registerBlock(
            "nitrified_sandstone",
            p -> new NitrifiedSandstoneBlock(p
                    .mapColor(MapColor.SAND)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> NITRIFIED_SOUL_SANDSTONE = BLOCKS.registerBlock(
            "nitrified_soul_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> QUARTZ_SANDSTONE = BLOCKS.registerBlock(
            "quartz_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> SOUL_SANDSTONE = BLOCKS.registerBlock(
            "soul_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> SMOOTH_QUARTZ_SANDSTONE = BLOCKS.registerBlock(
            "smooth_quartz_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> SMOOTH_SOUL_SANDSTONE = BLOCKS.registerBlock(
            "smooth_soul_sandstone",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<SandBlock> QUARTZ_SAND = BLOCKS.registerBlock(
            "quartz_sand",
            p -> new SandBlock(new ColorRGBA(0xFFDBD3C0), p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    public static final DeferredBlock<Block> QUARTZ_QUICKSAND = BLOCKS.registerBlock(
            "quartz_quicksand",
            p -> new Block(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    public static final DeferredBlock<Block> SOUL_QUICKSAND = BLOCKS.registerBlock(
            "soul_quicksand",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    public static final DeferredBlock<Block> SUSPICIOUS_QUARTZ_SAND = BLOCKS.registerBlock(
            "suspicious_quartz_sand",
            p -> new BrushableBlock(
                    Blocks.SAND,
                    SoundEvents.BRUSH_SAND,
                    SoundEvents.BRUSH_SAND_COMPLETED, p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    public static final DeferredBlock<Block> SUSPICIOUS_SOUL_SAND = BLOCKS.registerBlock(
            "suspicious_soul_sand",
            p -> new BrushableBlock(
                    Blocks.SOUL_SAND,
                    SoundEvents.BRUSH_SAND,
                    SoundEvents.BRUSH_SAND_COMPLETED, p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.5F)
                    .sound(SoundType.SOUL_SAND))
    );

    // ==================== 灰烬 / 植被（草稿） ====================

    /** 浮萤 */
    public static final DeferredBlock<Block> FROGBIT = BLOCKS.registerBlock(
            "frogbit",
            p -> new Block(p
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY))
    );

    /** 灰烬块 */
    public static final DeferredBlock<Block> ASH_BLOCK = BLOCKS.registerBlock(
            "ash_block",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.5F)
                    .sound(SoundType.SAND))
    );

    // ==================== 石英砂岩衍生物 ====================

    public static final DeferredBlock<StairBlock> QUARTZ_SANDSTONE_STAIRS = BLOCKS.registerBlock(
            "quartz_sandstone_stairs",
            p -> new StairBlock(QUARTZ_SANDSTONE.get().defaultBlockState(), p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<SlabBlock> QUARTZ_SANDSTONE_SLAB = BLOCKS.registerBlock(
            "quartz_sandstone_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<WallBlock> QUARTZ_SANDSTONE_WALL = BLOCKS.registerBlock(
            "quartz_sandstone_wall",
            p -> new WallBlock(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<StairBlock> SMOOTH_QUARTZ_SANDSTONE_STAIRS = BLOCKS.registerBlock(
            "smooth_quartz_sandstone_stairs",
            p -> new StairBlock(SMOOTH_QUARTZ_SANDSTONE.get().defaultBlockState(), p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<SlabBlock> SMOOTH_QUARTZ_SANDSTONE_SLAB = BLOCKS.registerBlock(
            "smooth_quartz_sandstone_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<SlabBlock> CUT_QUARTZ_SANDSTONE_SLAB = BLOCKS.registerBlock(
            "cut_quartz_sandstone_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.QUARTZ)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    // ==================== 灵魂砂岩衍生物 / 硝石 ====================

    public static final DeferredBlock<StairBlock> SOUL_SANDSTONE_STAIRS = BLOCKS.registerBlock(
            "soul_sandstone_stairs",
            p -> new StairBlock(SOUL_SANDSTONE.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<SlabBlock> SOUL_SANDSTONE_SLAB = BLOCKS.registerBlock(
            "soul_sandstone_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<WallBlock> SOUL_SANDSTONE_WALL = BLOCKS.registerBlock(
            "soul_sandstone_wall",
            p -> new WallBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<StairBlock> SMOOTH_SOUL_SANDSTONE_STAIRS = BLOCKS.registerBlock(
            "smooth_soul_sandstone_stairs",
            p -> new StairBlock(SMOOTH_SOUL_SANDSTONE.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<SlabBlock> SMOOTH_SOUL_SANDSTONE_SLAB = BLOCKS.registerBlock(
            "smooth_soul_sandstone_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<SlabBlock> CUT_SOUL_SANDSTONE_SLAB = BLOCKS.registerBlock(
            "cut_soul_sandstone_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    /** 硝石块 */
    public static final DeferredBlock<Block> NITER_BLOCK = BLOCKS.registerBlock(
            "niter_block",
            p -> new Block(p
                    .mapColor(MapColor.SAND)
                    .strength(0.8F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops())
    );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        blockItems();
        ITEMS.register(bus);
    }

    /**
     * 告示牌 / 悬挂告示牌 / 展示架复用原版方块实体类型。
     */
    public static void addBlockEntityBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityTypes.SIGN, CINDER_SIGN.get(), CINDER_WALL_SIGN.get(), EMBER_SIGN.get(), EMBER_WALL_SIGN.get());
        event.modify(BlockEntityTypes.HANGING_SIGN, CINDER_HANGING_SIGN.get(), CINDER_WALL_HANGING_SIGN.get(), EMBER_HANGING_SIGN.get(), EMBER_WALL_HANGING_SIGN.get());
        event.modify(BlockEntityTypes.SHELF, CINDER_SHELF.get(), EMBER_SHELF.get());
    }

    public static void blockItems() {
        // 焚烬木 / 灵焰木套件
        ITEMS.registerSimpleBlockItem(CINDER_LOG);
        ITEMS.registerSimpleBlockItem(CINDER_WOOD);
        ITEMS.registerSimpleBlockItem(STRIPPED_CINDER_LOG);
        ITEMS.registerSimpleBlockItem(STRIPPED_CINDER_WOOD);
        ITEMS.registerSimpleBlockItem(CINDER_PLANKS);
        ITEMS.registerSimpleBlockItem(CINDER_STAIRS);
        ITEMS.registerSimpleBlockItem(CINDER_SLAB);
        ITEMS.registerSimpleBlockItem(CINDER_FENCE);
        ITEMS.registerSimpleBlockItem(CINDER_FENCE_GATE);
        ITEMS.registerSimpleBlockItem(CINDER_DOOR);
        ITEMS.registerSimpleBlockItem(CINDER_TRAPDOOR);
        ITEMS.registerSimpleBlockItem(CINDER_PRESSURE_PLATE);
        ITEMS.registerSimpleBlockItem(CINDER_BUTTON);
        ITEMS.registerSimpleBlockItem(CINDER_SHELF);
        ITEMS.registerItem("cinder_sign",
                p -> new SignItem(CINDER_SIGN.get(), CINDER_WALL_SIGN.get(), p),
                () -> new Item.Properties().stacksTo(16).useBlockDescriptionPrefix());
        ITEMS.registerItem("cinder_hanging_sign",
                p -> new HangingSignItem(CINDER_HANGING_SIGN.get(), CINDER_WALL_HANGING_SIGN.get(), p),
                () -> new Item.Properties().stacksTo(16).useBlockDescriptionPrefix());
        ITEMS.registerSimpleBlockItem(CINDER_LEAVES);
        ITEMS.registerSimpleBlockItem(CINDER_SAPLING);

        ITEMS.registerSimpleBlockItem(EMBER_LOG);
        ITEMS.registerSimpleBlockItem(EMBER_WOOD);
        ITEMS.registerSimpleBlockItem(STRIPPED_EMBER_LOG);
        ITEMS.registerSimpleBlockItem(STRIPPED_EMBER_WOOD);
        ITEMS.registerSimpleBlockItem(EMBER_PLANKS);
        ITEMS.registerSimpleBlockItem(EMBER_STAIRS);
        ITEMS.registerSimpleBlockItem(EMBER_SLAB);
        ITEMS.registerSimpleBlockItem(EMBER_FENCE);
        ITEMS.registerSimpleBlockItem(EMBER_FENCE_GATE);
        ITEMS.registerSimpleBlockItem(EMBER_DOOR);
        ITEMS.registerSimpleBlockItem(EMBER_TRAPDOOR);
        ITEMS.registerSimpleBlockItem(EMBER_PRESSURE_PLATE);
        ITEMS.registerSimpleBlockItem(EMBER_BUTTON);
        ITEMS.registerSimpleBlockItem(EMBER_SHELF);
        ITEMS.registerItem("ember_sign",
                p -> new SignItem(EMBER_SIGN.get(), EMBER_WALL_SIGN.get(), p),
                () -> new Item.Properties().stacksTo(16).useBlockDescriptionPrefix());
        ITEMS.registerItem("ember_hanging_sign",
                p -> new HangingSignItem(EMBER_HANGING_SIGN.get(), EMBER_WALL_HANGING_SIGN.get(), p),
                () -> new Item.Properties().stacksTo(16).useBlockDescriptionPrefix());
        ITEMS.registerSimpleBlockItem(EMBER_LEAVES);
        ITEMS.registerSimpleBlockItem(EMBER_SAPLING);

        // 焦土 / 瘠地
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS_BLOCK);
        ITEMS.registerSimpleBlockItem(SCORCHED_DIRT);
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS);
        ITEMS.registerSimpleBlockItem(SCORCHED_TWIG);
        ITEMS.registerSimpleBlockItem(TINY_CACTUS);
        ITEMS.registerSimpleBlockItem(QUICKSAND);
        ITEMS.registerSimpleBlockItem(RED_QUICKSAND);
        ITEMS.registerSimpleBlockItem(SUSPICIOUS_RED);

        // 砂岩 / 沙
        ITEMS.registerSimpleBlockItem(CHISELED_QUARTZ_SANDSTONE);
        ITEMS.registerSimpleBlockItem(CHISELED_SOUL_SANDSTONE);
        ITEMS.registerSimpleBlockItem(CUT_QUARTZ_SANDSTONE);
        ITEMS.registerSimpleBlockItem(CUT_SOUL_SANDSTONE);
        ITEMS.registerSimpleBlockItem(NITRIFIED_QUARTZ_SANDSTONE);
        ITEMS.registerSimpleBlockItem(NITRIFIED_RED_SANDSTONE);
        ITEMS.registerSimpleBlockItem(NITRIFIED_SANDSTONE);
        ITEMS.registerSimpleBlockItem(NITRIFIED_SOUL_SANDSTONE);
        ITEMS.registerSimpleBlockItem(QUARTZ_SANDSTONE);
        ITEMS.registerSimpleBlockItem(SOUL_SANDSTONE);
        ITEMS.registerSimpleBlockItem(SMOOTH_QUARTZ_SANDSTONE);
        ITEMS.registerSimpleBlockItem(SMOOTH_SOUL_SANDSTONE);
        ITEMS.registerSimpleBlockItem(QUARTZ_SAND);
        ITEMS.registerSimpleBlockItem(QUARTZ_QUICKSAND);
        ITEMS.registerSimpleBlockItem(SOUL_QUICKSAND);
        ITEMS.registerSimpleBlockItem(SUSPICIOUS_QUARTZ_SAND);
        ITEMS.registerSimpleBlockItem(SUSPICIOUS_SOUL_SAND);

        // 灰烬 / 植被（草稿）
        ITEMS.registerSimpleBlockItem(FROGBIT);
        ITEMS.registerSimpleBlockItem(ASH_BLOCK);

        // 灵魂砂岩衍生物 / 硝石
        ITEMS.registerSimpleBlockItem(SOUL_SANDSTONE_STAIRS);
        ITEMS.registerSimpleBlockItem(SOUL_SANDSTONE_SLAB);
        ITEMS.registerSimpleBlockItem(SOUL_SANDSTONE_WALL);
        ITEMS.registerSimpleBlockItem(SMOOTH_SOUL_SANDSTONE_STAIRS);
        ITEMS.registerSimpleBlockItem(SMOOTH_SOUL_SANDSTONE_SLAB);
        ITEMS.registerSimpleBlockItem(CUT_SOUL_SANDSTONE_SLAB);
        ITEMS.registerSimpleBlockItem(NITER_BLOCK);

        // 石英砂岩衍生物
        ITEMS.registerSimpleBlockItem(QUARTZ_SANDSTONE_STAIRS);
        ITEMS.registerSimpleBlockItem(QUARTZ_SANDSTONE_SLAB);
        ITEMS.registerSimpleBlockItem(QUARTZ_SANDSTONE_WALL);
        ITEMS.registerSimpleBlockItem(SMOOTH_QUARTZ_SANDSTONE_STAIRS);
        ITEMS.registerSimpleBlockItem(SMOOTH_QUARTZ_SANDSTONE_SLAB);
        ITEMS.registerSimpleBlockItem(CUT_QUARTZ_SANDSTONE_SLAB);
    }

    private BarrenBlocks() {
    }
}
