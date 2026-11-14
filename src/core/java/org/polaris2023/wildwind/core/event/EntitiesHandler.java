package org.polaris2023.wildwind.core.event;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.entity.monster.Spiderling;
import org.polaris2023.wildwind.core.registry.ModEntities;
import org.polaris2023.wildwind.imp.ImpSpawnPlacements;

/**
 * 核心模块的实体属性与生成位置注册。
 *
 * <p><b>生成位置事件</b>（{@code RegisterSpawnPlacementsEvent} 在旧版叫
 * {@code SpawnPlacementRegisterEvent}）的订阅方法不在本类——它需要事件类型出现在方法签名里，
 * 而那是版本分叉的，内容模块写不了。订阅宿主放在 imp 的
 * {@code ImpSpawnPlacementEvents}（随 imp 一起编译进 core 的输出，所以总线同样能发现它）。
 * 本类只负责声明"要注册什么"。</p>
 *
 * <p><b>为什么声明写在事件回调里而不是静态块</b>：静态初始化发生在类第一次被触碰时，
 * 而 {@code @EventBusSubscriber} 的自动注册正是一次类初始化——那一刻
 * {@code DeferredRegister} 还没绑定，{@code ModEntities.SPIDERLING.get()} 会抛
 * {@code NullPointerException: Trying to access unbound value}。
 * 放进 {@link #onAttributeCreate} 就不会有这个问题：该事件触发时注册早已完成。</p>
 *
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
@EventBusSubscriber(modid = WildWindCore.MOD_ID)
public final class EntitiesHandler {

    @SubscribeEvent
    static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SPIDERLING.get(), Spiderling.createAttributes().build());
        // 此刻实体类型已绑定，可以安全取用
        ImpSpawnPlacements.declare(
            ModEntities.SPIDERLING.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules);
    }

    private EntitiesHandler() {
    }
}
