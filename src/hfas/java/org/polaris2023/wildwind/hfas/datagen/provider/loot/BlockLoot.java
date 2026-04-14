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
        // 焚烬木套件
        dropSelf(ModBlocks.BLAZE_LOG.get());
        dropSelf(ModBlocks.BLAZE_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_BLAZE_LOG.get());
        dropSelf(ModBlocks.STRIPPED_BLAZE_WOOD.get());
        dropSelf(ModBlocks.BLAZE_PLANKS.get());
        dropSelf(ModBlocks.BLAZE_STAIRS.get());
        dropSelf(ModBlocks.BLAZE_SLAB.get());
        dropSelf(ModBlocks.BLAZE_FENCE.get());
        dropSelf(ModBlocks.BLAZE_FENCE_GATE.get());
        dropSelf(ModBlocks.BLAZE_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.BLAZE_BUTTON.get());
        // 树叶 - 使用树叶掉落逻辑
        add(ModBlocks.BLAZE_LEAVES.get(), createLeavesDrops(ModBlocks.BLAZE_LEAVES.get(), ModBlocks.BLAZE_SAPLING.get(), 0.05F));
        dropSelf(ModBlocks.BLAZE_SAPLING.get());
        // 门需要特殊处理
        add(ModBlocks.BLAZE_DOOR.get(), createDoorTable(ModBlocks.BLAZE_DOOR.get()));
        add(ModBlocks.BLAZE_TRAPDOOR.get(), createDoorTable(ModBlocks.BLAZE_TRAPDOOR.get()));

        // 灵焰木套件
        dropSelf(ModBlocks.SOUL_LOG.get());
        dropSelf(ModBlocks.SOUL_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_SOUL_LOG.get());
        dropSelf(ModBlocks.STRIPPED_SOUL_WOOD.get());
        dropSelf(ModBlocks.SOUL_PLANKS.get());
        dropSelf(ModBlocks.SOUL_STAIRS.get());
        dropSelf(ModBlocks.SOUL_SLAB.get());
        dropSelf(ModBlocks.SOUL_FENCE.get());
        dropSelf(ModBlocks.SOUL_FENCE_GATE.get());
        dropSelf(ModBlocks.SOUL_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.SOUL_BUTTON.get());
        // 树叶 - 使用树叶掉落逻辑
        add(ModBlocks.SOUL_LEAVES.get(), createLeavesDrops(ModBlocks.SOUL_LEAVES.get(), ModBlocks.SOUL_SAPLING.get(), 0.05F));
        dropSelf(ModBlocks.SOUL_SAPLING.get());
        // 门需要特殊处理
        add(ModBlocks.SOUL_DOOR.get(), createDoorTable(ModBlocks.SOUL_DOOR.get()));
        add(ModBlocks.SOUL_TRAPDOOR.get(), createDoorTable(ModBlocks.SOUL_TRAPDOOR.get()));

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
