package org.polaris2023.wildwind.imp.client.bedrock;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.polaris2023.wildwind.imp.ImpId;

import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从资源包加载 Bedrock 几何与动画，并驱动 {@code ModelPart}。
 *
 * <p>替代 GeckoLib 的 {@code GeoModel} + {@code AnimationController} 运行时。</p>
 *
 * <h2>资源位置</h2>
 * <p>沿用仓库里已有的路径（Blockbench 导出的原文件，无需转换）：</p>
 * <ul>
 *   <li>几何：{@code assets/<ns>/geckolib/models/<modelId>.geo.json}</li>
 *   <li>动画：{@code assets/<ns>/geckolib/animations/<modelId>.animation.json}</li>
 * </ul>
 *
 * <h2>为什么不用 GeckoLib 的 4.x 路径</h2>
 * <p>GeckoLib 4.x 收 {@code geo/} 与 {@code animations/}，5.x 收 {@code geckolib/models/} 与
 * {@code geckolib/animations/}。既然我们要脱离 GeckoLib，就<b>只认自己的一套路径</b>，
 * 不必再为两代目录兼容——这也是去掉该库后的一处净收益。</p>
 *
 * <h2>跨版本</h2>
 * <p>对外一律用 {@link ImpId} 表达资源位置（{@code ImpId} 的 {@code equals/hashCode} 稳定，
 * 可以直接当缓存键），只有真正要交给 MC 时才 {@code unwrap()} 出该版本的原生类型。
 * 因此本类不需要 {@code @Since}/{@code @Until} 分段。</p>
 */
public final class GeoResources {

    private static final Gson GSON = new Gson();

    private static final Map<ImpId, BedrockGeometry.Geometry> GEOMETRY_CACHE = new ConcurrentHashMap<>();
    private static final Map<ImpId, Map<String, BedrockAnimation.Clip>> ANIMATION_CACHE = new ConcurrentHashMap<>();

    private GeoResources() {}

    /** 几何 json 的完整位置。 */
    public static ImpId geometryLocation(ImpId modelId) {
        return ImpId.of(modelId.namespace(), "geckolib/models/" + modelId.path() + ".geo.json");
    }

    /** 动画 json 的完整位置。 */
    public static ImpId animationLocation(ImpId modelId) {
        return ImpId.of(modelId.namespace(), "geckolib/animations/" + modelId.path() + ".animation.json");
    }

    /**
     * 加载几何。
     *
     * @return 加载失败时返回 {@code null}（调用方应降级为不渲染或空模型，而不是崩溃）
     */
    public static BedrockGeometry.Geometry geometry(ResourceManager manager, ImpId modelId) {
        return GEOMETRY_CACHE.computeIfAbsent(modelId, id -> {
            Optional<Resource> resource = manager.getResource(geometryLocation(id).unwrap());
            if (resource.isEmpty()) {
                return null;
            }
            try (BufferedReader reader = resource.get().openAsReader()) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                return BedrockGeometry.parse(toMap(json));
            } catch (Exception e) {
                return null;
            }
        });
    }

    /**
     * 加载动画集合。
     *
     * @return 加载失败时返回空 map
     */
    public static Map<String, BedrockAnimation.Clip> animations(ResourceManager manager, ImpId modelId) {
        return ANIMATION_CACHE.computeIfAbsent(modelId, id -> {
            Optional<Resource> resource = manager.getResource(animationLocation(id).unwrap());
            if (resource.isEmpty()) {
                return Map.of();
            }
            try (BufferedReader reader = resource.get().openAsReader()) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                JsonObject animations = GsonHelper.getAsJsonObject(json, "animations");
                Map<String, BedrockAnimation.Clip> out = new LinkedHashMap<>();
                for (String key : animations.keySet()) {
                    out.put(key, BedrockAnimation.parse(key, toMap(GsonHelper.getAsJsonObject(animations, key))));
                }
                return Map.copyOf(out);
            } catch (Exception e) {
                return Map.of();
            }
        });
    }

    /** 清空缓存（资源重载时应当调用）。 */
    public static void invalidateAll() {
        GEOMETRY_CACHE.clear();
        ANIMATION_CACHE.clear();
    }

    /** Gson 的 {@code JsonObject} → 普通 {@code Map}（解析层只依赖 JDK 类型）。 */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(JsonObject json) {
        return GSON.fromJson(json, Map.class);
    }

    /**
     * 把一个实体渲染需要的"模型 + 动画"绑定在一起。
     *
     * <p>这是上层内容模块唯一需要持有的对象：给定 {@code ModelPart} 根与动画状态，
     * 它负责每帧把动画变换写回 part。</p>
     *
     * <p>寻址方式：原版 {@code ModelPart.children} 是私有的，只能用 {@code getChild} 逐级下钻，
     * 所以这里持有建模时得到的"骨骼名 → 父骨骼名"表，按路径解析出每个骨骼对应的 {@code ModelPart}。</p>
     */
    public static final class Binding {

        private final ModelPart root;
        private final Map<String, ModelPart> parts;
        private final GeoAnimationState state;
        private long lastNanoTime;

        /**
         * @param root    {@code ModelPart} 树根
         * @param parents 骨骼名 → 父骨骼名（来自 {@link GeoModelBuilder.Built#parents()}）
         * @param state   动画状态机
         */
        public Binding(ModelPart root, Map<String, String> parents, GeoAnimationState state) {
            this.root = root;
            this.state = state;
            Map<String, ModelPart> resolved = new LinkedHashMap<>();
            for (String bone : parents.keySet()) {
                ModelPart part = resolve(root, parents, bone);
                if (part != null) {
                    resolved.put(bone, part);
                }
            }
            this.parts = Map.copyOf(resolved);
        }

        /** 沿父链自根下钻到指定骨骼。 */
        private static ModelPart resolve(ModelPart root, Map<String, String> parents, String bone) {
            java.util.ArrayDeque<String> chain = new java.util.ArrayDeque<>();
            String cursor = bone;
            while (cursor != null) {
                chain.addFirst(cursor);
                cursor = parents.get(cursor);
                if (chain.size() > 64) {
                    return null; // 防御环
                }
            }
            ModelPart part = root;
            for (String name : chain) {
                if (!part.hasChild(name)) {
                    return null;
                }
                part = part.getChild(name);
            }
            return part;
        }

        public GeoAnimationState state() {
            return this.state;
        }

        /** 模型根 {@code ModelPart}。 */
        public ModelPart root() {
            return this.root;
        }

        public ModelPart part(String boneName) {
            return this.parts.get(boneName);
        }

        /**
         * 按真实时间推进并应用动画。
         *
         * <p>用纳秒时间差而不是 tick 计数：原版渲染帧率与 tick 率不同，
         * 用 tick 会让高帧率下动画"卡住"。</p>
         */
        public void update() {
            long now = System.nanoTime();
            double delta = this.lastNanoTime == 0 ? 0 : (now - this.lastNanoTime) / 1_000_000_000.0;
            this.lastNanoTime = now;
            // 掉帧保护：单帧最多推进 0.1 秒，避免切回窗口时动画瞬移
            if (delta > 0.1) {
                delta = 0.1;
            }
            this.state.tick(delta);
            applyToParts();
        }

        private void applyToParts() {
            // 先复位到静止姿态；resetPose 会同时清掉上一帧的旋转/位移/缩放
            for (ModelPart part : this.parts.values()) {
                part.resetPose();
            }
            this.state.apply((bone, rotation, position, scale, override) -> {
                ModelPart part = this.parts.get(bone);
                if (part == null) {
                    return;
                }
                // Bedrock 的 rotation 是相对静止姿态的偏移，单位度
                part.xRot += (float) Math.toRadians(rotation[0]);
                part.yRot += (float) Math.toRadians(rotation[1]);
                part.zRot += (float) Math.toRadians(rotation[2]);
                // position 单位是像素，原版 ModelPart 的 x/y/z 也是像素
                part.x += position[0];
                part.y += position[1];
                part.z += position[2];
                part.xScale *= scale[0];
                part.yScale *= scale[1];
                part.zScale *= scale[2];
            });
        }
    }
}
