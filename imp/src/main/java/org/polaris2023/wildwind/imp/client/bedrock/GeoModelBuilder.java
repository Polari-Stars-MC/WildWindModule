package org.polaris2023.wildwind.imp.client.bedrock;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 把 {@link BedrockGeometry} 翻译成原版 {@code ModelPart} 层级。
 *
 * <p>这是 GeckoLib 替换工程里唯一与 MC 打交道的"建模"环节。之所以能写得版本无关，
 * 是因为 {@code MeshDefinition} / {@code PartDefinition} / {@code CubeListBuilder} /
 * {@code PartPose} / {@code LayerDefinition} 这套 API 在 1.20.6、1.21.1、26.2 上<b>完全相同</b>
 * （已用 javap 逐类核对），因此不需要 {@code @Since}/{@code @Until} 分段。</p>
 *
 * <h2>两个坐标系的关键差异</h2>
 * <ul>
 *   <li><b>立方体坐标</b>：Bedrock 的 {@code origin} 是立方体最小角的<b>绝对像素坐标</b>；
 *       原版 {@code addBox(x, y, z, w, h, d)} 的 {@code x/y/z} 是<b>相对该骨骼 pivot</b> 的角点偏移。
 *       所以这里要减掉 pivot。</li>
 *   <li><b>y 轴</b>：原版 {@code addBox} 的 y 是<b>向下为正</b>（模型空间里 y 向上，但建模 API
 *       以"从 pivot 往下长"的方式描述），而 Bedrock 的 origin 是<b>向上为正</b>的最小角。
 *       换算为 {@code y = pivotY - (originY + sizeY)}。</li>
 *   <li><b>旋转</b>：Bedrock 用度，原版 {@code PartPose.rotation} 用弧度。</li>
 * </ul>
 */
public final class GeoModelBuilder {

    private GeoModelBuilder() {}

    /**
     * 构建 {@code LayerDefinition}（可直接喂给 {@code EntityModelSet#bakeLayer}）。
     */
    public static LayerDefinition layer(BedrockGeometry.Geometry geometry) {
        return build(geometry).layer();
    }

    /** 直接烘出根 {@code ModelPart}。 */
    public static ModelPart bake(BedrockGeometry.Geometry geometry) {
        return build(geometry).layer().bakeRoot();
    }

    /**
     * 建模结果：{@code LayerDefinition} + "骨骼名 → 父骨骼名" 映射。
     *
     * <p>原版 {@code ModelPart.children} 是私有的，只能靠 {@code getChild} 逐级下钻，
     * 因此这里把父子关系一并带出来，供 {@link GeoResources.Binding} 建立寻址路径。</p>
     */
    public record Built(LayerDefinition layer, Map<String, String> parents) {}

    /** 构建并返回建模结果。 */
    public static Built build(BedrockGeometry.Geometry geometry) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        Map<String, BedrockGeometry.Bone> byName = geometry.byName();
        Map<String, PartDefinition> built = new LinkedHashMap<>();
        Map<String, String> parents = new LinkedHashMap<>();

        // 先建所有骨骼（不含立方体），再挂父子关系：geo 里父骨骼不一定排在子骨骼前面
        for (BedrockGeometry.Bone bone : geometry.bones()) {
            PartDefinition part = root.addOrReplaceChild(bone.name(), CubeListBuilder.create(), pose(bone));
            built.put(bone.name(), part);
            parents.put(bone.name(), bone.parent());
        }

        for (BedrockGeometry.Bone bone : geometry.bones()) {
            PartDefinition part = built.get(bone.name());
            if (part == null) {
                continue;
            }
            CubeListBuilder cubes = cubes(bone);
            if (cubes != null) {
                // addOrReplaceChild 会替换掉占位的空定义，从而保留同一 part 并补上立方体
                PartDefinition parent = bone.parent() == null ? root : built.get(bone.parent());
                if (parent != null) {
                    parent.addOrReplaceChild(bone.name(), cubes, pose(bone));
                }
            }
        }

        LayerDefinition layer = LayerDefinition.create(mesh, geometry.textureWidth(), geometry.textureHeight());
        return new Built(layer, Map.copyOf(parents));
    }

    /** 把骨骼的 pivot / 静止旋转转成 {@link PartPose}。 */
    private static PartPose pose(BedrockGeometry.Bone bone) {
        float[] pivot = bone.pivot();
        float[] rot = bone.rotation();
        return PartPose.offsetAndRotation(pivot[0], pivot[1], pivot[2],
            (float) Math.toRadians(rot[0]),
            (float) Math.toRadians(rot[1]),
            (float) Math.toRadians(rot[2]));
    }

    /** 把一根骨骼下的所有立方体加成 {@link CubeListBuilder}；没有立方体时返回 {@code null}。 */
    private static CubeListBuilder cubes(BedrockGeometry.Bone bone) {
        if (bone.cubes().isEmpty()) {
            return null;
        }
        CubeListBuilder builder = CubeListBuilder.create();
        float[] pivot = bone.pivot();

        for (BedrockGeometry.Cube cube : bone.cubes()) {
            float[] origin = cube.origin();
            float[] size = cube.size();

            // Bedrock origin（最小角、y 向上）→ 原版 addBox（相对 pivot、y 向下）
            float x = origin[0] - pivot[0];
            float y = pivot[1] - (origin[1] + size[1]);
            float z = origin[2] - pivot[2];

            if (cube.uv() != null) {
                builder.texOffs((int) cube.uv()[0], (int) cube.uv()[1]);
            }
            if (cube.mirror()) {
                builder.mirror();
            }
            builder.addBox(x, y, z, size[0], size[1], size[2], cube.inflate() == 0F ? CubeDeformationNone.VALUE : new net.minecraft.client.model.geom.builders.CubeDeformation(cube.inflate()));
        }
        return builder;
    }

    /**
     * {@code CubeDeformation} 的零值持有者。
     *
     * <p>原版 {@code CubeDeformation.NONE} 是 public 的，但为避免在某些映射下不可见，
     * 这里显式构造一个 0 膨胀实例。</p>
     */
    private static final class CubeDeformationNone {
        static final net.minecraft.client.model.geom.builders.CubeDeformation VALUE =
            new net.minecraft.client.model.geom.builders.CubeDeformation(0F);

        private CubeDeformationNone() {}
    }
}
