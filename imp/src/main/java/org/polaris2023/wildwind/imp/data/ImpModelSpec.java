package org.polaris2023.wildwind.imp.data;

import org.polaris2023.wildwind.imp.ImpId;

import java.util.List;

/**
 * 版本无关的「模型规格」（ADR-0005）。
 *
 * <p>模型是 datagen 里 API 差异最大的一类：</p>
 * <ul>
 *   <li>26.x 用原版新式 {@code net.minecraft.client.data.models.BlockModelGenerators}
 *       （有 {@code woodProvider} / {@code family} / {@code createDoor} / {@code createTintedLeaves} 等高层助手）</li>
 *   <li>1.20 / 1.21 用 NeoForge 的 Forge 风格
 *       {@code net.neoforged.neoforge.client.model.generators.BlockStateProvider}
 *       （{@code simpleBlock} / {@code stairsBlock} / {@code axisBlock} …，没有高层木材助手）</li>
 * </ul>
 *
 * <p>本规格只描述"要生成什么形状"，不含任何 MC 类型，因此三族共用；
 * 具体怎么落到各版本 API 由 {@link ImpModelWriter} 负责。</p>
 */
public sealed interface ImpModelSpec {

    /** 一整套木材：原木/木头/去皮/木板/楼梯/台阶/栅栏/栅栏门/压力板/按钮/门/活板门/树叶。 */
    record WoodSet(
        ImpId log,
        ImpId wood,
        ImpId strippedLog,
        ImpId strippedWood,
        ImpId planks,
        ImpId stairs,
        ImpId slab,
        ImpId fence,
        ImpId fenceGate,
        ImpId pressurePlate,
        ImpId button,
        ImpId door,
        ImpId trapdoor,
        ImpId leaves,
        int leavesTint
    ) implements ImpModelSpec {
        public WoodSet {
            List.of(log, wood, strippedLog, strippedWood, planks, stairs, slab, fence,
                fenceGate, pressurePlate, button, door, trapdoor).forEach(java.util.Objects::requireNonNull);
        }

        public boolean hasLeaves() {
            return leaves != null;
        }
    }

    /**
     * 六面方块：方块状态 + 物品模型都用同一个 {@code cube_all} 模型。
     *
     * <p>对应 core datagen 里 {@code MissingModels.cube} 的语义：贴图缺失时也照样
     * 引用 {@code <namespace>:block/<path>}，客户端显示 missingno 而不是报错。</p>
     */
    record Cube(ImpId block) implements ImpModelSpec {}

    /**
     * 石料套件：基方块 + 可选的楼梯 / 台阶 / 墙，四者共用基方块的 cube 贴图。
     *
     * <p>对应 core datagen 里 {@code MissingModels.stoneSet} 的语义。三个可选成员
     * 允许为 {@code null}（只生成存在的部分）。</p>
     */
    record StoneSet(ImpId base, ImpId stairs, ImpId slab, ImpId wall) implements ImpModelSpec {
        public StoneSet {
            java.util.Objects.requireNonNull(base, "base");
        }
    }

    /** 十字植物（含默认物品模型）。 */
    record Cross(ImpId block) implements ImpModelSpec {}

    /**
     * 双层植物：自定义单模型实现（CROSS 模板 + 默认物品模型）。
     *
     * <p>与 {@link BuiltinDoublePlant} 的区别是真实存在的：26.x 的内置助手会生成
     * {@code <name>_bottom} / {@code <name>_top} 两个模型，而这里是单个模型。
     * 二者产出不同，不能互相替代（由 datagen diff 发现）。</p>
     */
    record DoublePlant(ImpId block) implements ImpModelSpec {}

    /** 双层植物：走该版本的内置助手（26.x 会生成 bottom/top 两个模型）。 */
    record BuiltinDoublePlant(ImpId block) implements ImpModelSpec {}

    /** 外观沿用某个原版方块的模型（如制箭台）。 */
    record VanillaLike(ImpId block, ImpId vanillaBlock) implements ImpModelSpec {}

    /** 平面物品模型（如刷怪蛋）。 */
    record FlatItem(ImpId item) implements ImpModelSpec {}
}
