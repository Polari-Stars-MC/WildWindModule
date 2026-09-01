package org.polaris2023.wildwind.hfas.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

import java.util.Optional;

/**
 * 配置特征注册
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> BLAZE_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "blaze_tree")
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> SOUL_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "soul_tree")
    );

    // 杜鹃树 - 使用杜鹃木原木
    public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "azalea_tree")
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<net.minecraft.world.level.block.Block> blocks = context.lookup(Registries.BLOCK);

        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        BlockStateProvider belowTrunkProvider = TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes);

        // 焚烬木树
        register(context, BLAZE_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BLAZE_LOG.get()),
                new StraightTrunkPlacer(4, 2, 0),
                BlockStateProvider.simple(ModBlocks.BLAZE_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1),
                belowTrunkProvider
        ).build());

        // 灵焰木树
        register(context, SOUL_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.SOUL_LOG.get()),
                new StraightTrunkPlacer(4, 2, 0),
                BlockStateProvider.simple(ModBlocks.SOUL_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1),
                belowTrunkProvider
        ).build());

        // 杜鹃树 - 使用杜鹃木原木和原版杜鹃树叶
        register(context, AZALEA_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.AZALEA_LOG.get()),
                new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)),
                new WeightedStateProvider(
                        WeightedList.<net.minecraft.world.level.block.state.BlockState>builder()
                                .add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3)
                                .add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)
                ),
                new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
                Optional.empty(),
                new TwoLayersFeatureSize(1, 0, 1),
                SimpleStateProvider.simple(Blocks.ROOTED_DIRT)

        ).build());
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC config
    ) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
