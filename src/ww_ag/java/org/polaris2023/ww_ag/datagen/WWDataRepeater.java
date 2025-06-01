package org.polaris2023.ww_ag.datagen;

import com.mojang.serialization.Lifecycle;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author baka4n
 * {@code @Date 2025/05/24 11:40:30}
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class WWDataRepeater implements DataProvider, RegistrateProvider {
    private final AbstractRegistrate<?> parent;
    private final PackOutput output;
    private final  CompletableFuture<HolderLookup.Provider> future;
    public WWDataRepeater(AbstractRegistrate<?> parent,
                          PackOutput output,
                          CompletableFuture<HolderLookup.Provider> future) {
        this.parent = parent;
        this.output = output;
        this.future = future;
    }

    private final Set<String> modids = new HashSet<>();
    private final RegistrySetBuilder builder = new RegistrySetBuilder();

    public void add(String modid) {
        modids.add(modid);
    }

    public <E,T extends Registry<E>> void add(ResourceKey<T> key, RegistrySetBuilder.RegistryBootstrap<E> bootstrap) {
        builder.add(key, bootstrap);
    }
    public <E,T extends Registry<E>> void add(ResourceKey<T> key, Lifecycle lifecycle, RegistrySetBuilder.RegistryBootstrap<E> bootstrap) {
        builder.add(key, lifecycle, bootstrap);
    }

    public void configuredFeature(RegistrySetBuilder.RegistryBootstrap<ConfiguredFeature<?, ?>> bootstrap) {
        add(Registries.CONFIGURED_FEATURE, bootstrap);
    }

    public void placedFeature(RegistrySetBuilder.RegistryBootstrap<PlacedFeature> bootstrap) {
        add(Registries.PLACED_FEATURE, bootstrap);
    }

    public void biomeModifier(RegistrySetBuilder.RegistryBootstrap<BiomeModifier> bootstrap) {
        add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap);
    }



    private void init() {
        add("minecraft");
        add(parent.getModid());
        parent.genData(WWProviderType.REPEATER, this);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        init();
        DatapackBuiltinEntriesProvider provider = new DatapackBuiltinEntriesProvider(
                output,
                future,
                builder,
                modids
        );
        return provider.run(cachedOutput);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }
}
