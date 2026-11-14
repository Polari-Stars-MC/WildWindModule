package org.polaris2023.wildwind.imp.client;

import net.minecraft.world.entity.Entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实体侧的动画契约——替代 GeckoLib 的 {@code GeoEntity}。
 *
 * <p>GeckoLib 的实体侧接口在 4.x / 5.x 之间也不同（{@code AnimatableInstanceCache} 的包名、
 * {@code AnimationController} 的构造器、{@code AnimationTest} 的泛型），但本项目实际只用到了
 * 极少的功能：</p>
 * <ul>
 *   <li>按优先级注册若干「命名控制器」，每个控制器有一个「条件 → 播放哪条动画」的判定；</li>
 *   <li>用 {@code triggerAnim(控制器名, 动画键)} 触发一次性动画。</li>
 * </ul>
 *
 * <p>这里把它收敛成两个方法 + 一个可选的触发入口，版本无关、零外部依赖。</p>
 *
 * <h2>与 GeckoLib 写法的对应关系</h2>
 * <pre>{@code
 * // GeckoLib（旧）
 * public void registerControllers(AnimatableManager.ControllerRegistrar c) {
 *     c.add(new AnimationController<>("Move", 3, this::moveAnimController));
 *     c.add(new AnimationController<>("Attack", st -> PlayState.STOP).triggerableAnim("attack", ATTACK));
 * }
 * public void triggerAnim("Attack", "attack");
 *
 * // 现在
 * public void registerAnimations(AnimationSet set) {
 *     set.controller("Move", 3, st -> st.isMoving() ? "move.walk" : null);
 *     set.oneShot("Attack", "attack.swing");
 * }
 * this.triggerAnimation("Attack", "attack");
 * }</pre>
 *
 * @author safe_saffawa
 */
public interface ImpAnimatedEntity {

    /**
     * 注册动画。实现方通常在构造函数里调用一次。
     *
     * <p>注意：这个方法是<b>每次实例化时调用</b>，所以不要在里面积累静态状态。</p>
     */
    void registerAnimations(AnimationSet set);

    /** 实体侧的动画集：由 {@link #registerAnimations} 填充。 */
    final class AnimationSet {

        /** 控制器：名字 → 判定 {@code (state) -> 动画键（null 表示停）}。 */
        private final Map<String, Controller> controllers = new LinkedHashMap<>();

        /** 一次性动画：名字 → 动画键。 */
        private final Map<String, String> oneShots = new LinkedHashMap<>();

        /** 循环控制器。 */
        public AnimationSet controller(String name, int priority, Selector selector) {
            this.controllers.put(name, new Controller(name, priority, selector, false));
            return this;
        }

        /** 一次性动画控制器（对应 GeckoLib 的 {@code triggerableAnim}）。 */
        public AnimationSet oneShot(String name, String animationKey) {
            this.oneShots.put(name, animationKey);
            this.controllers.put(name, new Controller(name, 0, null, true));
            return this;
        }

        public Map<String, Controller> controllers() {
            return Map.copyOf(this.controllers);
        }

        public Map<String, String> oneShots() {
            return Map.copyOf(this.oneShots);
        }
    }

    /** 一个控制器。 */
    record Controller(String name, int priority, Selector selector, boolean oneShot) {}

    /**
     * 判定函数：给定实体渲染态信息，返回要循环播放的动画键（{@code null} 表示不播放）。
     *
     * <p>用 {@code Object} 接收实体本身，避免在接口里引入版本分叉的实体类型泛型；
     * 实现方强转成自己的实体类型即可（它一定是对的，因为是同一个实体注册的）。</p>
     */
    @FunctionalInterface
    interface Selector {
        String select(State state);
    }

    /** 判定时可用的状态信息（对应 GeckoLib {@code AnimationTest} 的常用部分）。 */
    record State(Entity entity, boolean isMoving, float limbSwing, float limbSwingAmount,
                 boolean isInWater, boolean isOnGround) {}
}
