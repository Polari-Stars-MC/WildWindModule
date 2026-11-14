package org.polaris2023.wildwind.core.block;

import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.imp.ImpBlocks;
import org.polaris2023.wildwind.imp.ImpLegacy;

/**
 * 核心模块的方块注册：杜鹃木套件 / 制箭台 / 玫瑰。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WildWindCore.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WildWindCore.MOD_ID);

    // 杜鹃木方块类型
    public static final BlockSetType AZALEA_BLOCK_SET = BlockSetType.register(new BlockSetType("azalea"));
    public static final WoodType AZALEA_WOOD_TYPE = WoodType.register(new WoodType("azalea", AZALEA_BLOCK_SET));

    // ==================== 杜鹃木套件 ====================

    public static final DeferredBlock<RotatedPillarBlock> AZALEA_LOG = ImpBlocks.registerBlock(BLOCKS,
            "azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> AZALEA_WOOD = ImpBlocks.registerBlock(BLOCKS,
            "azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_LOG = ImpBlocks.registerBlock(BLOCKS,
            "stripped_azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_WOOD = ImpBlocks.registerBlock(BLOCKS,
            "stripped_azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<Block> AZALEA_PLANKS = ImpBlocks.registerBlock(BLOCKS,
            "azalea_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<StairBlock> AZALEA_STAIRS = ImpBlocks.registerBlock(BLOCKS,
            "azalea_stairs",
            p -> new StairBlock(AZALEA_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<SlabBlock> AZALEA_SLAB = ImpBlocks.registerBlock(BLOCKS,
            "azalea_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceBlock> AZALEA_FENCE = ImpBlocks.registerBlock(BLOCKS,
            "azalea_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceGateBlock> AZALEA_FENCE_GATE = ImpBlocks.registerBlock(BLOCKS,
            "azalea_fence_gate",
            p -> new FenceGateBlock(AZALEA_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<DoorBlock> AZALEA_DOOR = ImpBlocks.registerBlock(BLOCKS,
            "azalea_door",
            p -> new DoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<TrapDoorBlock> AZALEA_TRAPDOOR = ImpBlocks.registerBlock(BLOCKS,
            "azalea_trapdoor",
            p -> new TrapDoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<PressurePlateBlock> AZALEA_PRESSURE_PLATE = ImpBlocks.registerBlock(BLOCKS,
            "azalea_pressure_plate",
            p -> new PressurePlateBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ButtonBlock> AZALEA_BUTTON = ImpBlocks.registerBlock(BLOCKS,
            "azalea_button",
            p -> new ButtonBlock(AZALEA_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // ==================== 制箭台 ====================

    /** 制箭台 - 使用自定义方块类支持 GUI */
    public static final DeferredBlock<Block> FLETCHING_TABLE = ImpBlocks.registerBlock(BLOCKS,
            "fletching_table",
            p -> new FletchingTableBlock(p
                    .mapColor(MapColor.WOOD)
                    .strength(2.5F)
                    .sound(SoundType.WOOD))
    );

    // ==================== 花 ====================

    /** 玫瑰 */
    public static final DeferredBlock<FlowerBlock> ROSE = ImpBlocks.registerBlock(BLOCKS,
            "rose",
            p -> new FlowerBlock(SuspiciousStewEffects.EMPTY, ImpLegacy.noCollision(p
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)))
    );

    /** 灵魂玫瑰（单格花） */
    public static final DeferredBlock<FlowerBlock> SOUL_ROSE = ImpBlocks.registerBlock(BLOCKS,
            "soul_rose",
            p -> new FlowerBlock(SuspiciousStewEffects.EMPTY, ImpLegacy.noCollision(p
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)))
    );

    /** 灵魂玫瑰丛（双层高花） */
    public static final DeferredBlock<DoublePlantBlock> SOUL_ROSE_BUSH = ImpBlocks.registerBlock(BLOCKS,
            "soul_rose_bush",
            p -> new DoublePlantBlock(ImpLegacy.noCollision(p
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)))
    );

    /** 凋零玫瑰丛（双层高花） */
    public static final DeferredBlock<DoublePlantBlock> WITHER_ROSE_BUSH = ImpBlocks.registerBlock(BLOCKS,
            "wither_rose_bush",
            p -> new DoublePlantBlock(ImpLegacy.noCollision(p
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)))
    );

    public static void blockItems() {
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

        ITEMS.registerSimpleBlockItem(ROSE);
        ITEMS.registerSimpleBlockItem(SOUL_ROSE);
        ITEMS.registerSimpleBlockItem(SOUL_ROSE_BUSH);
        ITEMS.registerSimpleBlockItem(WITHER_ROSE_BUSH);
        ITEMS.registerSimpleBlockItem(FLETCHING_TABLE);
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        blockItems();
        ITEMS.register(bus);
    }

    private ModBlocks() {
    }
}
