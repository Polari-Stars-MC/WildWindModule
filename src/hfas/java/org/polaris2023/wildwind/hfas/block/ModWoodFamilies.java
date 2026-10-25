package org.polaris2023.wildwind.hfas.block;

import java.util.List;

/**
 * 模组木材家族清单。
 * <p>
 * 各 datagen provider 统一遍历 {@link #ALL}，避免同一条 13 参数列表在多个文件里重复维护。
 * 这里只是对 {@link ModBlocks} 已注册方块的聚合，不参与注册。
 */
public final class ModWoodFamilies {

    private ModWoodFamilies() {
    }

    /** 焚烬木（blaze） */
    public static final WoodFamily BLAZE = new WoodFamily(
            "blaze",
            0xFF8000,
            ModBlocks.BLAZE_LOG,
            ModBlocks.BLAZE_WOOD,
            ModBlocks.STRIPPED_BLAZE_LOG,
            ModBlocks.STRIPPED_BLAZE_WOOD,
            ModBlocks.BLAZE_PLANKS,
            ModBlocks.BLAZE_STAIRS,
            ModBlocks.BLAZE_SLAB,
            ModBlocks.BLAZE_FENCE,
            ModBlocks.BLAZE_FENCE_GATE,
            ModBlocks.BLAZE_DOOR,
            ModBlocks.BLAZE_TRAPDOOR,
            ModBlocks.BLAZE_PRESSURE_PLATE,
            ModBlocks.BLAZE_BUTTON,
            ModBlocks.BLAZE_LEAVES,
            ModBlocks.BLAZE_SAPLING
    );

    /** 灵焰木（soul） */
    public static final WoodFamily SOUL = new WoodFamily(
            "soul",
            0x00FFFF,
            ModBlocks.SOUL_LOG,
            ModBlocks.SOUL_WOOD,
            ModBlocks.STRIPPED_SOUL_LOG,
            ModBlocks.STRIPPED_SOUL_WOOD,
            ModBlocks.SOUL_PLANKS,
            ModBlocks.SOUL_STAIRS,
            ModBlocks.SOUL_SLAB,
            ModBlocks.SOUL_FENCE,
            ModBlocks.SOUL_FENCE_GATE,
            ModBlocks.SOUL_DOOR,
            ModBlocks.SOUL_TRAPDOOR,
            ModBlocks.SOUL_PRESSURE_PLATE,
            ModBlocks.SOUL_BUTTON,
            ModBlocks.SOUL_LEAVES,
            ModBlocks.SOUL_SAPLING
    );

    /** 杜鹃木（azalea）：沿用原版树叶 / 树苗，没有自成套的树叶 */
    public static final WoodFamily AZALEA = new WoodFamily(
            "azalea",
            0,
            ModBlocks.AZALEA_LOG,
            ModBlocks.AZALEA_WOOD,
            ModBlocks.STRIPPED_AZALEA_LOG,
            ModBlocks.STRIPPED_AZALEA_WOOD,
            ModBlocks.AZALEA_PLANKS,
            ModBlocks.AZALEA_STAIRS,
            ModBlocks.AZALEA_SLAB,
            ModBlocks.AZALEA_FENCE,
            ModBlocks.AZALEA_FENCE_GATE,
            ModBlocks.AZALEA_DOOR,
            ModBlocks.AZALEA_TRAPDOOR,
            ModBlocks.AZALEA_PRESSURE_PLATE,
            ModBlocks.AZALEA_BUTTON,
            null,
            null
    );

    public static final List<WoodFamily> ALL = List.of(BLAZE, SOUL, AZALEA);
}
