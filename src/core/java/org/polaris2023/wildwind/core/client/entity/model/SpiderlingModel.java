package org.polaris2023.wildwind.core.client.entity.model;

import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.client.entity.renderstate.SpiderlingRenderState;
import org.polaris2023.wildwind.core.entity.monster.Spiderling;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.client.ImpGeoRig;
import org.polaris2023.wildwind.imp.client.ImpRig;

/**
 * 幼蛛的几何模型（Bedrock 资源 + 原版 {@code ModelPart} 骨架）。
 *
 * <p><b>这条线已经完全不再使用 GeckoLib。</b>模型继承的是原版
 * {@code EntityModel}（经 {@link ImpRig.Model} 收敛版本差异），骨架来自
 * {@link ImpGeoRig}，动画来自 {@code GeoResources.Binding}。</p>
 *
 * <p>资源路径（与 Blockbench 导出保持一致，无需转换）：</p>
 * <ul>
 *   <li>几何：{@code assets/ww_core/geckolib/models/entity/spiderling.geo.json}</li>
 *   <li>动画：{@code assets/ww_core/geckolib/animations/entity/spiderling.animation.json}
 *       （{@code move.walk} 循环 / {@code attack.swing} 一次性）</li>
 *   <li>纹理：{@code assets/ww_core/textures/entity/spiderling.png}</li>
 *   <li>发光：{@code assets/ww_core/textures/entity/spiderling_eyes.png}</li>
 * </ul>
 *
 * <p>本类只负责"我要哪个模型、用哪张纹理"，动画的选路与推进在
 * {@link ImpRig.Driver} 里（由 {@link org.polaris2023.wildwind.core.client.entity.renderer.SpiderlingRenderer}
 * 在构造期建立的 {@code Spiderling} 实体侧 {@code AnimationSet} 提供规则）。</p>
 *
 * <p><b>26.x 与旧版的分工</b>：26.x 的 {@code EntityModel} 泛型参数是渲染状态
 * （{@code SpiderlingRenderState}），{@code setupAnim} 只收到状态，实体从
 * {@code SpiderlingRenderState#entity} 取；1.20.6 / 1.21.1 的泛型参数是实体本身，
 * {@code setupAnim} 直接拿到 {@code Spiderling}。两者都由 {@link ImpRig.Model} 承载。</p>
 *
 * @author safe_saffawa
 * @see org.polaris2023.wildwind.core.client.entity.renderer.SpiderlingRenderer
 */
public final class SpiderlingModel extends ImpRig.Model<Spiderling, SpiderlingRenderState> {

    /** 模型 / 动画的资源 id（{@code ImpGeoRig} 会补全 {@code geckolib/...} 前缀）。 */
    public static final ImpId MODEL = WildWindCore.id("entity/spiderling");

    /** 主纹理。 */
    public static final ImpId TEXTURE = WildWindCore.id("textures/entity/spiderling.png");

    /** 发光眼睛纹理。 */
    public static final ImpId GLOW_TEXTURE = WildWindCore.id("textures/entity/spiderling_eyes.png");

    /**
     * @param rig 已烘焙的骨架（由 {@link ImpRig#loadRig} 从资源包读出）
     */
    public SpiderlingModel(ImpGeoRig.Rig rig) {
        super(rig, TEXTURE);
    }
}
