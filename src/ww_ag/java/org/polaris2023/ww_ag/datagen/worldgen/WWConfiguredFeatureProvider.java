package org.polaris2023.ww_ag.datagen.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.init.ModBlocks;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 09:15:47}
 */
@SuppressWarnings("CodeBlock2Expr")
public enum WWConfiguredFeatureProvider implements Supplier<ResourceKey<ConfiguredFeature<?, ?>>> {
    ORE_SALT((k, context) -> {
        FeatureUtils.register(context, k, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.SALT_ORE.getDefaultState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.DEEPSLATE_SALT_ORE.getDefaultState())
        ), 7));
    }),
    ORE_SALT_BUILD((k, context) -> {
        FeatureUtils.register(context, k, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.SALT_ORE.getDefaultState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.DEEPSLATE_SALT_ORE.getDefaultState())
        ), 7));
    }),
    ;
    private final ResourceKey<ConfiguredFeature<?, ?>> cf;
    private final BiConsumer<ResourceKey<ConfiguredFeature<?, ?>>, BootstrapContext<ConfiguredFeature<?, ?>>> cc;

    WWConfiguredFeatureProvider(BiConsumer<ResourceKey<ConfiguredFeature<?, ?>>, BootstrapContext<ConfiguredFeature<?, ?>>> cc) {
        this.cc = cc;
        cf = ResourceKey.create(Registries.CONFIGURED_FEATURE, WWAgMod.REGISTRATE.loc(name().toLowerCase(Locale.ROOT)));

    }
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        for (WWConfiguredFeatureProvider value : WWConfiguredFeatureProvider.values()) {
            value.cc.accept(value.cf, context);
        }
    }

    @Override
    public ResourceKey<ConfiguredFeature<?, ?>> get() {
        return cf;
    }
}
