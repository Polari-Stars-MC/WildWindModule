package org.polaris2023.wildwind.core.client.entity.renderer.layer;

import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.client.ImpRig;

/**
 * 幼蛛发光眼睛图层喵~——<b>纯原版 {@code RenderLayer}，零 GeckoLib</b>。
 *
 * <p>本类只承担"发光纹理是哪个"这一个版本无关的职责；图层的实例化与两族差异由
 * {@link ImpRig.Renderer#withGlowLayer} 承担。</p>
 *
 * <h2>为什么图层不写在这里</h2>
 * <p>原版 {@code RenderLayer} 的泛型元数与渲染约定在 26.x 与旧版之间完全不同：</p>
 * <ul>
 *   <li><b>26.x</b>：{@code RenderLayer<S extends EntityRenderState, M extends EntityModel<? super S>>}，
 *       立即绘制被换成"提交"——{@code submit(PoseStack, SubmitNodeCollector, int light, S, float yRot,
 *       float xRot)}，随后由 {@code SubmitNodeCollector} 延迟绘制；</li>
 *   <li><b>1.20.6 / 1.21.1</b>：{@code RenderLayer<T extends Entity, M extends EntityModel<T>>}，
 *       即时绘制——{@code render(PoseStack, MultiBufferSource, int light, T, limbSwing, limbSwingAmount,
 *       partialTick, ageInTicks, headYaw, headPitch)}。</li>
 * </ul>
 * <p>连泛型实参的含义都不同（状态 vs 实体），内容模块无法用一个 {@code extends} 同时满足
 * （{@code import} 不能带版本注解，{@code extends} 子句里的类型也不受 {@code @Name} 改写）。
 * 因此图层的实现放进 imp，本类只留纹理常量。</p>
 *
 * <h2>渲染类型</h2>
 * <p>两族都用原版的"发光眼睛"渲染类型（旧族 {@code RenderType.eyes(...)}、26.x
 * {@code RenderTypes.eyes(...)}），它与原版蜘蛛眼睛图层同源，会自动全亮度发光。</p>
 *
 * @author safe_saffawa
 * @see org.polaris2023.wildwind.core.client.entity.renderer.SpiderlingRenderer
 */
public final class SpiderlingEyesLayer {

    /**
     * 发光眼睛纹理。
     *
     * <p>与 {@code SpiderlingModel#GLOW_TEXTURE} 指向同一个文件；这里保留一份是为了让
     * "图层用哪张纹理"这件事只由图层自己声明，渲染器不必知道图层内部细节。</p>
     */
    public static final ImpId TEXTURE = WildWindCore.id("textures/entity/spiderling_eyes.png");

    private SpiderlingEyesLayer() {
    }
}
