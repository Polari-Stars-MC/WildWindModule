package org.polaris2023.wildwind.barren.data;

import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpDatapackSpec;

import java.util.List;

/**
 * 瘠土模块的数据包内建注册<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"往哪个动态注册表注册、会产出哪些条目"——纯数据、零 MC 类型，
 * 三个版本族共用同一份。"怎么发射"由
 * {@link org.polaris2023.wildwind.imp.data.ImpDatapackWriter} 按版本处理。</p>
 *
 * <p><b>为什么这里没有特征内容</b>：{@code ConfiguredFeature} / {@code PlacedFeature} /
 * {@code BiomeModifier} 本身就是 MC 类型，它们的构造由
 * {@code BarrenConfiguredFeatures#bootstrap} 这类方法完成。规格只声明注册表与条目 id，
 * 真正的注册动作留在内容模块，通过 {@code ImpDatapackWriter.Sinks} 交给发射器。
 * 硬把树形、群系这些领域知识搬进 imp 只会让 imp 反过来依赖内容模块。</p>
 *
 * <p>注册表顺序与原实现严格一致：{@code configured_feature} → {@code placed_feature}
 * → {@code biome_modifier}。</p>
 */
public final class BarrenDatapackSpecs {

    private BarrenDatapackSpecs() {}

    /** {@code minecraft:worldgen/configured_feature} 注册表 id。 */
    public static final ImpId CONFIGURED_FEATURE = ImpIds.idOf("minecraft", "worldgen/configured_feature");

    /** {@code minecraft:worldgen/placed_feature} 注册表 id。 */
    public static final ImpId PLACED_FEATURE = ImpIds.idOf("minecraft", "worldgen/placed_feature");

    /** {@code neoforge:biome_modifier} 注册表 id。 */
    public static final ImpId BIOME_MODIFIERS = ImpIds.idOf("neoforge", "biome_modifier");

    /**
     * 瘠土模块会产出的全部动态注册内容。
     *
     * <p>条目 id 只用于去重校验与可读性，不参与发射——发射由内容模块的
     * {@code bootstrap} 决定（见 {@code BarrenDatapackProvider} 的 {@code Sinks} 实现）。</p>
     */
    public static List<ImpDatapackSpec> datapacks() {
        return List.of(
            ImpDatapackSpec.of(CONFIGURED_FEATURE,
                WildWindBarren.id("cinder_tree"),
                WildWindBarren.id("ember_tree")),
            ImpDatapackSpec.of(PLACED_FEATURE,
                WildWindBarren.id("cinder_tree_placed"),
                WildWindBarren.id("ember_tree_placed")),
            ImpDatapackSpec.of(BIOME_MODIFIERS,
                WildWindBarren.id("add_cinder_tree"),
                WildWindBarren.id("add_ember_tree"))
        );
    }
}
