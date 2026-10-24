package org.polaris2023.wildwind.hfas.datagen.provider.loot;

import net.minecraft.core.HolderLookup;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class BlockLoot extends BlockLootSubProvider {
    private final ReleaseModule module;
    public BlockLoot(HolderLookup.Provider registries, ReleaseModule module) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        this.module = module;
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return java.util.stream.Stream.of(org.polaris2023.wildwind.core.block.ModBlocks.BLOCKS,org.polaris2023.wildwind.wilderness.block.ModBlocks.BLOCKS,org.polaris2023.wildwind.barren.block.ModBlocks.BLOCKS).flatMap(reg -> reg.getEntries().stream()).map(h -> (Block) h.value()).filter(module::owns).toList();
    }

    @Override
    protected void generate() {
        if (module == ReleaseModule.WILDERNESS) {
            // 焚烬木套件
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PLANKS.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_STAIRS.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SLAB.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE_GATE.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PRESSURE_PLATE.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_BUTTON.get());
            // 树叶 - 使用树叶掉落逻辑
            add(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES.get(), createLeavesDrops(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES.get(), org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING.get(), 0.05F));
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING.get());
            // 门需要特殊处理
            add(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_DOOR.get(), createDoorTable(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_DOOR.get()));
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_TRAPDOOR.get());

            // 灵焰木套件
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PLANKS.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_STAIRS.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SLAB.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE_GATE.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PRESSURE_PLATE.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_BUTTON.get());
            // 树叶 - 使用树叶掉落逻辑
            add(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES.get(), createLeavesDrops(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES.get(), org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING.get(), 0.05F));
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING.get());
            // 门需要特殊处理
            add(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_DOOR.get(), createDoorTable(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_DOOR.get()));
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_TRAPDOOR.get());

        }
        if (module == ReleaseModule.CORE) {
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get());
            // 杜鹃木套件
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_LOG.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WOOD.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_WOOD.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_STAIRS.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SLAB.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE_GATE.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PRESSURE_PLATE.get());
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_BUTTON.get());
            // 门需要特殊处理
            add(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_DOOR.get(), createDoorTable(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_DOOR.get()));
            dropSelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_TRAPDOOR.get());

        }
        if (module == ReleaseModule.WILDERNESS) {
            // 其他方块
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS_BLOCK.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_DIRT.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS.get());
            dropSelf(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_TWIG.get());
        }
        if (module == ReleaseModule.BARREN) dropSelf(org.polaris2023.wildwind.barren.block.ModBlocks.TINY_CACTUS.get());
        if (module == ReleaseModule.CORE) add(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get(), createNameableBlockEntityTable(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get()));
    }
}
