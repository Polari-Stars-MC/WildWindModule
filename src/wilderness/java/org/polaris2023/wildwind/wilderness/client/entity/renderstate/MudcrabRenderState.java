package org.polaris2023.wildwind.wilderness.client.entity.renderstate;

import org.polaris2023.wildwind.imp.client.ImpRig;
import org.polaris2023.wildwind.wilderness.entity.animal.MudcrabVariant;

/**
 * 泥沼蟹渲染状态喵~
 *
 * <p>本类是 {@link ImpRig.RenderState} 的薄壳，内容模块因此不必直接接触
 * {@code LivingEntityRenderState} 这类版本专属类型。</p>
 *
 * <h2>26.x（本类真正有内容的一族）</h2>
 * <p>26.x 的渲染是两阶段的：{@code extractRenderState} 采集、{@code submit} 绘制。
 * 绘制与 {@code setupAnim(state)} 阶段拿不到实体，而"在水里/在攀爬/是否受伤"必须问实体，
 * 所以 {@link ImpRig.RenderState} 在标准 {@code LivingEntityRenderState} 之上补了一个
 * {@code entity} 引用（由 {@code ImpRig.Renderer#extractRenderState} 填入）。本类只补一个
 * 收窄到泥沼蟹的访问器（{@link #mudcrab()}），不重复声明字段——避免字段隐藏导致读到的值与
 * 基类不一致。</p>
 *
 * <h2>变种字段为什么保留但当前无读者</h2>
 * <p>迁移前渲染器在 {@code extractLivingEntityRenderState} 里把 {@code variant} 填进来，
 * 模型按它选纹理。现在纹理在模型构造期固定（原因见 {@code MudcrabModel} 的类注释），
 * 这个字段<b>暂时没有读者</b>。保留它是为了：一旦 imp 补上按状态选纹理的钩子，
 * 只需接一行、不必再改渲染状态的采集逻辑。</p>
 *
 * <h2>1.20.6 / 1.21.1（旧族没有渲染状态）</h2>
 * <p>旧族的 Minecraft <b>根本没有</b> {@code EntityRenderState} 体系——渲染直接在
 * {@code render(T, ...)} 里完成，{@code setupAnim} 的第一个参数就是实体本身。因此旧族的
 * {@link ImpRig.RenderState} 是<b>空占位类</b>：本类在那两族里不承载任何数据，存在的唯一目的
 * 是让内容模块的源码在三个族里逐字相同。</p>
 *
 * <p>⚠ 因此<b>不要</b>往本类里塞字段并指望旧族能读到：旧族渲染路径上没有读取者。
 * 旧族需要渲染期数据时，正确做法是在实体上暴露只读访问器。</p>
 *
 * @author Mon-Landis
 * @author safe_saffawa
 * @see ImpRig.RenderState
 */
public class MudcrabRenderState extends ImpRig.RenderState {

    /** 本帧泥沼蟹的变种；26.x 由渲染器填入，当前无读者（见类注释）。 */
    public MudcrabVariant variant;

    /**
     * 26.x：取当前实体的类型化引用。
     *
     * <p>{@code ImpRig.RenderState.entity} 的类型是 {@code LivingEntity}（imp 不该知道内容模块
     * 的实体类），这里做一次收窄，让模型/判定侧不必强转。旧族的渲染状态是占位类，
     * {@code entity} 恒为 {@code null}，但那份源码在旧族不参与渲染路径。</p>
     *
     * @return 本帧的泥沼蟹；尚未采集时为 {@code null}
     */
    public org.polaris2023.wildwind.wilderness.entity.animal.Mudcrab mudcrab() {
        return this.entity instanceof org.polaris2023.wildwind.wilderness.entity.animal.Mudcrab mudcrab
            ? mudcrab : null;
    }
}
