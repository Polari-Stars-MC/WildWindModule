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
 * 瘠土更新的放置特征：焚烬木树 / 灵焰木树 / 灵魂砂岩层。
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

    public static final ResourceKey<PlacedFeature> SOUL_SANDSTONE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            WildWindBarren.id("soul_sandstone_placed")
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

        // 一个区块扫一次；高度只是为了让放置点落在可建造范围内，特征本身忽略 y
        context.register(SOUL_SANDSTONE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(BarrenConfiguredFeatures.SOUL_SANDSTONE_KEY),
                List.of(
                        CountPlacement.of(1),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
                        BiomeFilter.biome()
                )
        ));
    }

    private BarrenPlacedFeatures() {
    }
}
