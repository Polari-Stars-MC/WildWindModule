package org.polaris2023.wildwind.hfas.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.WoodSet;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public enum ModConfiguredFeatures implements Supplier<ResourceKey<ConfiguredFeature<?, ?>>> {
    CINDER(ctx -> new ConfiguredFeature<>(Feature.TREE, treeConfiguration(ModBlocks.CINDER))),
    EMBER(ctx -> new ConfiguredFeature<>(Feature.TREE, treeConfiguration(ModBlocks.EMBER))),
    ;

    private final ResourceKey<ConfiguredFeature<?, ?>> key;
    private final Function<BootstrapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> supplier;// data generated

    ModConfiguredFeatures(Function<BootstrapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> supplier) {
        this.key = createKey(name().toLowerCase(Locale.ROOT));
        this.supplier = supplier;
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
        for (ModConfiguredFeatures value : values()) {
            ctx.register(value.key, value.supplier.apply(ctx));
        }
    }

    @Override
    public ResourceKey<ConfiguredFeature<?, ?>> get() {
        return key;
    }

    private static TreeConfiguration treeConfiguration(WoodSet woodSet) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(woodSet.log().get().defaultBlockState()),
                new StraightTrunkPlacer(5, 2, 0),
                BlockStateProvider.simple(woodSet.leaves().get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                Optional.empty(),
                new TwoLayersFeatureSize(1, 0, 1),
                BlockStateProvider.simple(Blocks.DIRT.defaultBlockState())
        ).ignoreVines().build();
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, name));
    }
}
