package org.polaris2023.wildwind.core.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.imp.ImpWorldGen;

import java.util.List;

/**
 * 核心模块的配置特征：杜鹃树（杜鹃木原木 + 原版杜鹃树叶）。
 *
 * <p>两处写法差异收敛在 {@link ImpWorldGen}：加权方块状态提供器的类型
 * （26.x {@code WeightedList} / 旧版 {@code SimpleWeightedRandomList}），
 * 以及 {@code TreeConfiguration} 里「树干下方方块」的传法
 * （26.x 构造器尾参 / 旧版 {@code dirt(...)} 链式设置）。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            WildWindCore.id("azalea_tree").unwrap()
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        BlockStateProvider foliageProvider = ImpWorldGen.weightedStates(List.of(
                new ImpWorldGen.WeightedState(Blocks.AZALEA_LEAVES.defaultBlockState(), 3),
                new ImpWorldGen.WeightedState(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)
        ));

        // 树干下方放缠根泥土，与原版杜鹃树一致
        BlockState belowTrunk = Blocks.ROOTED_DIRT.defaultBlockState();

        context.register(AZALEA_TREE_KEY, new ConfiguredFeature<>(Feature.TREE,
                ImpWorldGen.tree(
                        ModBlocks.AZALEA_LOG.get().defaultBlockState(),
                        new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)),
                        foliageProvider,
                        new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
                        new TwoLayersFeatureSize(1, 0, 1),
                        belowTrunk
                )));
    }

    private ModConfiguredFeatures() {
    }
}
