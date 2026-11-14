package org.polaris2023.wildwind.imp.client.bedrock;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 动画播放状态机：替代 GeckoLib 的 {@code AnimationController} + {@code AnimatableManager}。
 *
 * <p>GeckoLib 那套体系有两个问题使它不适合作为跨版本基础：4.x 与 5.x 的包名、泛型元数、
 * 控制器构造器都不一样；而这套需求其实只用到"按条件选一条循环动画 + 受击/攻击时插入一次性动画"
 * 这么一点功能。所以这里用几十行自己实现，版本无关。</p>
 *
 * <p>职责划分：</p>
 * <ul>
 *   <li>{@link #register} 注册命名的动画片段与优先级；</li>
 *   <li>{@link #looping} 声明"条件满足时循环播放哪条"；</li>
 *   <li>{@link #trigger} 插入一次性动画（播完自动回到循环动画）；</li>
 *   <li>{@link #tick} 每帧推进时间，产出当前应该应用的所有骨骼变换。</li>
 * </ul>
 */
public final class GeoAnimationState {

    /** 一条已注册的动画。 */
    private record Entry(BedrockAnimation.Clip clip, int priority) {}

    /** 当前正在播放的实例。 */
    private static final class Playing {
        final BedrockAnimation.Clip clip;
        final boolean oneShot;
        double time;

        Playing(BedrockAnimation.Clip clip, boolean oneShot) {
            this.clip = clip;
            this.oneShot = oneShot;
        }
    }

    private final Map<String, Entry> registered = new LinkedHashMap<>();
    private Playing looping;
    private Playing oneShot;

    /** 注册一条动画；同名重复注册时后者覆盖前者。 */
    public GeoAnimationState register(String key, BedrockAnimation.Clip clip, int priority) {
        this.registered.put(key, new Entry(clip, priority));
        return this;
    }

    /**
     * 指定"条件满足时要循环播放的动画"。
     *
     * @param key 已注册的动画 key；传 {@code null} 表示停止循环动画
     */
    public void looping(String key) {
        if (key == null) {
            this.looping = null;
            return;
        }
        Entry entry = this.registered.get(key);
        if (entry == null) {
            return;
        }
        if (this.looping == null || this.looping.clip != entry.clip()) {
            this.looping = new Playing(entry.clip(), false);
        }
    }

    /** 插入一条一次性动画（播完自动回落到循环动画）。重复触发会从头重播。 */
    public void trigger(String key) {
        Entry entry = this.registered.get(key);
        if (entry == null) {
            return;
        }
        this.oneShot = new Playing(entry.clip(), true);
    }

    /** 是否有一次性动画正在播放。 */
    public boolean isPlayingOneShot() {
        return this.oneShot != null;
    }

    /**
     * 推进时间。
     *
     * @param deltaSeconds 本帧的秒数增量
     */
    public void tick(double deltaSeconds) {
        if (this.looping != null) {
            double length = this.looping.clip.length();
            this.looping.time += deltaSeconds;
            if (length > 0) {
                this.looping.time %= length;
            }
        }
        if (this.oneShot != null) {
            this.oneShot.time += deltaSeconds;
            if (this.oneShot.time >= this.oneShot.clip.length()) {
                this.oneShot = null;
            }
        }
    }

    /**
     * 求当前帧所有骨骼的变换并写入 sink。
     *
     * <p>混合策略刻意简单：一次性动画<b>覆盖</b>循环动画的同名骨骼通道，
     * 循环动画负责其余骨骼。这与 GeckoLib 的默认行为近似，且行为可预测。</p>
     *
     * @param sink 接收 {@code (骨骼名, rotation/position/scale)} 的回调
     */
    public void apply(Sink sink) {
        if (this.looping != null) {
            sample(this.looping, false, sink);
        }
        if (this.oneShot != null) {
            sample(this.oneShot, true, sink);
        }
    }

    private void sample(Playing playing, boolean override, Sink sink) {
        BedrockAnimation.Clip clip = playing.clip;
        double time = playing.time;
        if (clip.loop() && clip.length() > 0) {
            time %= clip.length();
        } else if (time > clip.length()) {
            time = clip.length();
        }

        for (Map.Entry<String, BedrockAnimation.BoneTrack> entry : clip.bones().entrySet()) {
            BedrockAnimation.BoneTrack track = entry.getValue();
            // 注意：默认值必须每次新建。sample() 在无关键帧时会把 fallback 直接返回，
            // 若复用静态数组，上层累加后会污染共享常量。
            sink.accept(entry.getKey(),
                track.rotation().sample(time, new float[] {0F, 0F, 0F}),
                track.position().sample(time, new float[] {0F, 0F, 0F}),
                track.scale().sample(time, new float[] {1F, 1F, 1F}),
                override);
        }
    }

    /** 变换接收口。{@code override} 为 true 表示这条通道应当覆盖先前写入的值。 */
    @FunctionalInterface
    public interface Sink {
        void accept(String bone, float[] rotationDegrees, float[] positionPixels, float[] scale, boolean override);
    }
}
