package org.polaris2023.wildwind.imp;

import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 箭矢实体的版本无关访问口（ADR-0003）。
 *
 * <p>解决的问题：{@code AbstractArrow} 在 1.20 / 1.21 位于
 * {@code net.minecraft.world.entity.projectile}，26.x 被移进
 * {@code net.minecraft.world.entity.projectile.arrow} 子包。类型本身的行为没变，
 * 只是位置变了，所以内容模块不该为此写出两套逻辑。</p>
 *
 * <p>这里收敛两件事：</p>
 * <ol>
 *   <li>{@link #TARGET}：Mixin 用<b>字符串目标</b>（{@code @Mixin(targets = ...)}）指向
 *       {@code AbstractArrow}，这样 core 的 mixin 不必 import 一个只在某族存在的类。
 *       注解值必须是编译期常量，因此这里按族分叉成 {@code static final String}，
 *       再用 {@code @Name} 统一成同一个名字。</li>
 *   <li>{@link #pickupItemStack}：{@code getPickupItemStackOrigin()} 是
 *       {@code AbstractArrow} 独有的方法（{@code Projectile} 上没有）。调用方统一把箭矢
 *       当作 {@link net.minecraft.world.entity.projectile.Projectile} 传递
 *       ——那个路径三族都一样——只有这一处需要往下转型。</li>
 * </ol>
 *
 * <p>注意：版本分叉的类一律不写 import，成员内用全限定名。</p>
 */
public final class ImpArrow {

    private ImpArrow() {}

    /**
     * {@code AbstractArrow} 的全限定类名，供 {@code @Mixin(targets = ...)} 使用。
     *
     * <p>26.x：随 {@code projectile.arrow} 子包迁移。</p>
     */
    @Since("26.1")
    @Name("TARGET")
    public static final String TARGET_MODERN = "net.minecraft.world.entity.projectile.arrow.AbstractArrow";

    /** 1.20 / 1.21：{@code AbstractArrow} 仍在 {@code projectile} 包下。 */
    @Until("26.1")
    @Name("TARGET")
    public static final String TARGET_LEGACY = "net.minecraft.world.entity.projectile.AbstractArrow";

    /** 26.x：读取箭矢自带的拾取物品（矢术组件就存在这里）。 */
    @Since("26.1")
    @Name("pickupItemStack")
    public static net.minecraft.world.item.ItemStack pickupItemStackModern(
            net.minecraft.world.entity.projectile.Projectile arrow) {
        return ((net.minecraft.world.entity.projectile.arrow.AbstractArrow) arrow).getPickupItemStackOrigin();
    }

    /** 1.20 / 1.21：同名字段访问器，只是包路径不同。 */
    @Until("26.1")
    @Name("pickupItemStack")
    public static net.minecraft.world.item.ItemStack pickupItemStackLegacy(
            net.minecraft.world.entity.projectile.Projectile arrow) {
        return ((net.minecraft.world.entity.projectile.AbstractArrow) arrow).getPickupItemStackOrigin();
    }

    /**
     * 该投射物是否真的是箭矢。
     *
     * <p>调用方按 {@code Projectile} 接收参数（那个路径三族一致），转型前先用本方法判一次，
     * 避免把雪球、恶魂火球之类也当成箭矢而抛 {@code ClassCastException}。</p>
     */
    @Since("26.1")
    @Name("isArrow")
    public static boolean isArrowModern(net.minecraft.world.entity.projectile.Projectile projectile) {
        return projectile instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow;
    }

    /** 1.20 / 1.21 的 {@code isArrow}。 */
    @Until("26.1")
    @Name("isArrow")
    public static boolean isArrowLegacy(net.minecraft.world.entity.projectile.Projectile projectile) {
        return projectile instanceof net.minecraft.world.entity.projectile.AbstractArrow;
    }
}
