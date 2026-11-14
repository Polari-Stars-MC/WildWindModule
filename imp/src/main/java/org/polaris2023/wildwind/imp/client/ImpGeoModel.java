package org.polaris2023.wildwind.imp.client;

import org.polaris2023.wildwind.imp.ImpId;

/**
 * 内容模块的「模型类」基类——把 {@code ImpGeoRig.Rig} 接进原版 {@code EntityModel} 体系。
 *
 * <p>为什么必须有这一层：原版渲染器强制要求模型是 {@code EntityModel} 的子类
 * （它提供 {@code setupAnim} 与 {@code renderToBuffer} 两个契约），而 {@code EntityModel}
 * 的泛型元数与构造器签名在三个版本族里都不一样（见 {@link ImpEntityRenderers} 的类注释表格）。
 * 内容模块不该、也不可能只写一份，所以差异全部收进
 * {@link ImpEntityRenderers.ModelBase}，本类只做"三族同形"的那部分。</p>
 *
 * <h2>内容模块要写的东西</h2>
 * <p>正常情况下<b>一行都不用写</b>：{@link ImpEntityRenderers.Renderer} 的
 * {@code (context, modelId, shadow)} 构造器会自己烘焙 rig 并建一个本类的实例。
 * 只有需要覆写钩子（{@link #onAnimate}）时，才像下面这样派生：</p>
 *
 * <pre>{@code
 * public final class SpiderlingModel extends ImpGeoModel<Spiderling> {
 *     private static final ImpId MODEL = WildWindCore.id("entity/spiderling");
 *
 *     public SpiderlingModel(ImpGeoRig.Rig rig) {
 *         super(rig, ImpEntityRenderers.defaultTexture(MODEL));
 *     }
 * }
 * }</pre>
 *
 * <h2>26.x 与旧版的分工差异（重要）</h2>
 * <ul>
 *   <li><b>1.20.6 / 1.21.1</b>：渲染是<b>单阶段</b>的。{@code LivingEntityRenderer#render}
 *       先调 {@code model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch)}
 *       再 {@code model.renderToBuffer(...)}，实体对象在 {@code setupAnim} 里直接可用。
 *       本类忽略那 5 个原版参数——Bedrock 动画由 {@code GeoResources.Binding} 按
 *       <b>真实时间</b>推进（{@code System.nanoTime()} 差分），不需要 tick 驱动的摆幅。</li>
 *   <li><b>26.x</b>：渲染是<b>两阶段</b>的（{@code extractRenderState} → {@code submit}）。
 *       {@code setupAnim} 收到的不是实体而是 {@code LivingEntityRenderState}；
 *       实体要从 {@link ImpEntityRenderers.State#entity} 取（由渲染器在
 *       {@code extractRenderState} 阶段填入）。本类在 26.x 分支里自动完成这次取用，
 *       所以 {@link #onAnimate} 的签名三族一致。</li>
 * </ul>
 *
 * <p>另一处 26.x 专属约束：{@code Model(ModelPart, Function<Identifier,RenderType>)} 会
 * 在<b>构造期</b>缓存 {@code root.getAllParts()}，因此 rig 的根 {@code ModelPart} 必须在
 * {@code super()} 时就绪——本类的构造器直接把 {@code rig.root()} 交上去，
 * 这也是它必须收 {@code rig} 而不是 lazy 加载的原因。</p>
 *
 * @param <T> 实体类型（三族同一含义；26.x 只在内部用于把状态转回实体）
 * @author safe_saffawa
 * @see ImpEntityRenderers.Renderer
 */
public class ImpGeoModel<T extends net.minecraft.world.entity.LivingEntity>
    extends ImpEntityRenderers.ModelBase<T> {

    /**
     * 动画回调。
     *
     * <p>形参用 {@code Entity} 而不是 {@code T}：回调由渲染器注册、由基类在
     * {@code setupAnim} 里调用，两个位置的泛型上下文不同（26.x 的 {@code setupAnim}
     * 根本看不到 {@code T}）。带类型的版本见 {@link ImpEntityRenderers.Renderer#onAnimate}。</p>
     */
    @FunctionalInterface
    public interface Animator {
        void animate(net.minecraft.world.entity.Entity entity, float ageInTicks);
    }

    private final ImpGeoRig.Rig rig;
    private Animator animator;

    /**
     * @param rig     已烘焙的骨架（由 {@link ImpEntityRenderers#loadRig} 得到）
     * @param texture 主纹理 id
     */
    public ImpGeoModel(ImpGeoRig.Rig rig, ImpId texture) {
        // 注意：26.x 的 Model 构造器会在此时缓存 root.getAllParts()，
        // 所以必须在 super() 阶段就把 rig 的根 part 交出去，不能延迟到首次 setupAnim。
        super(rootOf(rig), texture);
        this.rig = rig;
    }

    /** 取 rig 的根 {@code ModelPart}（{@code super()} 里不能引用实例字段）。 */
    private static net.minecraft.client.model.geom.ModelPart rootOf(ImpGeoRig.Rig rig) {
        return rig.root();
    }

    /**
     * 骨架门面：注册动画、设循环动画、触发一次性动画都从这里走。
     *
     * <p>内容模块通常在 {@link ImpEntityRenderers.Renderer#onAnimate} 里用它，
     * 也可以在实体侧用 {@code ImpGeoRig.Rig#trigger} 联动。</p>
     */
    public final ImpGeoRig.Rig rig() {
        return this.rig;
    }

    /**
     * 注册每帧动画回调（由渲染器在构造末尾调用）。
     *
     * <p>用 setter 而不是构造器参数：{@link ImpEntityRenderers.Renderer} 需要
     * {@code this} 才能建回调，而 {@code this} 在 {@code super()} 里不可用。</p>
     */
    public final void setAnimator(Animator animator) {
        this.animator = animator;
    }

    /**
     * 每帧推进动画。子类覆写它做额外动作时<b>必须</b>调 {@code super}，
     * 否则 {@code rig.update()} 不会执行、模型会静止在初始姿态。
     *
     * @param entity     本帧被渲染的实体；26.x 下由 {@link ImpEntityRenderers.State#entity} 提供，
     *                   若模型被别的渲染器复用则可能为 {@code null}
     * @param ageInTicks 含 partialTick 的年龄（三族语义一致）
     */
    @Override
    protected void onSetupAnim(net.minecraft.world.entity.Entity entity, float ageInTicks) {
        // 动画推进与实体无关：只按真实时间前进一步并写回 ModelPart
        this.rig.update();
        if (this.animator != null) {
            this.animator.animate(entity, ageInTicks);
        }
    }
}
