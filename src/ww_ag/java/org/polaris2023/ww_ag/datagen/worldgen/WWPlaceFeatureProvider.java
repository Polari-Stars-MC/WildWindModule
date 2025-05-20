package org.polaris2023.ww_ag.datagen.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 08:40:39}
 */
public enum WWPlaceFeatureProvider implements Supplier<ResourceKey<PlacedFeature>> {
    ORE_SALT((k, context) -> {
        var lookup = context.lookup(Registries.CONFIGURED_FEATURE);
        PlacementUtils.register(
                context, k,
                lookup.getOrThrow(WWConfiguredFeatureProvider.ORE_SALT.get()),
                WWPlaceFeatureProvider.orePlacement(CountPlacement.of(2), HeightRangePlacement.uniform(
                        VerticalAnchor.absolute(-32),
                        VerticalAnchor.absolute(32)
                ))
        );
    }),
    RE_SALT_BUILD((k, context) -> {
        var lookup = context.lookup(Registries.CONFIGURED_FEATURE);
        PlacementUtils.register(
                context, k,
                lookup.getOrThrow(WWConfiguredFeatureProvider.ORE_SALT_BUILD.get()),
                WWPlaceFeatureProvider.orePlacement(CountPlacement.of(2), HeightRangePlacement.uniform(
                        VerticalAnchor.absolute(-64),
                        VerticalAnchor.absolute(64)
                ))
        );
    }),

    ;
    private final ResourceKey<PlacedFeature> pf;
    private final BiConsumer<ResourceKey<PlacedFeature>, BootstrapContext<PlacedFeature>> pc;
    WWPlaceFeatureProvider(BiConsumer<ResourceKey<PlacedFeature>, BootstrapContext<PlacedFeature>> pc) {
        pf = ResourceKey.create(Registries.PLACED_FEATURE, WWAgMod.REGISTRATE.loc(name().toLowerCase(Locale.ROOT)));
        this.pc = pc;
    }
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        for (WWPlaceFeatureProvider value : WWPlaceFeatureProvider.values()) {
            value.pc.accept(value.pf, context);
        }
    }

    @Override
    public ResourceKey<PlacedFeature> get() {
        return pf;
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }
}
