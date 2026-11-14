package org.polaris2023.wildwind.imp;

import net.minecraft.world.entity.Entity;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 少数「同一语义、不同写法」的方块/实体/物品便捷方法的版本无关口（ADR-0003）。
 *
 * <p>三处差异，都是 26.x 单方面调整 API 形状，语义没变：</p>
 * <ol>
 *   <li><b>无碰撞方块属性</b>：1.20 / 1.21 是 {@code noCollission()}（Mojang 祖传拼写），
 *       26.x 修正为 {@code noCollision()}。</li>
 *   <li><b>刷怪蛋</b>：1.20 / 1.21 用 {@code new SpawnEggItem(type, bgColor, hlColor, props)}，
 *       26.x 改成 {@code new SpawnEggItem(props.spawnEgg(type))}。</li>
 *   <li><b>近战伤害</b>：1.20 / 1.21 是 {@code mob.doHurtTarget(target)}，
 *       26.x 变成需要显式 {@code ServerLevel} 的 {@code mob.doHurtTarget(level, target)}。</li>
 * </ol>
 */
public final class ImpLegacy {

    private ImpLegacy() {}

    /**
     * 26.x：{@code Properties.noCollision()}。
     *
     * @return 传入的同一个 builder（便于链式调用）
     */
    @Since("26.1")
    @Name("noCollision")
    public static net.minecraft.world.level.block.state.BlockBehaviour.Properties noCollisionModern(
            net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        return properties.noCollision();
    }

    /** 1.20 / 1.21：{@code noCollission()}（旧拼写，双 s）。 */
    @Until("26.1")
    @Name("noCollision")
    public static net.minecraft.world.level.block.state.BlockBehaviour.Properties noCollisionLegacy(
            net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        return properties.noCollission();
    }

    /**
     * 26.x：刷怪蛋从 {@code Item.Properties.spawnEgg(type)} 构造。
     *
     * <p><b>能力差异</b>：26.x 已经把刷怪蛋的底色 / 斑点色从 API 里<b>整个移除</b>
     * （{@code SpawnEggItem} 只剩 {@code SpawnEggItem(Item.Properties)} 一个构造器，
     * 颜色改由资源管线按实体类型推导）。因此这两个颜色参数在 26.x 下<b>被忽略</b>，
     * 刷怪蛋外观由原版决定——这不是遗漏，而是 26.x 已没有对应能力。</p>
     */
    @Since("26.1")
    @Name("spawnEgg")
    public static net.minecraft.world.item.SpawnEggItem spawnEggModern(
            net.minecraft.world.item.Item.Properties properties,
            java.util.function.Supplier<? extends net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.Mob>> type,
            int backgroundColor, int highlightColor) {
        return new net.minecraft.world.item.SpawnEggItem(properties.spawnEgg(type.get()));
    }

    /** 1.20 / 1.21：颜色是构造器参数。 */
    @Until("26.1")
    @Name("spawnEgg")
    public static net.minecraft.world.item.SpawnEggItem spawnEggLegacy(
            net.minecraft.world.item.Item.Properties properties,
            java.util.function.Supplier<? extends net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.Mob>> type,
            int backgroundColor, int highlightColor) {
        return new net.minecraft.world.item.SpawnEggItem(type.get(), backgroundColor, highlightColor, properties);
    }

    /** 26.x：{@code doHurtTarget} 需要显式 {@code ServerLevel}。 */
    @Since("26.1")
    @Name("doHurtTarget")
    public static boolean doHurtTargetModern(net.minecraft.world.entity.Mob mob,
                                             net.minecraft.server.level.ServerLevel level,
                                             Entity target) {
        return mob.doHurtTarget(level, target);
    }

    /** 1.20 / 1.21：{@code doHurtTarget} 只接受目标，level 从实体自身取。 */
    @Until("26.1")
    @Name("doHurtTarget")
    public static boolean doHurtTargetLegacy(net.minecraft.world.entity.Mob mob,
                                             net.minecraft.server.level.ServerLevel level,
                                             Entity target) {
        return mob.doHurtTarget(target);
    }
}
