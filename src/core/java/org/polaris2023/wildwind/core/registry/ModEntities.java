package org.polaris2023.wildwind.core.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.entity.monster.Spiderling;
import org.polaris2023.wildwind.imp.ImpEntityTypes;

/**
 * 核心模块的实体类型注册。
 *
 * <p>注册器与 {@code EntityType.Builder} 的差异（26.x 的 {@code DeferredRegister.Entities}
 * 与 {@code notInPeaceful()} 在 1.20/1.21 不存在）收敛在 {@link ImpEntityTypes}。</p>
 *
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            ImpEntityTypes.createRegister(WildWindCore.MOD_ID);

    /** 幼蛛实体类型 */
    public static final DeferredHolder<EntityType<?>, EntityType<Spiderling>> SPIDERLING =
            ImpEntityTypes.registerEntityType(ENTITY_TYPES, "spiderling", Spiderling::new, MobCategory.MONSTER,
                    new ImpEntityTypes.ImpEntityTypeSpec(
                            0.55F, 0.35F, 0.15F, 0.765F, 8, true));

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }

    private ModEntities() {
    }
}
