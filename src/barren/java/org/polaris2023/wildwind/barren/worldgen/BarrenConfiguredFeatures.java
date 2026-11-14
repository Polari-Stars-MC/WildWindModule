package org.polaris2023.wildwind.barren.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;

/**
 * 瘠土更新的配置特征：焚烬木树 / 灵焰木树。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class BarrenConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CINDER_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            WildWindBarren.id("cinder_tree").unwrap()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> EMBER_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            WildWindBarren.id("ember_tree").unwrap()
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        BlockStateProvider belowTrunkProvider = TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes);

        // 焚烬木树
        context.register(CINDER_TREE_KEY, new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(BarrenBlocks.CINDER_LOG.get()),
                        new StraightTrunkPlacer(4, 2, 0),
                        BlockStateProvider.simple(BarrenBlocks.CINDER_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1),
                        belowTrunkProvider
                ).build()));

        // 灵焰木树
        context.register(EMBER_TREE_KEY, new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(BarrenBlocks.EMBER_LOG.get()),
                        new StraightTrunkPlacer(4, 2, 0),
                        BlockStateProvider.simple(BarrenBlocks.EMBER_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1),
                        belowTrunkProvider
                ).build()));
    }

    private BarrenConfiguredFeatures() {
    }
}
