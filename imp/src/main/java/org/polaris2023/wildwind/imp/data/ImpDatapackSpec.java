package org.polaris2023.wildwind.imp.data;

import org.polaris2023.wildwind.imp.ImpId;

import java.util.List;
import java.util.Objects;

/**
 * 版本无关的「数据包内建注册规格」（ADR-0005）。
 *
 * <p>datagen 的知识分两层：<b>生成什么</b>（往哪些动态注册表里塞哪些条目）与
 * <b>怎么发射</b>（该版本的 {@code RegistrySetBuilder} / {@code DatapackBuiltinEntriesProvider} API）。
 * 前者是纯数据，可跨版本共用；后者因基类不同必须按版本分叉。</p>
 *
 * <p><b>为什么规格里没有"内容"</b>：数据包内建注册的内容本身就是 MC 类型——
 * {@code ConfiguredFeature} / {@code PlacedFeature} / {@code BiomeModifier} / 自定义
 * {@code MudcrabVariant}，它们的构造是 {@code Consumer<BootstrapContext<T>>} 形态的
 * {@code bootstrap} 方法，绝无可能退化成"纯数据"。因此本规格只承载
 * <b>哪个注册表（{@link #registry}）+ 会产出哪些 id（{@link #entries}）</b>，
 * 真正的注册动作由内容模块的 {@code Sinks#bootstrap} 交出来，
 * imp 只负责"按规格顺序把注册表挂到 builder 上"这段流程编排。</p>
 *
 * <p>这与 ADR-0005 的定位一致：规格宁可薄一点，也不臆造一层假的
 * "纯数据特征定义"再在发射器里翻译回 MC 类型——那只会把内容模块的领域知识
 * 挪进 imp，反而破坏分层。</p>
 *
 * @param registry 动态注册表的 id，如 {@code minecraft:worldgen/configured_feature}
 * @param entries  该注册表下本模块会产出的条目 id（按注册顺序；仅用于去重校验与可读性，
 *                 不参与发射——发射由内容模块的 bootstrap 决定）
 */
public record ImpDatapackSpec(ImpId registry, List<ImpId> entries) {

    public ImpDatapackSpec {
        Objects.requireNonNull(registry, "registry");
        entries = List.copyOf(entries);
    }

    public static ImpDatapackSpec of(ImpId registry, ImpId... entries) {
        return new ImpDatapackSpec(registry, List.of(entries));
    }

    public static ImpDatapackSpec of(ImpId registry, List<ImpId> entries) {
        return new ImpDatapackSpec(registry, entries);
    }

    /** 该规格是否描述了指定注册表。 */
    public boolean isFor(ImpId registry) {
        return this.registry.equals(registry);
    }
}
