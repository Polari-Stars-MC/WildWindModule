package org.polaris2023.wildwind.imp.data;

import org.polaris2023.wildwind.imp.ImpId;

import java.util.Objects;

/**
 * 版本无关的「数据映射规格」（ADR-0005）。
 *
 * <p>一条数据映射的知识正好是三段：<b>哪张数据映射</b>（{@code furnace_fuels} /
 * {@code strippables}……）、<b>哪个目标条目</b>（物品或方块的 id）、<b>什么值</b>
 * （燃烧时长 / 去皮后的方块）。前两段是纯字符串，第三段在本仓库目前也只有
 * "整数"与"另一个 id"两种形态，因此整条规格可以完全不含 MC 类型，三族共用。</p>
 *
 * <p>数据映射的<b>种类</b>由记录类型本身表达（{@link Fuel} / {@link Strippable}），
 * 而不是再加一个 {@code Kind} 枚举字段：种类与值类型是一一对应的，
 * 拆成两个字段只会允许"种类与值不匹配"这种非法状态。</p>
 *
 * <p>发射侧（{@link ImpDataMapWriter}）负责把种类映射到该版本的数据映射表常量
 * （如 {@code NeoForgeDataMaps.FURNACE_FUELS}）与对应的值对象（{@code FurnaceFuel}）。</p>
 */
public sealed interface ImpDataMapSpec {

    /**
     * 燃料条目（{@code neoforge/data_maps/item/furnace_fuels.json}）。
     *
     * <p>数据映射挂在<b>物品</b>注册表上，所以 {@code item} 必须是物品 id。
     * 内容模块常从 {@code WoodFamily} 拿到方块再传进来——本仓库的木质方块一律用
     * {@code registerSimpleBlockItem} 注册方块物品，方块 id 与物品 id 逐字符相同，
     * 因此这样传是等价的（详见 {@code ModDataMapSpecs#appendFuels} 的说明）。</p>
     *
     * @param item     物品 id（木质方块可用其同名方块 id）
     * @param burnTime 燃烧时长（游戏刻）
     */
    record Fuel(ImpId item, int burnTime) implements ImpDataMapSpec {
        public Fuel {
            Objects.requireNonNull(item, "item");
            if (burnTime <= 0) {
                throw new IllegalArgumentException("燃烧时长必须为正数：" + burnTime);
            }
        }
    }

    /**
     * 去皮条目（{@code neoforge/data_maps/block/strippables.json}）。
     *
     * @param block        被斧头右键的原方块 id
     * @param strippedBlock 去皮后的方块 id
     */
    record Strippable(ImpId block, ImpId strippedBlock) implements ImpDataMapSpec {
        public Strippable {
            Objects.requireNonNull(block, "block");
            Objects.requireNonNull(strippedBlock, "strippedBlock");
        }
    }
}
