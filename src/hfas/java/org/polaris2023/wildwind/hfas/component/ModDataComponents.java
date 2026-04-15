package org.polaris2023.wildwind.hfas.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;

/**
 * 数据组件注册
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, HFASMod.MOD_ID);

    /**
     * 箭矢组件 - 存储箭羽、箭杆、箭头类型
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ArrowComponent>> ARROW_COMPONENT =
            DATA_COMPONENTS.registerComponentType("arrow_component", builder ->
                    builder.persistent(ArrowComponent.CODEC)
                            .networkSynchronized(ArrowComponent.STREAM_CODEC)
            );

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}