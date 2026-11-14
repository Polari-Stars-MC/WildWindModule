package org.polaris2023.wildwind.barren.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;

import java.util.List;

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
            WildWindBarren.id("cinder_tree")
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> EMBER_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            WildWindBarren.id("ember_tree")
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> NITRIFIED_SANDSTONE_ORE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            WildWindBarren.id("nitrified_sandstone_ore")
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

        // 沙漠砂岩中的硝化砂岩矿团：尺寸在 1-10 之间由 RandomSizeOreFeature 随机
        context.register(NITRIFIED_SANDSTONE_ORE_KEY, new ConfiguredFeature<>(
                BarrenFeatures.RANDOM_SIZE_ORE.get(),
                new OreConfiguration(
                        List.of(OreConfiguration.target(
                                new BlockMatchTest(Blocks.SANDSTONE),
                                BarrenBlocks.NITRIFIED_SANDSTONE.get().defaultBlockState()
                        )),
                        10,
                        0.0F
                )
        ));
    }

    private BarrenConfiguredFeatures() {
    }
}
