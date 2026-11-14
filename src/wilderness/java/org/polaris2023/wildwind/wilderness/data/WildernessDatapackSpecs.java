package org.polaris2023.wildwind.wilderness.data;

import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpDatapackSpec;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;

import java.util.List;

/**
 * 荒野模块的数据包内建注册<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"往哪个动态注册表注册、会产出哪些条目"——纯数据、零 MC 类型，
 * 三个版本族共用同一份。"怎么发射"由
 * {@link org.polaris2023.wildwind.imp.data.ImpDatapackWriter} 按版本处理。</p>
 *
 * <p><b>为什么这里没有内容</b>：{@code BiomeModifier} 与自定义的 {@code MudcrabVariant}
 * 本身就是 MC 类型，构造由 {@code WildernessBiomeModifiers#bootstrap} /
 * {@code MudcrabVariant#bootstrapDatagen} 完成。规格只声明注册表与条目 id，
 * 真正的注册动作留在内容模块，通过 {@code ImpDatapackWriter.Sinks} 交给发射器。</p>
 *
 * <p>注册表顺序与原实现严格一致：{@code biome_modifier} → {@code entity_variant/mudcrab}。
 * 后者是荒野自建的动态注册表（{@code ww_wilderness:entity_variant/mudcrab}），
 * 所以它没有 {@code minecraft:} / {@code neoforge:} 前缀。</p>
 *
 * <p>荒野没有数据映射 Provider，故本模块只有规格、没有
 * {@code WildernessDataMapSpecs}。</p>
 */
public final class WildernessDatapackSpecs {

    private WildernessDatapackSpecs() {}

    /** {@code neoforge:biome_modifier} 注册表 id。 */
    public static final ImpId BIOME_MODIFIERS = ImpIds.idOf("neoforge", "biome_modifier");

    /** 荒野自建的泥沼蟹变种动态注册表 id。 */
    public static final ImpId MUDCRAB_VARIANT = ImpIds.idOf("ww_wilderness", "entity_variant/mudcrab");

    /**
     * 荒野模块会产出的全部动态注册内容。
     *
     * <p>条目 id 只用于去重校验与可读性，不参与发射——发射由内容模块的
     * {@code bootstrap} 决定（见 {@code WildernessDatapackProvider} 的 {@code Sinks} 实现）。
     * 泥沼蟹三个变种沿用原版温度变种的 id（{@code temperate} / {@code warm} / {@code cold}），
     * 产物因此落在 {@code data/minecraft/ww_wilderness/entity_variant/mudcrab/}。</p>
     */
    public static List<ImpDatapackSpec> datapacks() {
        return List.of(
            ImpDatapackSpec.of(BIOME_MODIFIERS,
                WildWindWilderness.id("add_mudcrab_spawns"),
                WildWindWilderness.id("add_piranha_spawns")),
            ImpDatapackSpec.of(MUDCRAB_VARIANT,
                ImpIds.idOf("minecraft", "temperate"),
                ImpIds.idOf("minecraft", "warm"),
                ImpIds.idOf("minecraft", "cold"))
        );
    }
}
