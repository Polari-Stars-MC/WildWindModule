package org.polaris2023.wildwind.wilderness.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.polaris2023.wildwind.imp.client.ImpRig;
import org.polaris2023.wildwind.wilderness.client.entity.model.PiranhaModel;
import org.polaris2023.wildwind.wilderness.client.entity.renderstate.PiranhaRenderState;
import org.polaris2023.wildwind.wilderness.entity.animal.Piranha;

/**
 * 恐鱼渲染器喵~——<b>纯原版渲染管线，零 GeckoLib</b>。
 *
 * <p>继承 {@link ImpRig.Renderer}（它再继承原版的 {@code MobRenderer}），版本差异全部在 imp 里
 * 分段处理，本类只做两件事：</p>
 * <ol>
 *   <li>指定模型 id 与主纹理（{@link PiranhaModel#MODEL} / {@link PiranhaModel#TEXTURE}）；</li>
 *   <li>把渲染状态工厂交给 imp（{@code PiranhaRenderState::new}）。</li>
 * </ol>
 *
 * <h2>26.x 的 {@code submit} 架构走到了哪一步：完整渲染，不是占位</h2>
 * <p>26.x 的渲染是两阶段的（{@code extractRenderState} → {@code submit}）。这里<b>没有</b>做任何
 * 简化：</p>
 * <ul>
 *   <li>{@code ImpRig.Renderer#extractRenderState} 把实体引用填进 {@link PiranhaRenderState}；</li>
 *   <li>原版 {@code LivingEntityRenderer#submit} 负责姿态、{@code state.scale} 缩放、
 *       {@code -1,-1,1} 翻转、{@code -1.501} 基准位移、受伤泛红、光照、阴影与名牌；</li>
 *   <li>模型通过 {@code SubmitNodeCollector#submitModel} 提交，{@code ModelFeatureRenderer}
 *       在绘制阶段调用 {@code PiranhaModel.setupAnim(state)}（推进 rig）与
 *       {@code renderToBuffer(...)}（画 {@code ModelPart} 树），二者在同一循环体内配对执行。</li>
 * </ul>
 *
 * <h2>1.20.6 / 1.21.1 怎么实现</h2>
 * <p>旧族是单阶段的：原版 {@code LivingEntityRenderer#render} 先
 * {@code model.setupAnim(piranha, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch)}
 * 再 {@code model.renderToBuffer(...)}。实体与摆幅在 {@code setupAnim} 里直接可用，
 * 因此旧族<b>不需要</b>渲染状态——{@link PiranhaRenderState} 在那两族是占位类。</p>
 *
 * <h2>动画语义（与迁移前的 GeckoLib 版本逐条对应）</h2>
 * <ul>
 *   <li>{@code Move}（优先级 3）：在水里循环 {@code move.swim}，否则循环 {@code misc.idle}；</li>
 *   <li>{@code Attack}（一次性）：由 {@code triggerAnimation("Attack", "attack")} 插入
 *       {@code attack.swing}。</li>
 * </ul>
 * <p>这些规则登记在实体侧（{@code Piranha#registerAnimations}），本渲染器不重复实现。</p>
 *
 * @author huimanman
 * @author mdjfnfn
 * @author safe_saffawa
 * @see ImpRig.Renderer
 */
public class PiranhaRenderer extends ImpRig.Renderer<Piranha, PiranhaRenderState> {

    /**
     * 阴影半径。
     *
     * <p>迁移前 {@code GeoEntityRenderer} 只调 {@code EntityRenderer(context)}，
     * 阴影半径其实是 0（不投影）。这里给一个小值让它有落地感；这是<b>唯一的视觉增量</b>，
     * 不影响动画与模型。</p>
     */
    private static final float SHADOW_RADIUS = 0.3F;

    public PiranhaRenderer(EntityRendererProvider.Context context) {
        super(context,
            PiranhaModel.MODEL,
            PiranhaModel.TEXTURE,
            SHADOW_RADIUS,
            PiranhaRenderState::new);
    }
}
