package org.polaris2023.wildwind.imp;

import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 注册器工厂形状的版本无关口（ADR-0003）。
 *
 * <h2>差异：{@code DeferredRegister.createDataComponents}</h2>
 * <p>javap 核对三族：</p>
 * <ul>
 *   <li><b>1.20.6</b>：只有 {@code createDataComponents(String modid)}——注册表键由方法内部
 *       固定成 {@code Registries.DATA_COMPONENT_TYPE}，<b>不接受</b> {@code ResourceKey} 参数；</li>
 *   <li><b>1.21.1</b>：两个重载都在，带 {@code ResourceKey} 的是新写法，
 *       只带 {@code String} 的被标 {@code @Deprecated(forRemoval = true)}；</li>
 *   <li><b>26.2</b>：只剩带 {@code ResourceKey} 的重载。</li>
 * </ul>
 * <p>于是内容模块写 {@code createDataComponents(Registries.DATA_COMPONENT_TYPE, modid)}
 * 在 1.20.6 会因「参数个数不同」失败，写单参版本则在 26.2 直接不存在。
 * 收敛到这里：旧族用单参（那时它还不是弃用写法），新族显式传注册表键。</p>
 *
 * <p>注意：{@code DataComponentType.Builder} 的签名三族一致
 * （{@code persistent(Codec)} / {@code networkSynchronized(StreamCodec)} / {@code build()}），
 * 只有 26.x 多一个 {@code ignoreSwapAnimation()}，不影响本口子。</p>
 */
public final class ImpRegistries {

    private ImpRegistries() {}

    /** 26.x / 1.21：显式传注册表键（1.21.1 起这是推荐写法）。 */
    @Since("1.21")
    @Name("createDataComponents")
    public static DeferredRegister.DataComponents createDataComponentsModern(String modId) {
        return DeferredRegister.createDataComponents(
                net.minecraft.core.registries.Registries.DATA_COMPONENT_TYPE, modId);
    }

    /** 1.20.6：只有单参版本，注册表键在方法内部固定。 */
    @Until("1.21")
    @Name("createDataComponents")
    public static DeferredRegister.DataComponents createDataComponentsLegacy(String modId) {
        return DeferredRegister.createDataComponents(modId);
    }
}
