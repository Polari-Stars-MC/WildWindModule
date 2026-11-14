package org.polaris2023.wildwind.core.registry;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.component.ArrowComponent;
import org.polaris2023.wildwind.imp.ImpRegistries;

/**
 * 核心模块的数据组件注册。
 *
 * <p>注册器工厂走 {@link ImpRegistries#createDataComponents}：1.20.6 的
 * {@code DeferredRegister.createDataComponents} 只接受 {@code String}，
 * 1.21.1 起才接受 {@code ResourceKey<Registry<DataComponentType<?>>>}。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author safe_saffawa
 */
public final class ModDataComponents {
    public static final DeferredRegister.DataComponents REGISTER =
            ImpRegistries.createDataComponents(WildWindCore.MOD_ID);

    /** 箭矢组件 - 存储箭羽、箭杆、箭头类型 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ArrowComponent>> ARROW_COMPONENT =
            REGISTER.registerComponentType("arrow_component", builder ->
                    builder.persistent(ArrowComponent.CODEC).networkSynchronized(ArrowComponent.STREAM_CODEC)
            );

    public static void register(IEventBus modBus) {
        REGISTER.register(modBus);
    }

    private ModDataComponents() {
    }
}
