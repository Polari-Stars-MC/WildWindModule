package org.polaris2023.wildwind.hfas.block;

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
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.worldgen.ModTreeGrowers;

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
public class ModBlocks {
    // 方块注册器
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HFASMod.MOD_ID);
    // 物品注册器
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HFASMod.MOD_ID);

    // ==================== 方块类型定义 ====================

    // 焚烬木方块类型
    public static final BlockSetType BLAZE_BLOCK_SET = BlockSetType.register(new BlockSetType("blaze"));
    // 焚烬木木材类型
    public static final WoodType BLAZE_WOOD_TYPE = WoodType.register(new WoodType("blaze", BLAZE_BLOCK_SET));

    // 灵焰木方块类型
    public static final BlockSetType SOUL_BLOCK_SET = BlockSetType.register(new BlockSetType("soul"));
    // 灵焰木木材类型
    public static final WoodType SOUL_WOOD_TYPE = WoodType.register(new WoodType("soul", SOUL_BLOCK_SET));

    // 杜鹃木方块类型
    public static final BlockSetType AZALEA_BLOCK_SET = BlockSetType.register(new BlockSetType("azalea"));
    // 杜鹃木木材类型
    public static final WoodType AZALEA_WOOD_TYPE = WoodType.register(new WoodType("azalea", AZALEA_BLOCK_SET));

    // ==================== 焚烬木套件 (blaze_wood) ====================

    // 焚烬木原木
    public static final DeferredBlock<RotatedPillarBlock> BLAZE_LOG = BLOCKS.registerBlock(
            "blaze_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木
    public static final DeferredBlock<RotatedPillarBlock> BLAZE_WOOD = BLOCKS.registerBlock(
            "blaze_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮焚烬木原木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_BLAZE_LOG = BLOCKS.registerBlock(
            "stripped_blaze_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮焚烬木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_BLAZE_WOOD = BLOCKS.registerBlock(
            "stripped_blaze_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木木板
    public static final DeferredBlock<Block> BLAZE_PLANKS = BLOCKS.registerBlock(
            "blaze_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木楼梯
    public static final DeferredBlock<StairBlock> BLAZE_STAIRS = BLOCKS.registerBlock(
            "blaze_stairs",
            p -> new StairBlock(BLAZE_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木台阶
    public static final DeferredBlock<SlabBlock> BLAZE_SLAB = BLOCKS.registerBlock(
            "blaze_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木栅栏
    public static final DeferredBlock<FenceBlock> BLAZE_FENCE = BLOCKS.registerBlock(
            "blaze_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木栅栏门
    public static final DeferredBlock<FenceGateBlock> BLAZE_FENCE_GATE = BLOCKS.registerBlock(
            "blaze_fence_gate",
            p -> new FenceGateBlock(BLAZE_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木门
    public static final DeferredBlock<DoorBlock> BLAZE_DOOR = BLOCKS.registerBlock(
            "blaze_door",
            p -> new DoorBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木活板门
    public static final DeferredBlock<TrapDoorBlock> BLAZE_TRAPDOOR = BLOCKS.registerBlock(
            "blaze_trapdoor",
            p -> new TrapDoorBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木压力板
    public static final DeferredBlock<PressurePlateBlock> BLAZE_PRESSURE_PLATE = BLOCKS.registerBlock(
            "blaze_pressure_plate",
            p -> new PressurePlateBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木按钮
    public static final DeferredBlock<ButtonBlock> BLAZE_BUTTON = BLOCKS.registerBlock(
            "blaze_button",
            p -> new ButtonBlock(BLAZE_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木树叶
    public static final DeferredBlock<LeavesBlock> BLAZE_LEAVES = BLOCKS.registerBlock(
            "blaze_leaves",
            p -> new TintedParticleLeavesBlock(0.01F,p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    // 焚烬木树苗
    public static final DeferredBlock<SaplingBlock> BLAZE_SAPLING = BLOCKS.registerBlock(
            "blaze_sapling",
            p -> new SaplingBlock(ModTreeGrowers.BLAZE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    // ==================== 灵焰木套件 (soul_wood) ====================

    // 灵焰木原木
    public static final DeferredBlock<RotatedPillarBlock> SOUL_LOG = BLOCKS.registerBlock(
            "soul_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木
    public static final DeferredBlock<RotatedPillarBlock> SOUL_WOOD = BLOCKS.registerBlock(
            "soul_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮灵焰木原木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SOUL_LOG = BLOCKS.registerBlock(
            "stripped_soul_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮灵焰木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SOUL_WOOD = BLOCKS.registerBlock(
            "stripped_soul_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木木板
    public static final DeferredBlock<Block> SOUL_PLANKS = BLOCKS.registerBlock(
            "soul_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木楼梯
    public static final DeferredBlock<StairBlock> SOUL_STAIRS = BLOCKS.registerBlock(
            "soul_stairs",
            p -> new StairBlock(SOUL_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木台阶
    public static final DeferredBlock<SlabBlock> SOUL_SLAB = BLOCKS.registerBlock(
            "soul_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木栅栏
    public static final DeferredBlock<FenceBlock> SOUL_FENCE = BLOCKS.registerBlock(
            "soul_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木栅栏门
    public static final DeferredBlock<FenceGateBlock> SOUL_FENCE_GATE = BLOCKS.registerBlock(
            "soul_fence_gate",
            p -> new FenceGateBlock(SOUL_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木门
    public static final DeferredBlock<DoorBlock> SOUL_DOOR = BLOCKS.registerBlock(
            "soul_door",
            p -> new DoorBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木活板门
    public static final DeferredBlock<TrapDoorBlock> SOUL_TRAPDOOR = BLOCKS.registerBlock(
            "soul_trapdoor",
            p -> new TrapDoorBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木压力板
    public static final DeferredBlock<PressurePlateBlock> SOUL_PRESSURE_PLATE = BLOCKS.registerBlock(
            "soul_pressure_plate",
            p -> new PressurePlateBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木按钮
    public static final DeferredBlock<ButtonBlock> SOUL_BUTTON = BLOCKS.registerBlock(
            "soul_button",
            p -> new ButtonBlock(SOUL_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木树叶
    public static final DeferredBlock<LeavesBlock> SOUL_LEAVES = BLOCKS.registerBlock(
            "soul_leaves",
            p -> new TintedParticleLeavesBlock(0.01F, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    // 灵焰木树苗
    public static final DeferredBlock<SaplingBlock> SOUL_SAPLING = BLOCKS.registerBlock(
            "soul_sapling",
            p -> new SaplingBlock(ModTreeGrowers.SOUL, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    // ==================== 杜鹃木套件 (azalea_wood) ====================

    // 杜鹃木原木
    public static final DeferredBlock<RotatedPillarBlock> AZALEA_LOG = BLOCKS.registerBlock(
            "azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木
    public static final DeferredBlock<RotatedPillarBlock> AZALEA_WOOD = BLOCKS.registerBlock(
            "azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮杜鹃木原木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_LOG = BLOCKS.registerBlock(
            "stripped_azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮杜鹃木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_WOOD = BLOCKS.registerBlock(
            "stripped_azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木木板
    public static final DeferredBlock<Block> AZALEA_PLANKS = BLOCKS.registerBlock(
            "azalea_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木楼梯
    public static final DeferredBlock<StairBlock> AZALEA_STAIRS = BLOCKS.registerBlock(
            "azalea_stairs",
            p -> new StairBlock(AZALEA_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木台阶
    public static final DeferredBlock<SlabBlock> AZALEA_SLAB = BLOCKS.registerBlock(
            "azalea_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木栅栏
    public static final DeferredBlock<FenceBlock> AZALEA_FENCE = BLOCKS.registerBlock(
            "azalea_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木栅栏门
    public static final DeferredBlock<FenceGateBlock> AZALEA_FENCE_GATE = BLOCKS.registerBlock(
            "azalea_fence_gate",
            p -> new FenceGateBlock(AZALEA_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木门
    public static final DeferredBlock<DoorBlock> AZALEA_DOOR = BLOCKS.registerBlock(
            "azalea_door",
            p -> new DoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木活板门
    public static final DeferredBlock<TrapDoorBlock> AZALEA_TRAPDOOR = BLOCKS.registerBlock(
            "azalea_trapdoor",
            p -> new TrapDoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木压力板
    public static final DeferredBlock<PressurePlateBlock> AZALEA_PRESSURE_PLATE = BLOCKS.registerBlock(
            "azalea_pressure_plate",
            p -> new PressurePlateBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木按钮
    public static final DeferredBlock<ButtonBlock> AZALEA_BUTTON = BLOCKS.registerBlock(
            "azalea_button",
            p -> new ButtonBlock(AZALEA_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // ==================== 其他方块 ====================

    // 焦灰草方块 (scorched_grass_block)
    public static final DeferredBlock<Block> SCORCHED_GRASS_BLOCK = BLOCKS.registerBlock(
            "scorched_grass_block",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.6F)
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    // 焦土 (scorched_dirt)
    public static final DeferredBlock<Block> SCORCHED_DIRT = BLOCKS.registerBlock(
            "scorched_dirt",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL))
    );

    // 焦灰草丛 (scorched_grass)
    public static final DeferredBlock<Block> SCORCHED_GRASS = BLOCKS.registerBlock(
            "scorched_grass",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY))
    );

    // 焦灰枝条 (scorched_twig)
    public static final DeferredBlock<Block> SCORCHED_TWIG = BLOCKS.registerBlock(
            "scorched_twig",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY))
    );

    // 仙人球 (tiny_cactus)
    public static final DeferredBlock<Block> TINY_CACTUS = BLOCKS.registerBlock(
            "tiny_cactus",
            p -> new Block(p
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY))
    );

    // 制箭台 (fletching_table) - 使用自定义方块类支持GUI
    public static final DeferredBlock<Block> FLETCHING_TABLE = BLOCKS.registerBlock(
            "fletching_table",
            p -> new FletchingTableBlock(p
                    .mapColor(MapColor.WOOD)
                    .strength(2.5F)
                    .sound(SoundType.WOOD))
    );

    public static void blockItems() {
        // 焚烬木套件物品
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

        // 灵焰木套件物品
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

        // 杜鹃木套件物品
        ITEMS.registerSimpleBlockItem(AZALEA_LOG);
        ITEMS.registerSimpleBlockItem(AZALEA_WOOD);
        ITEMS.registerSimpleBlockItem(STRIPPED_AZALEA_LOG);
        ITEMS.registerSimpleBlockItem(STRIPPED_AZALEA_WOOD);
        ITEMS.registerSimpleBlockItem(AZALEA_PLANKS);
        ITEMS.registerSimpleBlockItem(AZALEA_STAIRS);
        ITEMS.registerSimpleBlockItem(AZALEA_SLAB);
        ITEMS.registerSimpleBlockItem(AZALEA_FENCE);
        ITEMS.registerSimpleBlockItem(AZALEA_FENCE_GATE);
        ITEMS.registerSimpleBlockItem(AZALEA_DOOR);
        ITEMS.registerSimpleBlockItem(AZALEA_TRAPDOOR);
        ITEMS.registerSimpleBlockItem(AZALEA_PRESSURE_PLATE);
        ITEMS.registerSimpleBlockItem(AZALEA_BUTTON);

        // 其他方块物品
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS_BLOCK);
        ITEMS.registerSimpleBlockItem(SCORCHED_DIRT);
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS);
        ITEMS.registerSimpleBlockItem(SCORCHED_TWIG);
        ITEMS.registerSimpleBlockItem(TINY_CACTUS);
    }

    /**
     * 注册方块和物品到事件总线
     * 必须在模组构造函数中调用
     */
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        blockItems();
        ITEMS.register(bus);
    }
}
