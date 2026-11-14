package org.polaris2023.wildwind.imp.client.bedrock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bedrock 动画的解析与关键帧求值（不依赖 GeckoLib，也不依赖任何版本分叉 API）。
 *
 * <p>数据结构直接对齐 Blockbench 导出的 {@code .animation.json}：</p>
 * <pre>{@code
 * "animations": {
 *   "move.walk": {
 *     "animation_length": 1.0,
 *     "loop": true,
 *     "bones": {
 *       "head": { "rotation": { "0.0": { "post": { "vector": [0,0,0] } }, ... } }
 *     }
 *   }
 * }
 * }</pre>
 *
 * <p>关键帧语义与 Bedrock 一致：</p>
 * <ul>
 *   <li>时间单位是<b>秒</b>，键名可能不按数值排序（如 {@code "0.5"} 与 {@code "0.3333"}），
 *       解析时按 double 排序；</li>
 *   <li>{@code rotation} 是<b>角度</b>，且是相对骨骼静止姿态的偏移；</li>
 *   <li>{@code position} 是<b>像素</b>，同样是偏移；</li>
 *   <li>{@code scale} 是倍率，默认 1；</li>
 *   <li>{@code lerp_mode} 为 {@code catmullrom} 时用 Catmull-Rom 样条，缺省为线性。</li>
 * </ul>
 *
 * <p>这里刻意只实现"求值"这一件事，动画选择/混合状态机由 {@link GeoAnimationState} 负责。</p>
 */
public final class BedrockAnimation {

    /** 一条动画（一个 key，如 {@code move.walk}）。 */
    public record Clip(String name, double length, boolean loop, Map<String, BoneTrack> bones) {}

    /** 单个骨骼在某条动画里的三条通道。 */
    public record BoneTrack(Channel rotation, Channel position, Channel scale) {

        public static final BoneTrack EMPTY = new BoneTrack(Channel.EMPTY, Channel.EMPTY, Channel.EMPTY);
    }

    /** 一条通道（某个骨骼的 rotation / position / scale），按时间排序的关键帧序列。 */
    public static final class Channel {

        public static final Channel EMPTY = new Channel(List.of());

        /** 关键帧：时间（秒）、三维值、到下一帧的插值方式。 */
        public record Keyframe(double time, float[] value, boolean catmullRom) {}

        private final List<Keyframe> frames;

        public Channel(List<Keyframe> frames) {
            this.frames = List.copyOf(frames);
        }

        public boolean isEmpty() {
            return frames.isEmpty();
        }

        /**
         * 在指定时间求值。
         *
         * @param seconds   动画内部时间（秒），调用方负责按 loop / clamp 收敛到 [0, length]
         * @param fallback  没有任何关键帧时返回的值（通常是静止姿态偏移，即全 0 或 1）
         */
        public float[] sample(double seconds, float[] fallback) {
            if (frames.isEmpty()) {
                return fallback;
            }
            if (frames.size() == 1 || seconds <= frames.get(0).time()) {
                return frames.get(0).value().clone();
            }
            Keyframe last = frames.get(frames.size() - 1);
            if (seconds >= last.time()) {
                return last.value().clone();
            }

            // 找到 seconds 落在的区间 [a, b]
            int hi = 1;
            while (hi < frames.size() && frames.get(hi).time() <= seconds) {
                hi++;
            }
            Keyframe a = frames.get(hi - 1);
            Keyframe b = frames.get(hi);
            double span = b.time() - a.time();
            if (span <= 1e-9) {
                return b.value().clone();
            }
            double t = (seconds - a.time()) / span;

            if (a.catmullRom()) {
                // Catmull-Rom 需要前后各一个控制点；边界处退化为端点自身
                Keyframe p0 = hi - 2 >= 0 ? frames.get(hi - 2) : a;
                Keyframe p3 = hi + 1 < frames.size() ? frames.get(hi + 1) : b;
                float[] out = new float[3];
                for (int i = 0; i < 3; i++) {
                    out[i] = catmullRom(p0.value()[i], a.value()[i], b.value()[i], p3.value()[i], (float) t);
                }
                return out;
            }

            float[] out = new float[3];
            for (int i = 0; i < 3; i++) {
                out[i] = (float) (a.value()[i] + (b.value()[i] - a.value()[i]) * t);
            }
            return out;
        }

        private static float catmullRom(float p0, float p1, float p2, float p3, float t) {
            float t2 = t * t;
            float t3 = t2 * t;
            return 0.5F * ((2 * p1)
                + (-p0 + p2) * t
                + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t2
                + (-p0 + 3 * p1 - 3 * p2 + p3) * t3);
        }
    }

    private BedrockAnimation() {}

    /**
     * 从已解析的 JSON 树构建一条动画。
     *
     * @param name   动画 key（如 {@code move.walk}）
     * @param raw    该 key 对应的 JSON 对象（{@code Map} 形态）
     */
    @SuppressWarnings("unchecked")
    public static Clip parse(String name, Map<String, Object> raw) {
        double length = raw.get("animation_length") instanceof Number n ? n.doubleValue() : 1.0;
        boolean loop = Boolean.TRUE.equals(raw.get("loop"));

        Map<String, BoneTrack> bones = new LinkedHashMap<>();
        if (raw.get("bones") instanceof Map<?, ?> boneMap) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) boneMap).entrySet()) {
                if (!(entry.getValue() instanceof Map<?, ?> trackRaw)) {
                    continue;
                }
                Map<String, Object> track = (Map<String, Object>) trackRaw;
                bones.put(entry.getKey(), new BoneTrack(
                    parseChannel(track.get("rotation")),
                    parseChannel(track.get("position")),
                    parseChannel(track.get("scale"))
                ));
            }
        }
        return new Clip(name, length <= 0 ? 1.0 : length, loop, Map.copyOf(bones));
    }

    /**
     * 解析一条通道。
     *
     * <p>Bedrock 的写法有三种：直接给 {@code [x,y,z]}、给 {@code {post: {vector: [...]}}}、
     * 或给 {@code {pre: ..., post: ...}}。这里统一取 {@code post}（缺省则取裸向量）。</p>
     */
    @SuppressWarnings("unchecked")
    private static Channel parseChannel(Object raw) {
        if (!(raw instanceof Map<?, ?> timeMap)) {
            return Channel.EMPTY;
        }

        List<Channel.Keyframe> frames = new ArrayList<>();
        for (Map.Entry<String, Object> entry : ((Map<String, Object>) timeMap).entrySet()) {
            double time;
            try {
                time = Double.parseDouble(entry.getKey());
            } catch (NumberFormatException ignored) {
                continue;
            }
            Object body = entry.getValue();
            boolean catmullRom = false;
            float[] vector = null;

            if (body instanceof Map<?, ?> bodyMap) {
                Map<String, Object> m = (Map<String, Object>) bodyMap;
                catmullRom = "catmullrom".equals(m.get("lerp_mode"));
                Object post = m.get("post");
                if (post instanceof Map<?, ?> postMap) {
                    vector = toVector(((Map<String, Object>) postMap).get("vector"));
                } else {
                    vector = toVector(post);
                }
            } else {
                vector = toVector(body);
            }

            if (vector != null) {
                frames.add(new Channel.Keyframe(time, vector, catmullRom));
            }
        }

        frames.sort((a, b) -> Double.compare(a.time(), b.time()));
        return new Channel(frames);
    }

    /** 把 {@code [x,y,z]} 或标量转成 float[3]。 */
    @SuppressWarnings("unchecked")
    private static float[] toVector(Object raw) {
        if (raw instanceof List<?> list) {
            float[] out = new float[] {0F, 0F, 0F};
            for (int i = 0; i < 3 && i < list.size(); i++) {
                if (list.get(i) instanceof Number n) {
                    out[i] = n.floatValue();
                }
            }
            return out;
        }
        if (raw instanceof Number n) {
            float v = n.floatValue();
            return new float[] {v, v, v};
        }
        return null;
    }
}
