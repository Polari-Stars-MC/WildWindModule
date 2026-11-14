package org.polaris2023.wildwind.core.client.entity.renderstate;

import org.polaris2023.wildwind.imp.client.ImpRig;

/**
 * 幼蛛渲染状态喵~
 *
 * <p>本类是 {@link ImpRig.RenderState} 的薄壳，内容模块因此不必直接接触
 * {@code LivingEntityRenderState} 这类版本专属类型。</p>
 *
 * <h2>26.x（本类真正有内容的一族）</h2>
 * <p>26.x 的渲染是<b>两阶段</b>的：{@code extractRenderState(entity, state, partialTick)} 采集，
 * {@code submit(state, ...)} 绘制。绘制与 {@code setupAnim(state)} 阶段<b>拿不到实体</b>，
 * 而"走还是站"必须问实体，所以 {@link ImpRig.RenderState} 在标准
 * {@code LivingEntityRenderState} 之上补了一个 {@code entity} 引用，由
 * {@code ImpRig.Renderer#extractRenderState} 填入。本类只补一个收窄到幼蛛的访问器
 * （{@link #spiderling()}），不再重复声明字段——避免字段隐藏导致读到的值与基类不一致。</p>
 * <p>行走信息不必额外加字段：{@code LivingEntityRenderState} 自带
 * {@code walkAnimationPos} / {@code walkAnimationSpeed}（已含 partialTick 插值），
 * 动画判定（{@code isMoving}）直接用它们，与 GeckoLib 4.x {@code AnimationTest#isMoving}
 * 的语义一致。</p>
 *
 * <h2>1.20.6 / 1.21.1（旧族没有渲染状态）</h2>
 * <p>旧族的 Minecraft <b>根本没有</b> {@code EntityRenderState} 体系——
 * {@code EntityRenderer} 只有 1 个类型参数，渲染直接在 {@code render(T, ...)} 里完成，
 * {@code setupAnim} 的第一个参数就是实体本身。因此旧族的 {@link ImpRig.RenderState}
 * 是<b>空占位类</b>：本类在那两族里不承载任何数据、也不参与渲染，存在的唯一目的是让
 * 内容模块的源码在三个族里逐字相同。</p>
 * <p>⚠ 因此<b>不要</b>往本类里塞字段并指望旧族能读到：那些字段在 1.20.6 / 1.21.1 的渲染路径上
 * 没有任何读取者。旧族需要渲染期数据时的正确做法是在 {@code Spiderling} 上暴露只读访问器。</p>
 *
 * @author safe_saffawa
 * @see ImpRig.RenderState
 */
public class SpiderlingRenderState extends ImpRig.RenderState {

    /**
     * 26.x：取当前实体的类型化引用。
     *
     * <p>{@code ImpRig.RenderState.entity} 的类型是 {@code LivingEntity}（imp 不该知道内容模块
     * 的实体类），这里做一次收窄，让模型侧不必强转。</p>
     *
     * <p>只在 26.x 有实际意义——旧族的渲染状态是占位类，{@code entity} 恒为 {@code null}，
     * 模型那边 {@code setupAnim} 直接收到实体本身，不会调用本方法。保留同一份源码是为了让三个族
     * 逐字相同（版本注解只允许出现在 imp 内）。</p>
     *
     * @return 本帧的幼蛛；尚未采集（或状态被别的渲染器复用）时为 {@code null}
     */
    public org.polaris2023.wildwind.core.entity.monster.Spiderling spiderling() {
        return this.entity instanceof org.polaris2023.wildwind.core.entity.monster.Spiderling spiderling
            ? spiderling : null;
    }
}
