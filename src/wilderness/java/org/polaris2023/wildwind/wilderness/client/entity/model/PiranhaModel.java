package org.polaris2023.wildwind.wilderness.client.entity.model;

import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.client.ImpGeoRig;
import org.polaris2023.wildwind.imp.client.ImpRig;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;
import org.polaris2023.wildwind.wilderness.client.entity.renderstate.PiranhaRenderState;
import org.polaris2023.wildwind.wilderness.entity.animal.Piranha;

/**
 * 恐鱼的几何模型（Bedrock 资源 + 原版 {@code ModelPart} 骨架）喵~
 *
 * <p><b>这条线已经完全不再使用 GeckoLib。</b>模型继承的是原版 {@code EntityModel}
 * （经 {@link ImpRig.Model} 收敛版本差异），骨架来自 {@link ImpGeoRig}，
 * 动画来自 {@code GeoResources.Binding}。</p>
 *
 * <p>资源路径（与 Blockbench 导出保持一致，无需转换）：</p>
 * <ul>
 *   <li>几何：{@code assets/ww_wilderness/geckolib/models/entity/piranha.geo.json}</li>
 *   <li>动画：{@code assets/ww_wilderness/geckolib/animations/entity/piranha.animation.json}
 *       （{@code move.swim} / {@code misc.idle} 循环、{@code attack.swing} 一次性）</li>
 *   <li>纹理：{@code assets/ww_wilderness/textures/entity/piranha.png}</li>
 * </ul>
 *
 * <p><b>本类只声明"用哪个模型、哪张纹理"。</b>渲染器走
 * {@link ImpRig.Renderer} 的 {@code (context, modelId, texture, shadow, stateFactory)}
 * 构造器——那一条路径由 imp 内部烘焙 rig 并建 {@code ImpRig.Model}，因此本类在运行时
 * <b>不会</b>被实例化，它的作用与 {@code SpiderlingModel} 完全一致：把资源 id 收成常量，
 * 让"模型/动画/纹理在哪"这件事只出现在一个地方。动画的选路与推进在
 * {@link ImpRig.Driver} 里（规则由实体侧的 {@code Piranha#registerAnimations} 提供）。</p>
 *
 * @author mdjfnfn
 * @author safe_saffawa
 * @see org.polaris2023.wildwind.wilderness.client.entity.renderer.PiranhaRenderer
 */
public final class PiranhaModel extends ImpRig.Model<Piranha, PiranhaRenderState> {

    /** 模型 / 动画的资源 id（{@code ImpGeoRig} 会补全 {@code geckolib/...} 前缀）。 */
    public static final ImpId MODEL = WildWindWilderness.id("entity/piranha");

    /** 主纹理。 */
    public static final ImpId TEXTURE = WildWindWilderness.id("textures/entity/piranha.png");

    /**
     * @param rig 已烘焙的骨架（由 {@link ImpRig#loadRig} 从资源包读出）
     */
    public PiranhaModel(ImpGeoRig.Rig rig) {
        super(rig, TEXTURE);
    }
}
