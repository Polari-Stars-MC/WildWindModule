package org.polaris2023.wildwind.core.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.polaris2023.wildwind.core.client.entity.model.SpiderlingModel;
import org.polaris2023.wildwind.core.client.entity.renderer.layer.SpiderlingEyesLayer;
import org.polaris2023.wildwind.core.client.entity.renderstate.SpiderlingRenderState;
import org.polaris2023.wildwind.core.entity.monster.Spiderling;
import org.polaris2023.wildwind.imp.client.ImpRig;

/**
 * 幼蛛实体渲染器喵~——<b>纯原版渲染管线，零 GeckoLib</b>。
 *
 * <p>继承 {@link ImpRig.Renderer}（它再继承原版的 {@code MobRenderer}），版本差异全部在 imp 里
 * 分段处理，本类只做三件事：</p>
 * <ol>
 *   <li>指定模型 id 与主纹理（{@link SpiderlingModel#MODEL} / {@link SpiderlingModel#TEXTURE}）；</li>
 *   <li>把渲染状态工厂交给 imp（{@code SpiderlingRenderState::new}）；</li>
 *   <li>挂上发光眼睛图层（{@link SpiderlingEyesLayer#TEXTURE}）。</li>
 * </ol>
 *
 * <h2>26.x 的 {@code submit} 架构走到了哪一步：完整渲染，不是占位</h2>
 * <p>26.x 的渲染是两阶段的（{@code extractRenderState} → {@code submit}）。这里<b>没有</b>做任何
 * 简化或降级：</p>
 * <ul>
 *   <li>{@code ImpRig.Renderer#extractRenderState} 把实体引用填进 {@link SpiderlingRenderState}；</li>
 *   <li>原版 {@code LivingEntityRenderer#submit} 负责姿态、{@code state.scale} 缩放、
 *       {@code -1,-1,1} 翻转、{@code -1.501} 基准位移、受伤泛红、光照、阴影与名牌；</li>
 *   <li>模型通过 {@code SubmitNodeCollector#submitModel} 提交，{@code ModelFeatureRenderer}
 *       在绘制阶段调用 {@code SpiderlingModel.setupAnim(state)}（推进 rig）与
 *       {@code renderToBuffer(...)}（画 {@code ModelPart} 树），二者在同一循环体内配对执行；</li>
 *   <li>发光眼睛用 {@code order(1).submitModel(...)} 提交整模型（原因见
 *       {@code ImpRig.Renderer#withGlowLayer} 的注释）。</li>
 * </ul>
 *
 * <h2>1.20.6 / 1.21.1 怎么实现</h2>
 * <p>旧族是单阶段的：原版 {@code LivingEntityRenderer#render} 先
 * {@code model.setupAnim(spiderling, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch)}
 * 再 {@code model.renderToBuffer(...)}，随后遍历图层。实体与摆幅在 {@code setupAnim} 里直接可用，
 * 因此旧族<b>不需要</b>渲染状态——{@link SpiderlingRenderState} 在那两族是占位类，
 * 传入的状态工厂也不会被调用。发光图层的旧族实现（{@code RenderLayer#render} +
 * {@code ModelPart#render}）同样在 imp 里。</p>
 *
 * <h2>动画语义（与迁移前的 GeckoLib 版本逐条对应）</h2>
 * <ul>
 *   <li>{@code Move}（优先级 2）：移动时循环 {@code move.walk}，静止则不播；</li>
 *   <li>{@code Attack}（一次性）：控制器常驻不循环，由 {@code triggerAnimation("Attack", "attack")}
 *       插入 {@code attack.swing}。</li>
 * </ul>
 * <p>这些规则登记在实体侧（{@code Spiderling#registerAnimations}），本渲染器不重复实现。</p>
 *
 * @author safe_saffawa
 * @see SpiderlingEyesLayer
 * @see ImpRig.Renderer
 */
public class SpiderlingRenderer extends ImpRig.Renderer<Spiderling, SpiderlingRenderState> {

    /** 阴影半径（幼蛛比原版蜘蛛小一号）。 */
    private static final float SHADOW_RADIUS = 0.5F;

    public SpiderlingRenderer(EntityRendererProvider.Context context) {
        super(context,
            SpiderlingModel.MODEL,
            SpiderlingModel.TEXTURE,
            SHADOW_RADIUS,
            SpiderlingRenderState::new);
        this.withGlowLayer(SpiderlingEyesLayer.TEXTURE);
    }
}
