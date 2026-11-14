package org.polaris2023.wildwind.imp.client;

import net.minecraft.client.model.geom.ModelPart;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.client.bedrock.BedrockAnimation;
import org.polaris2023.wildwind.imp.client.bedrock.BedrockGeometry;
import org.polaris2023.wildwind.imp.client.bedrock.GeoAnimationState;
import org.polaris2023.wildwind.imp.client.bedrock.GeoModelBuilder;
import org.polaris2023.wildwind.imp.client.bedrock.GeoResources;

import java.util.Map;

/**
 * 版本无关的 Bedrock 实体渲染装配层——<b>GeckoLib 的替代品</b>。
 *
 * <p>为什么要有这一层：GeckoLib 的渲染/动画 API 在 4.x（1.20 / 1.21 线）与 5.x（26.x 线）
 * 之间被整体重写过（包名、泛型元数、控制器构造器全不同），而且它<b>按 MC 版本分别发布</b>，
 * 4.x 还不在同一个 maven group 下。把它换成原版 {@code ModelPart} + 自己解析 Blockbench
 * 资源后，就只剩 Minecraft 自身的版本差异需要处理，少了一整个外部变量。</p>
 *
 * <h2>分层</h2>
 * <ol>
 *   <li>{@code BedrockGeometry} / {@code BedrockAnimation}：解析 {@code .geo.json} 与
 *       {@code .animation.json}（纯数据，零 MC 类型）</li>
 *   <li>{@code GeoModelBuilder}：把几何翻成 {@code ModelPart} 层级</li>
 *   <li>{@code GeoAnimationState}：关键帧求值 + 动画状态机</li>
 *   <li>{@code GeoResources}：资源加载与每帧驱动 {@code ModelPart}</li>
 *   <li><b>本类</b>：把上面这些装配成内容模块可以直接用的「模型 + 渲染器」</li>
 * </ol>
 *
 * <h2>与 GeckoLib 的取舍</h2>
 * <p>GeckoLib 支持的功能远超本仓库所需（molang、动画控制器状态机、表情、渲染类型切换…）。
 * 这里只保留实际用到的部分：<b>按条件循环播放一条动画 + 受击/攻击时插入一次性动画</b>。
 * 这是有意的能力收缩，换来的是"不再依赖任何版本分叉的第三方库"。</p>
 */
public final class ImpGeoRig {

    private ImpGeoRig() {}

    /**
     * 一套装配好的模型骨架：{@code ModelPart} 根 + 骨骼父子表 + 动画状态机。
     *
     * <p>内容模块的「模型类」持有它，渲染器每帧调用 {@link #update()} 驱动动画。</p>
     */
    public static final class Rig {

        private final Map<String, BedrockAnimation.Clip> clips;
        private final GeoResources.Binding binding;

        private Rig(Map<String, BedrockAnimation.Clip> clips, GeoResources.Binding binding) {
            this.clips = clips;
            this.binding = binding;
        }

        /**
         * 烘焙一个模型。
         *
         * @param geometry 已解析的几何（{@code null} 时退化为空骨架，不崩溃）
         * @param clips    动画集合
         */
        public static Rig bake(BedrockGeometry.Geometry geometry,
                               Map<String, BedrockAnimation.Clip> clips) {
            if (geometry == null) {
                // 资源缺失：给一个空骨架，让渲染器能安全跑而不 NPE
                BedrockGeometry.Geometry empty = new BedrockGeometry.Geometry(
                    "empty", 64, 64, 0, 0, new float[] {0F, 0F, 0F}, java.util.List.of());
                GeoModelBuilder.Built built = GeoModelBuilder.build(empty);
                GeoAnimationState state = new GeoAnimationState();
                return new Rig(Map.of(), new GeoResources.Binding(built.layer().bakeRoot(), built.parents(), state));
            }
            GeoModelBuilder.Built built = GeoModelBuilder.build(geometry);
            GeoAnimationState state = new GeoAnimationState();
            return new Rig(clips, new GeoResources.Binding(built.layer().bakeRoot(), built.parents(), state));
        }

        /** 每帧推进动画并写回 {@code ModelPart}。 */
        public void update() {
            this.binding.update();
        }

        /** 根 {@code ModelPart}（供渲染器渲染）。 */
        public ModelPart root() {
            return this.binding.root();
        }

        public GeoAnimationState state() {
            return this.binding.state();
        }

        /** 已加载的动画集合，便于上层按名字注册到状态机。 */
        public Map<String, BedrockAnimation.Clip> clips() {
            return this.clips;
        }

        /**
         * 注册一条动画到状态机。
         *
         * @param key      状态机内部键（上层自定义，如 {@code "move.walk"}）
         * @param clipName 动画文件里的 key（如 {@code "move.walk"}）
         */
        public Rig register(String key, String clipName, int priority) {
            BedrockAnimation.Clip clip = this.clips.get(clipName);
            if (clip != null) {
                this.binding.state().register(key, clip, priority);
            }
            return this;
        }

        /** 声明当前要循环播放的动画键；{@code null} 表示停止。 */
        public void loop(String key) {
            this.binding.state().looping(key);
        }

        /** 插入一次性动画。 */
        public void playOnce(String key) {
            this.binding.state().trigger(key);
        }

        public boolean isPlayingOnce() {
            return this.binding.state().isPlayingOneShot();
        }
    }

    /**
     * 从资源包加载并烘焙一个模型（客户端初始化/资源重载后调用）。
     *
     * @param manager 资源管理器
     * @param modelId 模型 id（{@code <ns>:<path>}，对应 {@code geckolib/models/<path>.geo.json}）
     */
    public static Rig load(net.minecraft.server.packs.resources.ResourceManager manager, ImpId modelId) {
        BedrockGeometry.Geometry geometry = GeoResources.geometry(manager, modelId);
        Map<String, BedrockAnimation.Clip> clips = GeoResources.animations(manager, modelId);
        return Rig.bake(geometry, clips);
    }
}
