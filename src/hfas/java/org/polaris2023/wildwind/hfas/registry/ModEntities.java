package org.polaris2023.wildwind.hfas.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.client.eventsub.EntitiesClientHandler;
import org.polaris2023.wildwind.hfas.entity.animal.Mudcrab;


public class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(HFASMod.MOD_ID);

    private ModEntities() {
    }

    //泥沼蟹实体类型
    public static final DeferredHolder<EntityType<?>, EntityType<Mudcrab>> MUDCRAB =
            ENTITY_TYPES.registerEntityType("mudcrab", Mudcrab::new, MobCategory.WATER_CREATURE,
                    builder -> builder.sized(0.5F, 0.55F).clientTrackingRange(8)
            );

    /**
     * 向模组事件总线注册实体类型喵~
     *
     * @see EntitiesClientHandler EntitiesClientEvent 实体客户端渲染绑定
     * @see org.polaris2023.wildwind.hfas.event.EntitiesHandler EntitiesHandler 实体服务端信息（attribute等绑定）
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }

}
