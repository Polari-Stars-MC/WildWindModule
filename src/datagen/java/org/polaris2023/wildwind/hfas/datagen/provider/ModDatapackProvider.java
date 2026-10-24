package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.wildwind.wilderness.entity.animal.MudcrabVariant;
import org.polaris2023.wildwind.wilderness.registry.DatadrivenRegistryKey;
import org.polaris2023.wildwind.hfas.worldgen.ModBiomeModifiers;
import org.polaris2023.wildwind.hfas.worldgen.ModConfiguredFeatures;
import org.polaris2023.wildwind.hfas.worldgen.ModPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ReleaseModule module) {
        super(output, registries, new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
                        .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
                        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
                        .add(DatadrivenRegistryKey.MUDCRAB_VARIANT, MudcrabVariant::bootstrapDatagen),
                // Existing mudcrab variant IDs are minecraft:temperate/warm/cold. Keep these
                // entries in Wilderness without changing saved entity variant identifiers.
                module == ReleaseModule.WILDERNESS ? Set.of(module.id(), "minecraft") : Set.of(module.id()));
    }
}
