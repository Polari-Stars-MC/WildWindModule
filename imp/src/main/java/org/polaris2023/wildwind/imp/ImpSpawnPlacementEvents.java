package org.polaris2023.wildwind.imp;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * 生成位置事件的订阅宿主。
 *
 * <p>为什么要单独一个类：{@code @EventBusSubscriber.modid()} <b>没有默认值</b>，必须写死；
 * 而 {@link ImpSpawnPlacements} 是被三个内容模块共用的中性工具类，不该绑定某个 modid。
 * 订阅宿主反过来——它只为 {@code ww_core} 的幼蛛服务，写死 modid 才正确。</p>
 *
 * <p>它之所以能放在 imp 里：imp 的类会被编译进 core 的输出、由 mod 加载器加载
 * （ADR-0003 的 srcDir 机制），所以 {@code @EventBusSubscriber} 与内容模块里写的
 * 完全等效。</p>
 *
 * <p>订阅方法本身调 {@link ImpSpawnPlacements#onSpawnPlacements(Object)} 不行——参数类型
 * 必须精确匹配事件类，所以这里也按族写两份、由预处理器删除不适用的那份。</p>
 *
 * @author safe_saffawa
 */
@EventBusSubscriber(modid = "ww_core")
public final class ImpSpawnPlacementEvents {

    /**
     * 26.x / 1.21 的订阅方法。
     *
     * <p>事件类在 <b>1.21 就改名了</b>（javap 核对：{@code SpawnPlacementRegisterEvent}
     * 只在 20.6.144 里存在；21.1.209 与 26.2 都已是
     * {@code RegisterSpawnPlacementsEvent}），所以分界是 {@code 1.21} 而不是 {@code 26.1}。</p>
     */
    @org.polaris2023.wildwind.imp.annotations.Since("1.21")
    @SubscribeEvent
    public static void onRegisterSpawnPlacements(
            net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {
        ImpSpawnPlacements.forEach(rule -> event.register(rule.type(), rule.placement(),
            rule.heightmap(), rule.predicate(),
            net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE));
    }

    /**
     * 1.20.6 的订阅方法（旧类名）。
     */
    @org.polaris2023.wildwind.imp.annotations.Until("1.21")
    @SubscribeEvent
    public static void onSpawnPlacementRegister(
            net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent event) {
        ImpSpawnPlacements.forEach(rule -> event.register(rule.type(), rule.placement(),
            rule.heightmap(), rule.predicate(),
            net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent.Operation.REPLACE));
    }

    private ImpSpawnPlacementEvents() {}
}
