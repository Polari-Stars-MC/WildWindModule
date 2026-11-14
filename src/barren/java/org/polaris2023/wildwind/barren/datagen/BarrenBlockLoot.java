package org.polaris2023.wildwind.barren.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.NonNull;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.block.BarrenWoodFamilies;
import org.polaris2023.wildwind.barren.registry.ModItems;
import org.polaris2023.wildwind.core.block.WoodFamily;

import java.util.Set;

/**
 * 瘠土更新的方块战利品表。
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
        for (WoodFamily family : BarrenWoodFamilies.ALL) {
            generateWood(family);
        }

        dropPottedContents(BarrenBlocks.POTTED_CINDER_SAPLING.get());
        dropPottedContents(BarrenBlocks.POTTED_EMBER_SAPLING.get());

        dropSelf(BarrenBlocks.CINDER_SHELF.get());
        dropSelf(BarrenBlocks.CINDER_SIGN.get());
        dropSelf(BarrenBlocks.CINDER_HANGING_SIGN.get());
        dropSelf(BarrenBlocks.EMBER_SHELF.get());
        dropSelf(BarrenBlocks.EMBER_SIGN.get());
        dropSelf(BarrenBlocks.EMBER_HANGING_SIGN.get());
        // 原版墙面告示牌没有掉落物，但战利品表必须存在
        add(BarrenBlocks.CINDER_WALL_SIGN.get(), noDrop());
        add(BarrenBlocks.CINDER_WALL_HANGING_SIGN.get(), noDrop());
        add(BarrenBlocks.EMBER_WALL_SIGN.get(), noDrop());
        add(BarrenBlocks.EMBER_WALL_HANGING_SIGN.get(), noDrop());

        dropSelf(BarrenBlocks.SCORCHED_GRASS_BLOCK.get());
        dropSelf(BarrenBlocks.SCORCHED_DIRT.get());
        dropSelf(BarrenBlocks.SCORCHED_GRASS.get());
        dropSelf(BarrenBlocks.SCORCHED_TWIG.get());
        dropSelf(BarrenBlocks.TINY_CACTUS.get());
        dropSelf(BarrenBlocks.QUICKSAND.get());
        dropSelf(BarrenBlocks.RED_QUICKSAND.get());
        add(BarrenBlocks.SUSPICIOUS_RED.get(), createSilkTouchOnlyTable(BarrenBlocks.SUSPICIOUS_RED.get()));

        dropSelf(BarrenBlocks.CHISELED_QUARTZ_SANDSTONE.get());
        dropSelf(BarrenBlocks.CHISELED_SOUL_SANDSTONE.get());
        dropSelf(BarrenBlocks.CUT_QUARTZ_SANDSTONE.get());
        dropSelf(BarrenBlocks.CUT_SOUL_SANDSTONE.get());
        dropSelf(BarrenBlocks.NITRIFIED_QUARTZ_SANDSTONE.get());
        dropSelf(BarrenBlocks.NITRIFIED_RED_SANDSTONE.get());
        add(BarrenBlocks.NITRIFIED_SANDSTONE.get(), createNitrifiedSandstoneDrop());
        dropSelf(BarrenBlocks.NITRIFIED_SOUL_SANDSTONE.get());
        dropSelf(BarrenBlocks.QUARTZ_SANDSTONE.get());
        dropSelf(BarrenBlocks.SOUL_SANDSTONE.get());
        dropSelf(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE.get());
        dropSelf(BarrenBlocks.SMOOTH_SOUL_SANDSTONE.get());
        dropSelf(BarrenBlocks.QUARTZ_SAND.get());
        dropSelf(BarrenBlocks.QUARTZ_QUICKSAND.get());
        dropSelf(BarrenBlocks.SOUL_QUICKSAND.get());
        dropSelf(BarrenBlocks.ASH_BLOCK.get());
        add(BarrenBlocks.SUSPICIOUS_QUARTZ_SAND.get(), createSilkTouchOnlyTable(BarrenBlocks.SUSPICIOUS_QUARTZ_SAND.get()));
        add(BarrenBlocks.SUSPICIOUS_SOUL_SAND.get(), createSilkTouchOnlyTable(BarrenBlocks.SUSPICIOUS_SOUL_SAND.get()));

        // 灰烬 / 植被（草稿）
        dropSelf(BarrenBlocks.FROGBIT.get());
        dropSelf(BarrenBlocks.ASH_BLOCK.get());

        // 灵魂砂岩衍生物 / 硝石
        dropSelf(BarrenBlocks.SOUL_SANDSTONE_STAIRS.get());
        add(BarrenBlocks.SOUL_SANDSTONE_SLAB.get(), createSlabItemTable(BarrenBlocks.SOUL_SANDSTONE_SLAB.get()));
        dropSelf(BarrenBlocks.SOUL_SANDSTONE_WALL.get());
        dropSelf(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_STAIRS.get());
        add(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_SLAB.get(), createSlabItemTable(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_SLAB.get()));
        add(BarrenBlocks.CUT_SOUL_SANDSTONE_SLAB.get(), createSlabItemTable(BarrenBlocks.CUT_SOUL_SANDSTONE_SLAB.get()));
        dropSelf(BarrenBlocks.NITER_BLOCK.get());

        // 石英砂岩衍生物
        dropSelf(BarrenBlocks.QUARTZ_SANDSTONE_STAIRS.get());
        add(BarrenBlocks.QUARTZ_SANDSTONE_SLAB.get(), createSlabItemTable(BarrenBlocks.QUARTZ_SANDSTONE_SLAB.get()));
        dropSelf(BarrenBlocks.QUARTZ_SANDSTONE_WALL.get());
        dropSelf(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_STAIRS.get());
        add(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_SLAB.get(), createSlabItemTable(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_SLAB.get()));
        add(BarrenBlocks.CUT_QUARTZ_SANDSTONE_SLAB.get(), createSlabItemTable(BarrenBlocks.CUT_QUARTZ_SANDSTONE_SLAB.get()));
    }

    /**
     * 硝化砂岩：精准采集掉落自身，否则掉落 1-4 个硝石并受时运（矿脉公式）影响。
     */
    private LootTable.Builder createNitrifiedSandstoneDrop() {
        Block block = BarrenBlocks.NITRIFIED_SANDSTONE.get();
        HolderGetter<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return createSilkTouchDispatchTable(block,
                applyExplosionDecay(block,
                        LootItem.lootTableItem(ModItems.NITER.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    private void generateWood(WoodFamily family) {
        dropSelf(family.log().get());
        dropSelf(family.wood().get());
        dropSelf(family.strippedLog().get());
        dropSelf(family.strippedWood().get());
        dropSelf(family.planks().get());
        dropSelf(family.stairs().get());
        dropSelf(family.slab().get());
        dropSelf(family.fence().get());
        dropSelf(family.fenceGate().get());
        dropSelf(family.pressurePlate().get());
        dropSelf(family.button().get());
        dropSelf(family.trapdoor().get());
        add(family.door().get(), createDoorTable(family.door().get()));

        if (family.hasLeaves()) {
            Block leaves = family.leaves().get();
            add(leaves, createLeavesDrops(leaves, family.sapling().get(), 0.05F));
            dropSelf(family.sapling().get());
        }
    }
}
