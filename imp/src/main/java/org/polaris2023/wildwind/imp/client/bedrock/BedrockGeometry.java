package org.polaris2023.wildwind.imp.client.bedrock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bedrock {@code .geo.json} 几何的解析结果（纯数据，不依赖 MC 类型）。
 *
 * <p>把 Blockbench 导出的几何描述成"骨骼树 + 每根骨骼的立方体"，再由
 * {@code GeoModelBuilder} 翻译成原版 {@code ModelPart} 层级。
 * 这样解析与建模分开，解析部分在所有版本上完全相同。</p>
 *
 * <p>坐标系提醒：Bedrock 的 {@code pivot} / {@code origin} 单位是<b>像素</b>，
 * 且 y 轴向上、z 轴朝南，与原版 {@code ModelPart} 一致（原版也是像素单位）。
 * 唯一需要注意的换算：Bedrock 的旋转角是<b>度</b>，原版 {@code ModelPart} 用弧度。</p>
 */
public final class BedrockGeometry {

    /** 整份几何。 */
    public record Geometry(String identifier, int textureWidth, int textureHeight,
                           double visibleBoundsWidth, double visibleBoundsHeight,
                           float[] visibleBoundsOffset, List<Bone> bones) {

        /** 按名字索引骨骼。 */
        public Map<String, Bone> byName() {
            Map<String, Bone> map = new LinkedHashMap<>();
            for (Bone bone : bones) {
                map.put(bone.name(), bone);
            }
            return Map.copyOf(map);
        }

        /** 没有任何父骨骼的根骨骼名。 */
        public List<String> roots() {
            List<String> roots = new ArrayList<>();
            for (Bone bone : bones) {
                if (bone.parent() == null) {
                    roots.add(bone.name());
                }
            }
            return List.copyOf(roots);
        }
    }

    /**
     * 一根骨骼。
     *
     * @param name    骨骼名
     * @param parent  父骨骼名；{@code null} 表示根
     * @param pivot   旋转枢轴（像素）
     * @param rotation 静止姿态的旋转偏移（度）
     * @param cubes   该骨骼下的立方体
     */
    public record Bone(String name, String parent, float[] pivot, float[] rotation, List<Cube> cubes) {}

    /**
     * 一个立方体。
     *
     * @param origin 最小角坐标（像素）
     * @param size   三边尺寸（像素）
     * @param uv     贴图左上角（像素）；{@code null} 表示未指定
     * @param uvSize 贴图覆盖的尺寸；{@code null} 表示按 {@code size} 自动推断
     * @param inflate 外扩量
     * @param mirror  是否镜像
     */
    public record Cube(float[] origin, float[] size, float[] uv, float[] uvSize,
                       float inflate, boolean mirror) {}

    private BedrockGeometry() {}

    /**
     * 从已解析的 JSON 树构建几何。
     *
     * <p>兼容两种外层形态：{@code {"minecraft:geometry": [...]}} 与单对象。</p>
     */
    @SuppressWarnings("unchecked")
    public static Geometry parse(Map<String, Object> root) {
        Object geometryNode = root.get("minecraft:geometry");
        Map<String, Object> geometry;
        if (geometryNode instanceof List<?> list && !list.isEmpty()) {
            geometry = (Map<String, Object>) list.get(0);
        } else if (geometryNode instanceof Map<?, ?> map) {
            geometry = (Map<String, Object>) map;
        } else {
            geometry = root;
        }

        Map<String, Object> description = geometry.get("description") instanceof Map<?, ?> d
            ? (Map<String, Object>) d
            : Map.of();

        String identifier = description.get("identifier") instanceof String s ? s : "geometry.unknown";
        int texW = description.get("texture_width") instanceof Number n ? n.intValue() : 64;
        int texH = description.get("texture_height") instanceof Number n ? n.intValue() : 64;
        double vbW = description.get("visible_bounds_width") instanceof Number n ? n.doubleValue() : 0;
        double vbH = description.get("visible_bounds_height") instanceof Number n ? n.doubleValue() : 0;
        float[] vbOffset = description.get("visible_bounds_offset") instanceof List<?>
            ? toVec3((List<?>) description.get("visible_bounds_offset"), 0F)
            : new float[] {0F, 0F, 0F};

        List<Bone> bones = new ArrayList<>();
        if (geometry.get("bones") instanceof List<?> boneList) {
            for (Object rawBone : boneList) {
                if (!(rawBone instanceof Map<?, ?> bm)) {
                    continue;
                }
                Map<String, Object> boneMap = (Map<String, Object>) bm;
                String name = boneMap.get("name") instanceof String s ? s : "bone";
                String parent = boneMap.get("parent") instanceof String s ? s : null;
                float[] pivot = boneMap.get("pivot") instanceof List<?> l
                    ? toVec3(l, 0F) : new float[] {0F, 0F, 0F};
                float[] rotation = boneMap.get("rotation") instanceof List<?> r
                    ? toVec3(r, 0F) : new float[] {0F, 0F, 0F};

                List<Cube> cubes = new ArrayList<>();
                if (boneMap.get("cubes") instanceof List<?> cubeList) {
                    for (Object rawCube : cubeList) {
                        if (rawCube instanceof Map<?, ?> cm) {
                            cubes.add(parseCube((Map<String, Object>) cm));
                        }
                    }
                }
                bones.add(new Bone(name, parent, pivot, rotation, List.copyOf(cubes)));
            }
        }

        return new Geometry(identifier, texW, texH, vbW, vbH, vbOffset, List.copyOf(bones));
    }

    @SuppressWarnings("unchecked")
    private static Cube parseCube(Map<String, Object> raw) {
        float[] origin = raw.get("origin") instanceof List<?> o ? toVec3(o, 0F) : new float[] {0F, 0F, 0F};
        float[] size = raw.get("size") instanceof List<?> s ? toVec3(s, 0F) : new float[] {1F, 1F, 1F};

        float[] uv = null;
        if (raw.get("uv") instanceof List<?> uvList) {
            uv = toVec3(uvList, 0F);
        }
        float[] uvSize = null;
        if (raw.get("uv_size") instanceof List<?> uvSizeList) {
            uvSize = toVec3(uvSizeList, 0F);
        }

        float inflate = raw.get("inflate") instanceof Number n ? n.floatValue() : 0F;
        boolean mirror = Boolean.TRUE.equals(raw.get("mirror"));

        return new Cube(origin, size, uv, uvSize, inflate, mirror);
    }

    private static float[] toVec3(List<?> list, float fallback) {
        float[] out = new float[] {fallback, fallback, fallback};
        for (int i = 0; i < 3 && i < list.size(); i++) {
            if (list.get(i) instanceof Number n) {
                out[i] = n.floatValue();
            }
        }
        return out;
    }
}
