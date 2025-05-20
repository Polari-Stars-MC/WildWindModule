package org.polaris2023.ww_ag.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.datagen.worldgen.WWBiomeModifyProvider;
import org.polaris2023.ww_ag.datagen.worldgen.WWConfiguredFeatureProvider;
import org.polaris2023.ww_ag.datagen.worldgen.WWPlaceFeatureProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 08:10:03}
 */
public class WWAgDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder REGISTRY_SET = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, WWConfiguredFeatureProvider::bootstrap)
            .add(Registries.PLACED_FEATURE, WWPlaceFeatureProvider::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, WWBiomeModifyProvider::bootstrap);
    public WWAgDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, REGISTRY_SET, Set.of("minecraft", WWAgMod.modid));
    }
}
