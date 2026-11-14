package org.polaris2023.wildwind.core.datagen.provider.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.data.ModLootSpecs;
import org.polaris2023.wildwind.imp.data.ImpLootWriter;

import java.util.Set;

/**
 * 核心模块的方块战利品表：薄适配（ADR-0005）。
 *
 * <p>要生成什么在 {@link ModLootSpecs#blockLoot()}，"怎么发射"在
 * {@link ImpLootWriter}；这里只负责把 26.x 的 {@code protected} 掉落助手转出去。
 * 每个助手都是 {@code protected}，imp 跨包调不到，因此必须由子类一行转发。</p>
 *
 * @author baka4n
 * @author mdjfnfn
 * @author safe_saffawa
 */
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
        ImpLootWriter.apply(ModLootSpecs.blockLoot(), new ImpLootWriter.Sinks() {

            @Override
            public void dropSelf(Block block) {
                BlockLoot.this.dropSelf(block);
            }

            @Override
            public void door(Block block) {
                BlockLoot.this.add(block, BlockLoot.this.createDoorTable(block));
            }
        });
    }
}
