package org.polaris2023.wildwind.barren.block;

import org.polaris2023.wildwind.core.block.WoodFamily;

import java.util.List;

/**
 * 瘠土更新的木材家族清单（焚烬木 / 灵焰木）。
 *
 * @author safe_saffawa
 */
public final class BarrenWoodFamilies {

    public static final WoodFamily CINDER = new WoodFamily(
            "cinder",
            0x00FFFF,
            BarrenBlocks.CINDER_LOG,
            BarrenBlocks.CINDER_WOOD,
            BarrenBlocks.STRIPPED_CINDER_LOG,
            BarrenBlocks.STRIPPED_CINDER_WOOD,
            BarrenBlocks.CINDER_PLANKS,
            BarrenBlocks.CINDER_STAIRS,
            BarrenBlocks.CINDER_SLAB,
            BarrenBlocks.CINDER_FENCE,
            BarrenBlocks.CINDER_FENCE_GATE,
            BarrenBlocks.CINDER_DOOR,
            BarrenBlocks.CINDER_TRAPDOOR,
            BarrenBlocks.CINDER_PRESSURE_PLATE,
            BarrenBlocks.CINDER_BUTTON,
            BarrenBlocks.CINDER_LEAVES,
            BarrenBlocks.CINDER_SAPLING
    );

    public static final WoodFamily EMBER = new WoodFamily(
            "ember",
            0xFF8000,
            BarrenBlocks.EMBER_LOG,
            BarrenBlocks.EMBER_WOOD,
            BarrenBlocks.STRIPPED_EMBER_LOG,
            BarrenBlocks.STRIPPED_EMBER_WOOD,
            BarrenBlocks.EMBER_PLANKS,
            BarrenBlocks.EMBER_STAIRS,
            BarrenBlocks.EMBER_SLAB,
            BarrenBlocks.EMBER_FENCE,
            BarrenBlocks.EMBER_FENCE_GATE,
            BarrenBlocks.EMBER_DOOR,
            BarrenBlocks.EMBER_TRAPDOOR,
            BarrenBlocks.EMBER_PRESSURE_PLATE,
            BarrenBlocks.EMBER_BUTTON,
            BarrenBlocks.EMBER_LEAVES,
            BarrenBlocks.EMBER_SAPLING
    );

    public static final List<WoodFamily> ALL = List.of(CINDER, EMBER);

    private BarrenWoodFamilies() {
    }
}
