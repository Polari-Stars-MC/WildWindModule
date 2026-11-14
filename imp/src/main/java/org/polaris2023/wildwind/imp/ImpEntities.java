package org.polaris2023.wildwind.imp;

import net.minecraft.world.entity.Entity;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 实体上「同一语义、不同重载/可见性」的两个小口子（ADR-0003）。
 *
 * <h2>差异一：{@code push(Vec3)}</h2>
 * <p>{@code Entity.push} 的重载集合逐族增长（javap 核对）：</p>
 * <ul>
 *   <li><b>1.20.6</b>：{@code push(Entity)} 与 {@code push(double, double, double)}——
 *       <b>没有</b> {@code push(Vec3)}；</li>
 *   <li><b>1.21.1 / 26.2</b>：多一个 {@code push(Vec3)}，实现就是转发到三分量版本
 *       （1.21.1 源码：{@code this.push(p.x, p.y, p.z)}）。</li>
 * </ul>
 * <p>所以旧族用三分量重载表达<b>完全相同的语义</b>，不是降级。</p>
 *
 * <h2>差异二：{@code Entity.getRandom()}</h2>
 * <p>{@code getRandom()} 在 1.20.6 <b>只声明在 {@code LivingEntity} 上</b>，{@code Entity}
 * 本身只有 {@code protected final RandomSource random} 字段（源码核对：1.20.6 的
 * {@code Entity} 只有 {@code getRandomX/Y/Z}，没有公开 {@code getRandom()}）；
 * 1.21.1 起才上提到 {@code Entity}。</p>
 *
 * <p><b>能力降级</b>：1.20.6 下非 {@code LivingEntity} 的实体拿不到「自身随机源」，
 * 只能退到 {@code level.getRandom()}（世界随机源）。两者都是 {@code RandomSource}，
 * 统计性质相同但<b>不是同一个流</b>——同一次攻击的随机结果在两族不会逐位一致。
 * 生物实体走 {@code LivingEntity.getRandom()}，与新版完全一致。</p>
 *
 * @author baka4n
 * @author safe_saffawa
 */
public final class ImpEntities {

    private ImpEntities() {}

    /** 26.x / 1.21：直接转发 {@code push(Vec3)}。 */
    @Since("1.21")
    @Name("push")
    public static void pushModern(Entity entity, net.minecraft.world.phys.Vec3 motion) {
        entity.push(motion);
    }

    /** 1.20.6：没有 {@code push(Vec3)}，拆成三分量（语义相同）。 */
    @Until("1.21")
    @Name("push")
    public static void pushLegacy(Entity entity, net.minecraft.world.phys.Vec3 motion) {
        entity.push(motion.x, motion.y, motion.z);
    }

    /** 26.x / 1.21：{@code Entity} 自己就有 {@code getRandom()}。 */
    @Since("1.21")
    @Name("random")
    public static net.minecraft.util.RandomSource randomModern(Entity entity) {
        return entity.getRandom();
    }

    /**
     * 1.20.6：{@code Entity} 没有公开 {@code getRandom()}。
     *
     * <p>生物走自身的 {@code LivingEntity.getRandom()}（与新版一致）；
     * 其它实体退到世界随机源——见类注释的<b>能力降级</b>说明。</p>
     */
    @Until("1.21")
    @Name("random")
    public static net.minecraft.util.RandomSource randomLegacy(Entity entity) {
        if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
            return living.getRandom();
        }
        return entity.level().getRandom();
    }
}
