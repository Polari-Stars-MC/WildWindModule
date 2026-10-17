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


        // 杜鹃木套件
        dropSelf(ModBlocks.AZALEA_LOG.get());
        dropSelf(ModBlocks.AZALEA_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_AZALEA_LOG.get());
        dropSelf(ModBlocks.STRIPPED_AZALEA_WOOD.get());
        dropSelf(ModBlocks.AZALEA_PLANKS.get());
        dropSelf(ModBlocks.AZALEA_STAIRS.get());
        dropSelf(ModBlocks.AZALEA_SLAB.get());
        dropSelf(ModBlocks.AZALEA_FENCE.get());
        dropSelf(ModBlocks.AZALEA_FENCE_GATE.get());
        dropSelf(ModBlocks.AZALEA_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.AZALEA_BUTTON.get());
        // 门需要特殊处理
        add(ModBlocks.AZALEA_DOOR.get(), createDoorTable(ModBlocks.AZALEA_DOOR.get()));
        add(ModBlocks.AZALEA_TRAPDOOR.get(), createDoorTable(ModBlocks.AZALEA_TRAPDOOR.get()));

        // 其他方块
        dropSelf(ModBlocks.SCORCHED_GRASS_BLOCK.get());
        dropSelf(ModBlocks.SCORCHED_DIRT.get());
        dropSelf(ModBlocks.SCORCHED_GRASS.get());
        dropSelf(ModBlocks.SCORCHED_TWIG.get());
        dropSelf(ModBlocks.TINY_CACTUS.get());
        dropOther(ModBlocks.FLETCHING_TABLE.get(), Items.FLETCHING_TABLE);
    }
}

        this.dropSelf(ModBlocks.CINDER_LOG.get());
        this.dropSelf(ModBlocks.CINDER_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_CINDER_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_CINDER_WOOD.get());
        this.dropSelf(ModBlocks.CINDER_PLANKS.get());
        this.dropSelf(ModBlocks.CINDER_STAIRS.get());
        this.dropSelf(ModBlocks.CINDER_SLAB.get());
        this.dropSelf(ModBlocks.CINDER_FENCE.get());
        this.dropSelf(ModBlocks.CINDER_FENCE_GATE.get());
        this.dropSelf(ModBlocks.CINDER_DOOR.get());
        this.dropSelf(ModBlocks.CINDER_TRAPDOOR.get());
        this.dropSelf(ModBlocks.CINDER_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.CINDER_BUTTON.get());
        this.add(ModBlocks.CINDER_LEAVES.get(), block -> this.createLeavesDrops(block, ModBlocks.CINDER_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.CINDER_SAPLING.get());
        this.dropSelf(ModBlocks.EMBER_LOG.get());
        this.dropSelf(ModBlocks.EMBER_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_EMBER_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_EMBER_WOOD.get());
        this.dropSelf(ModBlocks.EMBER_PLANKS.get());
        this.dropSelf(ModBlocks.EMBER_STAIRS.get());
        this.dropSelf(ModBlocks.EMBER_SLAB.get());
        this.dropSelf(ModBlocks.EMBER_FENCE.get());
        this.dropSelf(ModBlocks.EMBER_FENCE_GATE.get());
        this.dropSelf(ModBlocks.EMBER_DOOR.get());
        this.dropSelf(ModBlocks.EMBER_TRAPDOOR.get());
        this.dropSelf(ModBlocks.EMBER_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.EMBER_BUTTON.get());
        this.add(ModBlocks.EMBER_LEAVES.get(), block -> this.createLeavesDrops(block, ModBlocks.EMBER_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.EMBER_SAPLING.get());