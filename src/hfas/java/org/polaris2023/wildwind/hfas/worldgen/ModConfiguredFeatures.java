﻿package org.polaris2023.wildwind.hfas.worldgen;

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



    // 杜鹃树 - 使用杜鹃木原木
    public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "azalea_tree")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> CINDER_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "cinder_tree")
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> EMBER_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "ember_tree")
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<net.minecraft.world.level.block.Block> blocks = context.lookup(Registries.BLOCK);

        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        BlockStateProvider belowTrunkProvider = TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes);



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

        // ����ľ��
        register(context, CINDER_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.CINDER_LOG.get()),
                new net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer(4, 2, 0),
                new org.polaris2023.wildwind.hfas.worldgen.feature.foliageplacers.AcaciaFoliagePlacer(net.minecraft.util.valueproviders.UniformInt.of(2, 3), net.minecraft.util.valueproviders.ConstantInt.of(0)),
                new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().build());

        // ���ľ��
        register(context, EMBER_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.EMBER_LOG.get()),
                new net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer(5, 2, 0),
                new org.polaris2023.wildwind.hfas.worldgen.feature.foliageplacers.PalmFoliagePlacer(net.minecraft.util.valueproviders.ConstantInt.of(2), net.minecraft.util.valueproviders.ConstantInt.of(0), 2),
                new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(1, 0, 2)
        ).ignoreVines().dirt(BlockStateProvider.simple(ModBlocks.SCORCHED_DIRT.get())).build());