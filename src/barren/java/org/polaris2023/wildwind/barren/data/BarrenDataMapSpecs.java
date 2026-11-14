package org.polaris2023.wildwind.barren.data;

import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.block.BarrenWoodFamilies;
import org.polaris2023.wildwind.core.block.WoodFamily;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpDataMapSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * 瘠土模块的数据映射<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"哪张数据映射 + 哪个目标 + 什么值"——纯数据、零 MC 类型，
 * 三个版本族共用同一份。"怎么发射"由
 * {@link org.polaris2023.wildwind.imp.data.ImpDataMapWriter} 按版本处理。</p>
 *
 * <p>条目顺序与原实现严格一致（先去皮、后燃料；燃料按 {@link WoodFamily}
 * 的固定字段顺序），保证 datagen 产物可逐字节对比。</p>
 */
public final class BarrenDataMapSpecs {

    private BarrenDataMapSpecs() {}

    /** 木材方块家族的燃烧时长（游戏刻），与原实现逐条对应。 */
    private static final int BURN_LOG = 300;
    private static final int BURN_PLANKS = 300;
    private static final int BURN_STAIRS = 300;
    private static final int BURN_FENCE = 300;
    private static final int BURN_FENCE_GATE = 300;
    private static final int BURN_PRESSURE_PLATE = 300;
    private static final int BURN_SLAB = 150;
    private static final int BURN_BUTTON = 100;
    private static final int BURN_SAPLING = 100;

    /** 全部数据映射条目：先去皮、后燃料。 */
    public static List<ImpDataMapSpec> dataMaps() {
        List<ImpDataMapSpec> specs = new ArrayList<>();

        // ── 去皮 ──
        specs.add(new ImpDataMapSpec.Strippable(
            ImpIds.ofHolder(BarrenBlocks.CINDER_LOG), ImpIds.ofHolder(BarrenBlocks.STRIPPED_CINDER_LOG)));
        specs.add(new ImpDataMapSpec.Strippable(
            ImpIds.ofHolder(BarrenBlocks.CINDER_WOOD), ImpIds.ofHolder(BarrenBlocks.STRIPPED_CINDER_WOOD)));
        specs.add(new ImpDataMapSpec.Strippable(
            ImpIds.ofHolder(BarrenBlocks.EMBER_LOG), ImpIds.ofHolder(BarrenBlocks.STRIPPED_EMBER_LOG)));
        specs.add(new ImpDataMapSpec.Strippable(
            ImpIds.ofHolder(BarrenBlocks.EMBER_WOOD), ImpIds.ofHolder(BarrenBlocks.STRIPPED_EMBER_WOOD)));

        // ── 燃料 ──
        for (WoodFamily family : BarrenWoodFamilies.ALL) {
            appendFuels(specs, family);
        }

        return List.copyOf(specs);
    }

    /**
     * 一种木材的燃料条目，顺序与原实现一致。
     *
     * <p>燃料的目标是<b>物品</b>，而 {@link WoodFamily} 给的是<b>方块</b>。这里直接传方块 id
     * 是安全的：本仓库的木质方块一律用 {@code registerSimpleBlockItem} 注册方块物品，
     * 而它取方块自身路径作物品 id，所以"方块 id"与"方块物品 id"逐字符相同——
     * 原实现的 {@code block.asItem()} 正是靠这一点拿到物品。发射器会到 {@code ITEM}
     * 注册表里按同一 id 解析，行为与原实现一致。</p>
     */
    private static void appendFuels(List<ImpDataMapSpec> specs, WoodFamily family) {
        specs.add(fuel(ImpIds.ofHolder(family.log()), BURN_LOG));
        specs.add(fuel(ImpIds.ofHolder(family.wood()), BURN_LOG));
        specs.add(fuel(ImpIds.ofHolder(family.strippedLog()), BURN_LOG));
        specs.add(fuel(ImpIds.ofHolder(family.strippedWood()), BURN_LOG));
        specs.add(fuel(ImpIds.ofHolder(family.planks()), BURN_PLANKS));
        specs.add(fuel(ImpIds.ofHolder(family.stairs()), BURN_STAIRS));
        specs.add(fuel(ImpIds.ofHolder(family.slab()), BURN_SLAB));
        specs.add(fuel(ImpIds.ofHolder(family.fence()), BURN_FENCE));
        specs.add(fuel(ImpIds.ofHolder(family.fenceGate()), BURN_FENCE_GATE));
        specs.add(fuel(ImpIds.ofHolder(family.pressurePlate()), BURN_PRESSURE_PLATE));
        specs.add(fuel(ImpIds.ofHolder(family.button()), BURN_BUTTON));
        if (family.sapling() != null) {
            specs.add(fuel(ImpIds.ofHolder(family.sapling()), BURN_SAPLING));
        }
    }

    private static ImpDataMapSpec.Fuel fuel(ImpId item, int burnTime) {
        return new ImpDataMapSpec.Fuel(item, burnTime);
    }
}
