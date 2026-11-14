package org.polaris2023.wildwind.wilderness.client.entity.renderstate;

import org.polaris2023.wildwind.imp.client.ImpRig;

/**
 * 恐鱼渲染状态喵~
 *
 * <p>本类是 {@link ImpRig.RenderState} 的薄壳，内容模块因此不必直接接触
 * {@code LivingEntityRenderState} 这类版本专属类型。</p>
 *
 * <h2>26.x（本类真正有内容的一族）</h2>
 * <p>26.x 的渲染是两阶段的：{@code extractRenderState} 采集、{@code submit} 绘制。
 * 绘制与 {@code setupAnim(state)} 阶段拿不到实体，而"在水里还是不在"必须问实体，
 * 所以 {@link ImpRig.RenderState} 在标准 {@code LivingEntityRenderState} 之上补了一个
 * {@code entity} 引用（由 {@code ImpRig.Renderer#extractRenderState} 填入）。本类只补一个
 * 收窄到恐鱼的访问器（{@link #piranha()}），不重复声明字段。</p>
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
 * @author mdjfnfn
 * @author safe_saffawa
 * @see ImpRig.RenderState
 */
public class PiranhaRenderState extends ImpRig.RenderState {

    /**
     * 26.x：取当前实体的类型化引用。
     *
     * <p>{@code ImpRig.RenderState.entity} 的类型是 {@code LivingEntity}（imp 不该知道内容模块
     * 的实体类），这里做一次收窄，让模型/判定侧不必强转。旧族的渲染状态是占位类，
     * {@code entity} 恒为 {@code null}，但那份源码在旧族不参与渲染路径。</p>
     *
     * @return 本帧的恐鱼；尚未采集时为 {@code null}
     */
    public org.polaris2023.wildwind.wilderness.entity.animal.Piranha piranha() {
        return this.entity instanceof org.polaris2023.wildwind.wilderness.entity.animal.Piranha piranha
            ? piranha : null;
    }
}
