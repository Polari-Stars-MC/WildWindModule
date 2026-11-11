package org.polaris2023.wildwind.barren.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

/**
 * 瘠土更新的树木生长器：焚烬木 / 灵焰木。
 *
 * @author baka4n
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class BarrenTreeGrowers {

    public static final TreeGrower CINDER = new TreeGrower(
            "cinder",
            Optional.empty(),
            Optional.of(configured(BarrenConfiguredFeatures.CINDER_TREE_KEY)),
            Optional.empty()
    );

    public static final TreeGrower EMBER = new TreeGrower(
            "ember",
            Optional.empty(),
            Optional.of(configured(BarrenConfiguredFeatures.EMBER_TREE_KEY)),
            Optional.empty()
    );

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(ResourceKey<ConfiguredFeature<?, ?>> key) {
        return key;
    }


    private BarrenTreeGrowers() {
    }
}
