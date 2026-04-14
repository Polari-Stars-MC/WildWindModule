package org.polaris2023.wildwind.hfas.datagen.provider.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

import java.util.Set;

public class BlockLoot extends BlockLootSubProvider {
    public BlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(h -> (Block) h.value()).toList();
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.SCORCHED_GRASS_BLOCK.get());
        dropSelf(ModBlocks.SCORCHED_DIRT.get());
        dropSelf(ModBlocks.SCORCHED_GRASS.get());
        dropSelf(ModBlocks.SCORCHED_TWIG.get());
        dropSelf(ModBlocks.TINY_CACTUS.get());
        dropOther(ModBlocks.FLETCHING_TABLE.get(), Items.FLETCHING_TABLE);

    }
}
