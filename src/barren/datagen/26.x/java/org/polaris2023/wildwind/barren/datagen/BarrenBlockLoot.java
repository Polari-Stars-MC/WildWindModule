package org.polaris2023.wildwind.barren.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.data.BarrenLootSpecs;
import org.polaris2023.wildwind.imp.data.ImpLootWriter;

import java.util.Set;

/**
 * 瘠土更新的方块战利品表：薄适配（ADR-0005）。
 *
 * <p>要生成什么在 {@link BarrenLootSpecs#blockLoot()}，"怎么发射"在
 * {@link ImpLootWriter}；这里只负责把 26.x 的 {@code protected} 掉落助手转出去。
 * 树叶形状刻意转调原版 {@code createLeavesDrops}，避免在 imp 里重抄一份
 * {@code table_bonus} 概率表导致产物漂移。</p>
 *
 * @author baka4n
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class BarrenBlockLoot extends BlockLootSubProvider {

    public BarrenBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return BarrenBlocks.BLOCKS.getEntries().stream().map(h -> (Block) h.value()).toList();
    }

    @Override
    protected void generate() {
        ImpLootWriter.apply(BarrenLootSpecs.blockLoot(), new ImpLootWriter.Sinks() {

            @Override
            public void dropSelf(Block block) {
                BarrenBlockLoot.this.dropSelf(block);
            }

            @Override
            public void door(Block block) {
                BarrenBlockLoot.this.add(block, BarrenBlockLoot.this.createDoorTable(block));
            }

            @Override
            public void slab(Block slab) {
                BarrenBlockLoot.this.add(slab, BarrenBlockLoot.this.createSlabItemTable(slab));
            }

            @Override
            public void silkTouchOnly(Block block) {
                BarrenBlockLoot.this.add(block, BarrenBlockLoot.this.createSilkTouchOnlyTable(block));
            }

            @Override
            public void pottedContents(Block potted) {
                BarrenBlockLoot.this.dropPottedContents(potted);
            }

            @Override
            public void leaves(Block leaves, Block sapling, float saplingChance) {
                BarrenBlockLoot.this.add(leaves,
                    BarrenBlockLoot.this.createLeavesDrops(leaves, sapling, saplingChance));
            }

            @Override
            public void noDrop(Block block) {
                BarrenBlockLoot.this.add(block, BarrenBlockLoot.noDrop());
            }
        });
    }
}
