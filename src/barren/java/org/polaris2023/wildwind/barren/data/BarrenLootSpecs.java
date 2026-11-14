package org.polaris2023.wildwind.barren.data;

import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.block.BarrenWoodFamilies;
import org.polaris2023.wildwind.core.block.WoodFamily;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpLootSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * 瘠土模块的战利品表<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"生成什么"——纯数据、零 MC 类型，三个版本族共用同一份。
 * "怎么发射"由 {@link org.polaris2023.wildwind.imp.data.ImpLootWriter} 按版本处理。</p>
 *
 * <p>条目顺序与原实现严格一致，保证 datagen 产物可逐字节对比。</p>
 */
public final class BarrenLootSpecs {

    private BarrenLootSpecs() {}

    public static List<ImpLootSpec> blockLoot() {
        List<ImpLootSpec> specs = new ArrayList<>();

        for (WoodFamily family : BarrenWoodFamilies.ALL) {
            appendWood(specs, family);
        }

        specs.add(new ImpLootSpec.PottedContents(ImpIds.ofHolder(BarrenBlocks.POTTED_CINDER_SAPLING)));
        specs.add(new ImpLootSpec.PottedContents(ImpIds.ofHolder(BarrenBlocks.POTTED_EMBER_SAPLING)));

        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.CINDER_SHELF)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.CINDER_SIGN)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.CINDER_HANGING_SIGN)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.EMBER_SHELF)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.EMBER_SIGN)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.EMBER_HANGING_SIGN)));
        // 原版墙面告示牌没有掉落物，但战利品表必须存在
        specs.add(new ImpLootSpec.NoDrop(ImpIds.ofHolder(BarrenBlocks.CINDER_WALL_SIGN)));
        specs.add(new ImpLootSpec.NoDrop(ImpIds.ofHolder(BarrenBlocks.CINDER_WALL_HANGING_SIGN)));
        specs.add(new ImpLootSpec.NoDrop(ImpIds.ofHolder(BarrenBlocks.EMBER_WALL_SIGN)));
        specs.add(new ImpLootSpec.NoDrop(ImpIds.ofHolder(BarrenBlocks.EMBER_WALL_HANGING_SIGN)));

        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SCORCHED_GRASS_BLOCK)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SCORCHED_DIRT)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SCORCHED_GRASS)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SCORCHED_TWIG)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.TINY_CACTUS)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.QUICKSAND)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.RED_QUICKSAND)));
        specs.add(new ImpLootSpec.SilkTouchOnly(ImpIds.ofHolder(BarrenBlocks.SUSPICIOUS_RED)));

        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.CHISELED_QUARTZ_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.CHISELED_SOUL_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.CUT_QUARTZ_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.CUT_SOUL_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.NITRIFIED_QUARTZ_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.NITRIFIED_RED_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.NITRIFIED_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.NITRIFIED_SOUL_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.QUARTZ_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SOUL_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SMOOTH_SOUL_SANDSTONE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.QUARTZ_SAND)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.QUARTZ_QUICKSAND)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SOUL_QUICKSAND)));
        specs.add(new ImpLootSpec.SilkTouchOnly(ImpIds.ofHolder(BarrenBlocks.SUSPICIOUS_QUARTZ_SAND)));
        specs.add(new ImpLootSpec.SilkTouchOnly(ImpIds.ofHolder(BarrenBlocks.SUSPICIOUS_SOUL_SAND)));

        // 灰烬 / 植被（草稿）
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.FROGBIT)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.ASH_BLOCK)));

        // 灵魂砂岩衍生物 / 硝石
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SOUL_SANDSTONE_STAIRS)));
        specs.add(new ImpLootSpec.Slab(ImpIds.ofHolder(BarrenBlocks.SOUL_SANDSTONE_SLAB)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SOUL_SANDSTONE_WALL)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_STAIRS)));
        specs.add(new ImpLootSpec.Slab(ImpIds.ofHolder(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_SLAB)));
        specs.add(new ImpLootSpec.Slab(ImpIds.ofHolder(BarrenBlocks.CUT_SOUL_SANDSTONE_SLAB)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.NITER_BLOCK)));

        // 石英砂岩衍生物
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.QUARTZ_SANDSTONE_STAIRS)));
        specs.add(new ImpLootSpec.Slab(ImpIds.ofHolder(BarrenBlocks.QUARTZ_SANDSTONE_SLAB)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.QUARTZ_SANDSTONE_WALL)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_STAIRS)));
        specs.add(new ImpLootSpec.Slab(ImpIds.ofHolder(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_SLAB)));
        specs.add(new ImpLootSpec.Slab(ImpIds.ofHolder(BarrenBlocks.CUT_QUARTZ_SANDSTONE_SLAB)));

        return List.copyOf(specs);
    }

    private static void appendWood(List<ImpLootSpec> specs, WoodFamily family) {
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.log())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.wood())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.strippedLog())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.strippedWood())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.planks())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.stairs())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.slab())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.fence())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.fenceGate())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.pressurePlate())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.button())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.trapdoor())));
        specs.add(new ImpLootSpec.Door(ImpIds.ofHolder(family.door())));

        if (family.hasLeaves()) {
            specs.add(new ImpLootSpec.Leaves(
                ImpIds.ofHolder(family.leaves()), ImpIds.ofHolder(family.sapling()), 0.05F));
            specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.sapling())));
        }
    }
}
