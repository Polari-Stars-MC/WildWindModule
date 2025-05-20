package org.polaris2023.ww_ag.datagen.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 08:14:18}
 */
public enum WWBiomeModifyProvider implements Supplier<ResourceKey<BiomeModifier>> {
    SALT_ORR((k, context) -> {
        var biomesLookup = context.lookup(Registries.BIOME);
        var placeFeatureLookup = context.lookup(Registries.PLACED_FEATURE);
        context.register(k, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomesLookup.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                HolderSet.direct(
                        placeFeatureLookup.getOrThrow(WWPlaceFeatureProvider.ORE_SALT.get()),
                        placeFeatureLookup.getOrThrow(WWPlaceFeatureProvider.RE_SALT_BUILD.get())
                ),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
    });
    private final ResourceKey<BiomeModifier> bm;
    private final BiConsumer<ResourceKey<BiomeModifier>, BootstrapContext<BiomeModifier>> bc;
    WWBiomeModifyProvider(BiConsumer<ResourceKey<BiomeModifier>, BootstrapContext<BiomeModifier>> bc) {
        bm = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, WWAgMod.REGISTRATE.loc(name().toLowerCase(Locale.ROOT)));
        this.bc = bc;

    }
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        for (WWBiomeModifyProvider value : WWBiomeModifyProvider.values()) {
            value.bc.accept(value.bm, context);
        }
    }

    @Override
    public ResourceKey<BiomeModifier> get() {
        return bm;
    }
}
