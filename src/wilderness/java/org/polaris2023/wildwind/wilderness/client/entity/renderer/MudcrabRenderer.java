package org.polaris2023.wildwind.wilderness.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.client.ImpRig;
import org.polaris2023.wildwind.wilderness.client.entity.model.MudcrabModel;
import org.polaris2023.wildwind.wilderness.client.entity.renderstate.MudcrabRenderState;
import org.polaris2023.wildwind.wilderness.entity.animal.Mudcrab;
import org.polaris2023.wildwind.wilderness.entity.animal.MudcrabVariant;

/**
 * 泥沼蟹渲染器喵~——<b>纯原版渲染管线，零 GeckoLib</b>。
 *
 * <p>继承 {@link ImpRig.Renderer}（它再继承原版的 {@code MobRenderer}），版本差异全部在 imp 里
 * 分段处理。本类只做三件事：指定模型与主纹理、交出渲染状态工厂、用 imp 的三个钩子实现
 * 「变种纹理 / 幼年体型 / 攀爬倾斜」。</p>
 *
 * <h2>钩子对应关系（迁移前是 GeckoLib 的覆写）</h2>
 * <table border="1">
 *   <caption>能力与钩子</caption>
 *   <tr><th>能力</th><th>GeckoLib 时代</th><th>现在</th></tr>
 *   <tr>
 *     <td>变种纹理（temperate / warm / cold）</td>
 *     <td>{@code getTextureResource} 覆写</td>
 *     <td>{@link #textureFor} → imp 接进两族的 {@code getTextureLocation}</td>
 *   </tr>
 *   <tr>
 *     <td>幼年体型 ×0.6</td>
 *     <td>{@code scaleModelForRender}</td>
 *     <td>{@link #scaleFor} → 26.x 的 {@code submit} / 旧族的 {@code scale}</td>
 *   </tr>
 *   <tr>
 *     <td>攀爬时贴墙倾斜</td>
 *     <td>{@code adjustRenderPose}</td>
 *     <td>{@link #rotationFor} → 26.x 的 {@code submit} / 旧族的 {@code setupRotations}</td>
 *   </tr>
 * </table>
 *
 * <p><b>三个钩子的参数统一是实体</b>，不是渲染状态：这样同一份覆写在两个族都成立，
 * 内容模块不需要写任何版本注解。26.x 的 {@code ImpRig.Renderer} 会先从渲染状态里取出
 * 实体（{@code extractRenderState} 已填入）再传给钩子。</p>
 *
 * <h2>26.x 的 {@code submit} 架构</h2>
 * <p>渲染分两阶段（{@code extractRenderState} → {@code submit}）。原版
 * {@code LivingEntityRenderer#submit} 负责姿态、{@code state.scale}、{@code -1,-1,1} 翻转、
 * {@code -1.501} 基准位移、受伤泛红、光照、阴影与名牌；模型经
 * {@code SubmitNodeCollector#submitModel} 提交，{@code ModelFeatureRenderer} 在绘制阶段调用
 * {@code setupAnim(state)}（推进 rig）与 {@code renderToBuffer(...)}，二者在同一循环体内配对。</p>
 *
 * <h2>动画语义（与迁移前的 GeckoLib 版本逐条对应）</h2>
 * <ul>
 *   <li>{@code Move}（优先级 0）：水里 {@code move.swim}；否则移动或攀爬 {@code move.walk}；
 *       否则 {@code misc.idle}；</li>
 *   <li>{@code Hurt}（优先级 1，盖过 {@code Move}）：存活且 {@code hurtTime > 0} 时
 *       {@code misc.hurt}；</li>
 *   <li>{@code Attack}（一次性）：{@code triggerAnimation("Attack", "attack")} → {@code attack.swing}；</li>
 *   <li>{@code Greeting}（一次性）：{@code triggerAnimation("Greeting", "greeting")} → {@code misc.greeting}。</li>
 * </ul>
 * <p>这些规则登记在实体侧（{@code Mudcrab#registerAnimations}），本渲染器不重复实现。</p>
 *
 * @author Mon-Landis
 * @author safe_saffawa
 * @see MudcrabModel
 * @see ImpRig.Renderer
 */
public class MudcrabRenderer extends ImpRig.Renderer<Mudcrab, MudcrabRenderState> {

    /** 阴影半径（泥沼蟹个头不大）。 */
    private static final float SHADOW_RADIUS = 0.5F;

    /** 幼年个体的体型倍率。 */
    private static final float BABY_SCALE = 0.6F;

    public MudcrabRenderer(EntityRendererProvider.Context context) {
        super(context,
            MudcrabModel.MODEL,
            MudcrabModel.TEXTURE,
            SHADOW_RADIUS,
            MudcrabRenderState::new);
    }

    /** 按变种的注册键选纹理；拿不到变种时退回默认（温带）。 */
    @Override
    protected ImpId textureFor(Mudcrab crab) {
        if (crab == null) {
            return MudcrabModel.TEXTURE;
        }
        var variantKey = crab.getVariant().unwrapKey().orElse(null);
        if (variantKey == null) {
            return MudcrabModel.TEXTURE;
        }
        if (variantKey.equals(MudcrabVariant.WARM)) {
            return MudcrabModel.TEXTURE_WARM;
        }
        if (variantKey.equals(MudcrabVariant.COLD)) {
            return MudcrabModel.TEXTURE_COLD;
        }
        return MudcrabModel.TEXTURE;
    }

    /** 幼年体型。 */
    @Override
    protected float scaleFor(Mudcrab crab) {
        return crab != null && crab.isBaby() ? BABY_SCALE : 1.0F;
    }

    /**
     * 攀爬时的贴墙倾斜。
     *
     * <p>语义与迁移前的 {@code adjustRenderPose} 一致：读实体的
     * {@code clientSideModelYRotOffset} / {@code clientSideModelXRotOffset}
     * （这两个字段由实体自己按 tick 缓动），换算成弧度的 Y/X 旋转写进 {@code out}。</p>
     */
    @Override
    protected void rotationFor(Mudcrab crab, float[] out) {
        if (crab == null) {
            return;
        }
        float yRotDegrees = crab.clientSideModelYRotOffset;
        float xRotDegrees = crab.clientSideModelXRotOffset;
        if (yRotDegrees == 0F && xRotDegrees == 0F) {
            return;
        }
        out[0] = (float) Math.toRadians(yRotDegrees);
        out[1] = (float) Math.toRadians(xRotDegrees);
    }
}
