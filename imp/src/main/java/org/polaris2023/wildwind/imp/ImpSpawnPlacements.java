package org.polaris2023.wildwind.imp;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.levelgen.Heightmap;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体生成位置注册的版本无关入口。
 *
 * <h2>问题本质</h2>
 * <p>NeoForge 把这个事件<b>改了类名</b>——{@code register(...)} 的五个参数完全一致
 * （javap 核对 20.6.144 / 21.1.209 / 26.2.0.75）：</p>
 * <ul>
 *   <li>26.x 与 <b>1.21</b>：{@code net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent}</li>
 *   <li><b>只有 1.20.6</b>：{@code net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent}</li>
 * </ul>
 * <p>分界是 {@code 1.21}，不是 {@code 26.1}——21.1.209 里旧类名已经不存在了。</p>
 *
 * <p>事件订阅方法的<b>参数类型属于方法签名</b>：{@code @Name} 改不了它（那只改我们自己声明的
 * 符号名），{@code import} 又不能带版本注解，而且 {@code @Since}/{@code @Until} 只对
 * <b>imp 源码</b>生效（预处理器不处理内容模块）。所以「内容模块一份源码订阅两族事件」
 * 在当前架构下不可能实现。</p>
 *
 * <h2>做法</h2>
 * <p>把 {@code @SubscribeEvent} 方法写进本类：imp 的类会被编译进内容模块的输出、由 mod
 * 加载器加载（ADR-0003 的 srcDir 机制），所以 {@code @EventBusSubscriber} 在这里同样生效。
 * 一个订阅方法用 {@code @Since}、另一份用 {@code @Until}，预处理器按族删掉不适用的那份。</p>
 *
 * <p>内容模块只调用 {@link #declare} 声明规则，不接触任何版本分叉类型。</p>
 *
 * <p><b>modid 约定</b>：本类固定挂 {@code ww_core} 的总线——因为生成位置注册目前只有
 * core 的幼蛛需要。其它模块若要注册，再按同样模式加一个类（不能复用本类，因为
 * {@code @EventBusSubscriber.modid()} 没有默认值）。</p>
 */
public final class ImpSpawnPlacements {

    private ImpSpawnPlacements() {}

    /** 一条待注册规则。 */
    public record Rule<T extends Mob>(
            EntityType<T> type,
            net.minecraft.world.entity.SpawnPlacementType placement,
            Heightmap.Types heightmap,
            net.minecraft.world.entity.SpawnPlacements.SpawnPredicate<T> predicate) {}

    private static final Map<EntityType<?>, Rule<?>> PENDING = new ConcurrentHashMap<>();

    /** 声明一个实体的生成位置规则（内容模块初始化时调用）。 */
    public static <T extends Mob> void declare(EntityType<T> type,
                                               net.minecraft.world.entity.SpawnPlacementType placement,
                                               Heightmap.Types heightmap,
                                               net.minecraft.world.entity.SpawnPlacements.SpawnPredicate<T> predicate) {
        PENDING.put(type, new Rule<>(type, placement, heightmap, predicate));
    }

    @SuppressWarnings("unchecked")
    static <T extends Mob> void forEach(java.util.function.Consumer<Rule<T>> sink) {
        for (Rule<?> raw : PENDING.values()) {
            sink.accept((Rule<T>) raw);
        }
    }

    // ── 26.x / 1.21：RegisterSpawnPlacementsEvent ────────────────────────

    /**
     * 26.x / 1.21 的订阅入口。
     *
     * <p>分界是 {@code 1.21}：事件类在 1.21 就改名了（javap 核对：
     * {@code SpawnPlacementRegisterEvent} 只在 20.6.144 存在）。</p>
     */
    @Since("1.21")
    @Name("onSpawnPlacements")
    public static void onSpawnPlacementsModern(
            net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {
        forEach(rule -> event.register(rule.type(), rule.placement(), rule.heightmap(), rule.predicate(),
            net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE));
    }

    // ── 1.20.6：SpawnPlacementRegisterEvent ──────────────────────────────

    /** 1.20.6 的订阅入口（旧类名）。 */
    @Until("1.21")
    @Name("onSpawnPlacements")
    public static void onSpawnPlacementsLegacy(
            net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent event) {
        forEach(rule -> event.register(rule.type(), rule.placement(), rule.heightmap(), rule.predicate(),
            net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent.Operation.REPLACE));
    }
}
