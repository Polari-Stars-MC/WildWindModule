package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModDataMapsProvider extends DataMapProvider {
    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    public ModDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.STRIPPABLES)
                .add(ModBlocks.AZALEA_LOG.getKey(), new Strippable(ModBlocks.STRIPPED_AZALEA_LOG.get()), false)
                .add(ModBlocks.AZALEA_WOOD.getKey(), new Strippable(ModBlocks.STRIPPED_AZALEA_WOOD.get()), false)
                .add(ModBlocks.BLAZE_LOG.getKey(), new Strippable(ModBlocks.STRIPPED_BLAZE_LOG.get()), false)
                .add(ModBlocks.BLAZE_WOOD.getKey(), new Strippable(ModBlocks.STRIPPED_BLAZE_WOOD.get()), false)
                .add(ModBlocks.SOUL_LOG.getKey(), new Strippable(ModBlocks.STRIPPED_SOUL_LOG.get()), false)
                .add(ModBlocks.SOUL_WOOD.getKey(), new Strippable(ModBlocks.STRIPPED_SOUL_WOOD.get()), false)
        ;
    }
}
