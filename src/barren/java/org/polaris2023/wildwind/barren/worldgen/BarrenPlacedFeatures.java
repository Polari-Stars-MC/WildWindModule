package org.polaris2023.wildwind.barren.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.polaris2023.wildwind.barren.WildWindBarren;

import java.util.List;

/**
 * 瘠土更新的放置特征：焚烬木树 / 灵焰木树。
 *
 * @author baka4n
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class BarrenPlacedFeatures {

    public static final ResourceKey<PlacedFeature> CINDER_TREE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            WildWindBarren.id("cinder_tree_placed")
    );

    public static final ResourceKey<PlacedFeature> EMBER_TREE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            WildWindBarren.id("ember_tree_placed")
    );

    public static final ResourceKey<PlacedFeature> NITRIFIED_SANDSTONE_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            WildWindBarren.id("nitrified_sandstone_ore_placed")
    );

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(CINDER_TREE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(BarrenConfiguredFeatures.CINDER_TREE_KEY),
                List.of()
        ));

        context.register(EMBER_TREE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(BarrenConfiguredFeatures.EMBER_TREE_KEY),
                List.of()
        ));

        // 每区块尝试 5 次，矿团大小 1-10
        context.register(NITRIFIED_SANDSTONE_ORE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(BarrenConfiguredFeatures.NITRIFIED_SANDSTONE_ORE_KEY),
                List.of(
                        CountPlacement.of(5),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(80)),
                        BiomeFilter.biome()
                )
        ));
    }

    private BarrenPlacedFeatures() {
    }
}
