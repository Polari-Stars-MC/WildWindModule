package org.polaris2023.wildwind.imp;

import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 原版蜘蛛的版本无关桥接（<b>这不是 GeckoLib 差异</b>，是 Minecraft 自身的包迁移）。
 *
 * <p>26.x 把 {@code Spider} 从 {@code net.minecraft.world.entity.monster} 挪到了
 * {@code net.minecraft.world.entity.monster.spider}（同包还迁入了 CaveSpider 等）。
 * 类的位置变化无法用 {@code @Name} 表达（它只改名字、不改包），内容模块又不能
 * 为一个 {@code extends} 写条件 import——{@code import} 不接受版本注解。</p>
 *
 * <p>解决办法是让 imp <b>持有那个 extends</b>：内容模块继承 {@link Base}，
 * 而 {@link Base} 在每个版本族里分别继承当时正确的 {@code Spider}。
 * 预处理器只保留一段，所以三个产物里 {@code Base} 都只有一个确定的父类。</p>
 *
 * <p>顺带收口 {@code Spider.createAttributes()}：静态方法不被继承，
 * 版本分叉同样只能由 imp 转发。</p>
 *
 * <h2>本类已完全去 GeckoLib 化</h2>
 * <p>幼蛛（{@code Spiderling}）的实体渲染已经从 GeckoLib 迁移到原版渲染管线
 * （见 {@code ImpRig} 与 ADR-0006），动画契约由 {@code ImpAnimatedEntity} / {@code ImpRig.Animated}
 * 承担，不再经过 GeckoLib 的 {@code GeoEntity} / {@code AnimatableInstanceCache}。
 * 因此本类现在<b>只剩</b>一件事：承载 {@code Spider} 的包路径分叉。它不能删——
 * 那是 Minecraft 自身的差异，与 GeckoLib 无关。</p>
 *
 * @author safe_saffawa
 */
public final class ImpSpiders {

    private ImpSpiders() {}

    /** 26.x：{@code Spider} 位于 {@code ...monster.spider}。 */
    @Since("26.1")
    public abstract static class Base extends net.minecraft.world.entity.monster.spider.Spider {
        protected Base(net.minecraft.world.entity.EntityType<? extends Base> entityType,
                       net.minecraft.world.level.Level level) {
            super(entityType, level);
        }

        /** 转发 {@code Spider.createAttributes()}（静态方法不被继承）。 */
        public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
            return net.minecraft.world.entity.monster.spider.Spider.createAttributes();
        }
    }

    /** 1.20 / 1.21：{@code Spider} 位于 {@code ...monster}。 */
    @Until("26.1")
    public abstract static class Base extends net.minecraft.world.entity.monster.Spider {
        protected Base(net.minecraft.world.entity.EntityType<? extends Base> entityType,
                       net.minecraft.world.level.Level level) {
            super(entityType, level);
        }

        /** 转发 {@code Spider.createAttributes()}（静态方法不被继承）。 */
        public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
            return net.minecraft.world.entity.monster.Spider.createAttributes();
        }
    }
}
