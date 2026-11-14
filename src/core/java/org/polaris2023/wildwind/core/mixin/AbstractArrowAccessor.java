package org.polaris2023.wildwind.core.mixin;

import org.polaris2023.wildwind.imp.ImpArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * 箭矢实体的内部字段 / 方法访问器。
 * <p>
 * {@code baseDamage} 是私有字段，{@code setPierceLevel} 是私有方法，
 * 矢术的「伤害加成」与「穿透 1 个目标」都要用到。
 * <p>
 * 这些成员只存在于 {@code AbstractArrow} 上，不在三族稳定的父类 {@code Projectile} 里，
 * 所以在 mixin 体中不能直接调用——一律经本接口转发。
 * <p>
 * 目标类用字符串（{@code @Mixin(targets = ...)}）：箭矢类的包路径在 26.x 被移进了
 * {@code projectile.arrow} 子包，写类字面量会让另一族编译失败。
 *
 * @author safe_saffawa
 */
@Mixin(targets = ImpArrow.TARGET)
public interface AbstractArrowAccessor {

    @Accessor("baseDamage")
    double wildwind$getBaseDamage();

    @Invoker("setBaseDamage")
    void wildwind$setBaseDamage(double baseDamage);

    @Invoker("getPierceLevel")
    byte wildwind$getPierceLevel();

    @Invoker("setPierceLevel")
    void wildwind$setPierceLevel(byte pierceLevel);
}
