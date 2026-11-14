package org.polaris2023.wildwind.wilderness.client.entity.model;

import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.client.ImpGeoRig;
import org.polaris2023.wildwind.imp.client.ImpRig;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;
import org.polaris2023.wildwind.wilderness.client.entity.renderstate.MudcrabRenderState;
import org.polaris2023.wildwind.wilderness.entity.animal.Mudcrab;

/**
 * 泥沼蟹的几何模型（Bedrock 资源 + 原版 {@code ModelPart} 骨架）喵~
 *
 * <p><b>这条线已经完全不再使用 GeckoLib。</b>模型继承的是原版 {@code EntityModel}
 * （经 {@link ImpRig.Model} 收敛版本差异），骨架来自 {@link ImpGeoRig}，
 * 动画来自 {@code GeoResources.Binding}。</p>
 *
 * <p>资源路径（与 Blockbench 导出保持一致，无需转换）：</p>
 * <ul>
 *   <li>几何：{@code assets/ww_wilderness/geckolib/models/entity/mudcrab.geo.json}
 *       （14 骨骼 / 13 立方体）</li>
 *   <li>动画：{@code assets/ww_wilderness/geckolib/animations/entity/mudcrab.animation.json}
 *       （6 条：{@code attack.swing} / {@code move.walk} / {@code move.swim} /
 *       {@code misc.idle} / {@code misc.greeting} / {@code misc.hurt}）</li>
 *   <li>纹理：{@code assets/ww_wilderness/textures/entity/mudcrab/<variant>.png}</li>
 * </ul>
 *
 * <h2>⚠ 变种纹理：能力降级（唯一的功能回退）</h2>
 * <p>迁移前 {@code MudcrabModel#getTextureResource} 按 {@code MudcrabRenderState#variant}
 * 动态返回 {@code entity/mudcrab/{temperate,warm,cold}} 三张纹理之一。</p>
 * <p>现在模型纹理由 {@link ImpRig.Model#texture()} 在<b>构造期</b>固定（该方法是 {@code final}，
 * 且 {@link ImpRig.Renderer#getTextureLocation} 的两个族签名互不相同——
 * 26.x 收渲染状态、1.20.6/1.21.1 收实体——内容模块无法用一份源码覆写）。
 * 因此这里退化为<b>单一纹理</b>：{@code temperate}。</p>
 * <p>恢复方式只有一条：在 imp 的 {@code ImpRig.Renderer} 里加一个
 * {@code protected ImpId textureFor(S state)} 钩子（默认返回 {@code rigModel.texture()}）。
 * 那属于 {@code imp/client} 的改动，本次迁移按约束未做，已作为已知降级上报。</p>
 *
 * @author Mon-Landis
 * @author safe_saffawa
 * @see org.polaris2023.wildwind.wilderness.client.entity.renderer.MudcrabRenderer
 */
public final class MudcrabModel extends ImpRig.Model<Mudcrab, MudcrabRenderState> {

    /** 模型 / 动画的资源 id（{@code ImpGeoRig} 会补全 {@code geckolib/...} 前缀）。 */
    public static final ImpId MODEL = WildWindWilderness.id("entity/mudcrab");

    /**
     * 主纹理。
     *
     * <p>三变种（{@code temperate} / {@code warm} / {@code cold}）现在共用这一张——
     * 原因见类注释的「变种纹理：能力降级」。</p>
     */
    public static final ImpId TEXTURE = WildWindWilderness.id("textures/entity/mudcrab/temperate.png");

    /** 变种 {@code warm} 的纹理（保留常量以便将来恢复按变种选纹理时不必再找路径）。 */
    public static final ImpId TEXTURE_WARM = WildWindWilderness.id("textures/entity/mudcrab/warm.png");

    /** 变种 {@code cold} 的纹理（同上）。 */
    public static final ImpId TEXTURE_COLD = WildWindWilderness.id("textures/entity/mudcrab/cold.png");

    /**
     * @param rig 已烘焙的骨架（由 {@link ImpRig#loadRig} 从资源包读出）
     */
    public MudcrabModel(ImpGeoRig.Rig rig) {
        super(rig, TEXTURE);
    }
}
