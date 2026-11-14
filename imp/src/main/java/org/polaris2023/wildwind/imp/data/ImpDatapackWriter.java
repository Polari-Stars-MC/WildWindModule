package org.polaris2023.wildwind.imp.data;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;

/**
 * 版本无关的「数据包内建注册发射器」（ADR-0005）。
 *
 * <p>分工：</p>
 * <ul>
 *   <li>{@link ImpDatapackSpec} = <b>生成什么</b>（哪个动态注册表 + 会产出哪些条目 id）</li>
 *   <li>本类 = <b>怎么发射</b>（按版本分段，预处理器裁掉不适用分支，{@code @Name} 把留存段改回同名）</li>
 *   <li>{@link Sinks} = 内容模块提供的极薄适配（按注册表交出该版本的引导器，通常一个方法体几行）</li>
 * </ul>
 *
 * <p><b>为什么规格里没有"内容"、而由 {@link Sinks} 交出引导器</b>：
 * {@code ConfiguredFeature} / {@code PlacedFeature} / {@code BiomeModifier} / 自定义
 * {@code MudcrabVariant} 的构造本质是 {@code Consumer<BootstrapContext<T>>}，
 * 它既是 MC 类型、又需要内容模块的领域知识（树形、群系、贴图路径……）。
 * 把这段搬进 imp 只会让 imp 反过来依赖内容模块，所以规格只留
 * "注册表 + 条目 id"，真正的注册动作留在内容模块，
 * imp 负责的正是"按规格顺序把注册表挂到 {@link RegistrySetBuilder} 上"这段纯流程编排。
 * 宁可规格薄一点，也不臆造一层假的"纯数据特征定义"再在发射器里翻译回 MC 类型——
 * 那只会把领域知识挪进 imp，反而破坏分层。</p>
 *
 * <p><b>为什么这个类没有旧版分支</b>：{@code RegistrySetBuilder} 是
 * {@code net.minecraft.core} 里的<b>非 datagen 专属</b>类，1.20.6 / 1.21.1 / 26.2 三代实测完全同形
 * （下方 {@code apply} 上有 javap 证据），所以"挂注册表"这段流程本身不需要分叉，
 * {@code apply} 也就只有一份实现、三个族共用。</p>
 */
public final class ImpDatapackWriter {

    private ImpDatapackWriter() {}

    /**
     * 内容模块实现的薄适配。
     *
     * <p>返回 {@code Object} 而不是 {@code RegistrySetBuilder.RegistryBootstrap<T>}：
     * 那个嵌套类型要写出 {@code T} 的具体实参，而 {@code Sinks} 是<b>一个</b>接口要同时
     * 服务三种注册表（{@code ConfiguredFeature} / {@code PlacedFeature} /
     * {@code BiomeModifier}），签名里无法同时表达。内容模块在实现内部用局部变量
     * 保留强类型（{@code RegistrySetBuilder.RegistryBootstrap<PlacedFeature> b = ...::bootstrap;}），
     * 类型安全因此仍在实现处，只是不在接口签名处。这是本方案唯一的妥协，
     * 与 {@link ImpRecipeWriter.Sinks} 同源。</p>
     */
    public interface Sinks {

        /**
         * 交出该注册表在当前版本的引导器。
         *
         * <p>三代都是 {@code RegistrySetBuilder.RegistryBootstrap<T>}——旧版位置是
         * {@code net.minecraft.core.RegistrySetBuilder$RegistryBootstrap}，
         * 不是 1.20 早期曾出现过的 {@code net.minecraft.data.registries} 包。</p>
         *
         * @param registry 注册表 id（来自 {@link ImpDatapackSpec#registry()}）
         * @throws IllegalArgumentException 内容模块不认识该注册表时
         */
        Object bootstrap(ImpId registry);
    }

    /**
     * 按规格顺序构造 {@link RegistrySetBuilder}。
     *
     * <p><b>单实现在三族同形</b>，依据（{@code javap} 实测）：</p>
     * <ul>
     *   <li>{@code neoforge-21.1.209-merged.jar} 与 {@code neoforge-20.6.144-merged.jar} 的
     *       {@code net.minecraft.core.RegistrySetBuilder} 均为：
     *       <pre>
     *       public &lt;T&gt; RegistrySetBuilder add(ResourceKey<? extends Registry<T>>,
     *                                          RegistrySetBuilder.RegistryBootstrap&lt;T&gt;)
     *       public &lt;T&gt; RegistrySetBuilder add(ResourceKey<? extends Registry<T>>,
     *                                          Lifecycle, RegistryBootstrap&lt;T&gt;)
     *       </pre>
     *       与 26.2 的 {@code minecraft-patched-26.2.0.75-merged.jar} 逐行一致；</li>
     *   <li>嵌套接口 {@code RegistryBootstrap<T>} 三代都是
     *       {@code void run(net.minecraft.data.worldgen.BootstrapContext<T>)}；</li>
     *   <li>{@code ResourceKey.createRegistryKey(ResourceLocation)} /
     *       {@code (Identifier)} 的返回类型在两代都是
     *       {@code ResourceKey<Registry<T>>}，由 {@code ImpId#unwrap()} 按版本分叉提供实参。</li>
     * </ul>
     *
     * <p>唯一没被抽到这里的是宿主 Provider：1.20.6 的
     * {@code DatapackBuiltinEntriesProvider} 只有三参
     * {@code (PackOutput, CompletableFuture<HolderLookup.Provider>, RegistrySetBuilder, Set<String>)}，
     * 1.21.1 / 26.2 还有 {@code PatchedRegistries} 等重载。那是内容模块 Provider 的构造选择，
     * 不属于本发射器的职责。</p>
     *
     * <p>顺序必须与迁移前完全一致：挂载顺序决定 {@code RegistrySetBuilder} 内部的
     * 注册阶段顺序，也就决定各动态注册表之间的可见性（例如 placed_feature 必须先
     * 能看到 configured_feature）。</p>
     */
    @SuppressWarnings("unchecked")
    public static RegistrySetBuilder apply(List<ImpDatapackSpec> specs, Sinks sinks) {
        RegistrySetBuilder builder = new RegistrySetBuilder();
        for (ImpDatapackSpec spec : specs) {
            // 注册表 id 走 ResourceKey.createRegistryKey，不硬编码 Registries.* 常量：
            // 规格里给的已经是完整路径（如 minecraft:worldgen/configured_feature）。
            ResourceKey<net.minecraft.core.Registry<Object>> registryKey =
                ResourceKey.createRegistryKey(spec.registry().unwrap());
            // 泛型在运行期被擦除，注册表与引导器的配对由内容模块保证（同一份规格驱动两边）。
            builder.add(registryKey,
                (RegistrySetBuilder.RegistryBootstrap<Object>) sinks.bootstrap(spec.registry()));
        }
        return builder;
    }
}
