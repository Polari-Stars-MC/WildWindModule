package org.polaris2023.wildwind.core.entity.monster;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;
import org.polaris2023.wildwind.core.entity.WindupAttackMob;
import org.polaris2023.wildwind.core.entity.ai.goal.ChargingMeleeAttackGoal;
import org.polaris2023.wildwind.core.registry.ModSoundEvents;
import org.polaris2023.wildwind.imp.ImpSpiders;
import org.polaris2023.wildwind.imp.client.ImpAnimatedEntity;
import org.polaris2023.wildwind.imp.client.ImpRig;

/**
 * 幼蛛；继承原版蜘蛛的攀爬与目标选择，攻击改为蓄力扑击喵~
 *
 * <p><b>动画部分已经与 GeckoLib 完全解耦。</b>用 imp 的版本无关契约
 * {@link ImpAnimatedEntity}（"我有哪些动画"）+ {@link ImpRig.Animated}（渲染侧真正需要的两点：
 * 交出动画集、报告一次性动画的触发边沿）。实体因此只描述<b>语义</b>，不接触任何
 * {@code ModelPart}/{@code EntityModel}/{@code RenderLayer}。</p>
 *
 * <h2>动画规则（与迁移前的 GeckoLib 写法逐条对应，语义不变）</h2>
 * <pre>{@code
 * // 迁移前（GeckoLib）
 * controllers.add(ImpGeo.anAnimation(this, "Move", 2,
 *     state -> state.isMoving() ? state.walk() : ImpGeo.Play.STOP));
 * controllers.add(ImpGeo.anAnimation(this, "Attack", 0, state -> ImpGeo.Play.STOP)
 *     .triggerable("attack", ImpGeo.Animation.ATTACK_SWING));
 * this.triggerAnimation("Attack", "attack");
 *
 * // 现在
 * set.controller("Move", 2, st -> st.isMoving() ? "move.walk" : null);
 * set.oneShot("Attack", "attack.swing");
 * this.triggerAnimation("Attack", "attack");
 * }</pre>
 * <ul>
 *   <li>{@code Move}（过渡 2 tick、优先级 2）→ 循环 {@code move.walk}；</li>
 *   <li>{@code Attack}（优先级 0、控制器自身不循环）→ 一次性 {@code attack.swing}；</li>
 *   <li>一次性动画有<b>优先级覆盖</b>：播放期间覆盖循环动画的同名骨骼通道。</li>
 * </ul>
 *
 * <h2>为什么触发令牌是同步字段</h2>
 * <p>攻击起手判定（{@link #prepareAttack}）跑在<b>服务端</b>（{@code processPreparingAttack}），
 * 而动画只在<b>客户端</b>播放。若用普通字段，服务端置位后客户端永远看不到——这正是 GeckoLib 4.x
 * 的 {@code triggerAnim} 在"服务端调用、客户端不见反应"上踩过的坑。所以令牌走
 * {@link SynchedEntityData}。</p>
 *
 * <h2>关于 {@code Spider} 的包路径</h2>
 * <p>{@code Spider} 在 26.x 位于 {@code net.minecraft.world.entity.monster.spider}、旧版位于
 * {@code net.minecraft.world.entity.monster}。这是 Minecraft 自身的包迁移，本类不 import 任何
 * 版本的 {@code Spider}，由 {@link ImpSpiders.Base} 承载那个 {@code extends}
 * （{@link ImpSpiders} 里已不再有任何 GeckoLib 内容）。</p>
 *
 * @author safe_saffawa
 * @see ImpRig.Animated
 * @see ImpRig.Driver
 */
public class Spiderling extends ImpSpiders.Base implements ImpRig.Animated, WindupAttackMob {

    /** 攻击动画的控制器名（与 {@code AnimationSet#oneShot} 的第一个参数一致）。 */
    public static final String ATTACK_CONTROLLER = "Attack";

    /** 攻击动画的触发键（与 {@code AnimationSet#oneShot} 的第二个参数一致）。 */
    public static final String ATTACK_TRIGGER = "attack";

    /**
     * 一次性动画的触发令牌。
     *
     * <p>取值为"已触发过的次数"。渲染侧每帧比对它是否变化来检测<b>边沿</b>，
     * 所以连续两次攻击会得到不同的值，动画不会被去重吞掉。</p>
     */
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TRIGGER =
        SynchedEntityData.defineId(Spiderling.class, EntityDataSerializers.INT);

    /** 本实体登记的动画集（构造期填好，之后只读）。 */
    private final ImpAnimatedEntity.AnimationSet animations = new ImpAnimatedEntity.AnimationSet();

    /**
     * 渲染侧已经消费到的令牌值（只增不减，永不回落）。
     *
     * <p>初值 <b>0</b> 而不是 -1：{@code DATA_ATTACK_TRIGGER} 的同步默认值就是 0，
     * 若初值取 -1，实体刚出现的第一帧就会被当成"令牌变化过"而多播一次攻击动画。</p>
     */
    private int consumedAttackTrigger = 0;

    private int preparingToAttack;

    public Spiderling(EntityType<? extends ImpSpiders.Base> entityType, Level level) {
        super(entityType, level);
        // registerAnimations 在接口里是"每次实例化调用一次"的约定，这里显式完成
        this.registerAnimations(this.animations);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ImpSpiders.Base.createAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACK_TRIGGER, 0);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 动画契约（ImpAnimatedEntity + ImpRig.Animated）
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 登记动画规则——与迁移前 GeckoLib 版本的语义逐条对应。
     *
     * <p>{@code Move} 的优先级 2 高于 {@code Attack} 的 0，但 {@code Attack} 是<b>一次性</b>
     * 而不是循环控制器，所以在状态机里它走"覆盖"通道而不是优先级比较；这里的数字只是
     * 与迁移前的写法保持一致，可读性优先。</p>
     */
    @Override
    public void registerAnimations(ImpAnimatedEntity.AnimationSet set) {
        // 移动时循环 move.walk；静止时返回 null（对应 GeckoLib 的 PlayState.STOP）
        set.controller("Move", 2, state -> state.isMoving() ? "move.walk" : null);
        // 攻击：由 triggerAnimation 插入，播完自动回落到循环动画
        set.oneShot(ATTACK_CONTROLLER, "attack.swing");
    }

    /** 渲染器通过这个访问器拿到动画集（{@code registerAnimations} 不回传结果）。 */
    @Override
    public ImpAnimatedEntity.AnimationSet animationSet() {
        return this.animations;
    }

    /**
     * 取走一次性动画的触发令牌。
     *
     * <p>只在令牌值与上次消费的不同时返回 {@code true} 一次。语义与 GeckoLib 的
     * {@code triggerableAnim} 一致：一次触发对应一次播放，重复触发会重新从 0 播。</p>
     *
     * <p>注意这是<b>渲染侧</b>调用（每帧、每渲染器一次），因此只读同步字段、不加锁。</p>
     */
    @Override
    public boolean consumeAnimationTrigger(String controllerName) {
        if (!ATTACK_CONTROLLER.equals(controllerName)) {
            return false;
        }
        int current = this.getEntityData().get(DATA_ATTACK_TRIGGER);
        if (current == this.consumedAttackTrigger) {
            return false;
        }
        this.consumedAttackTrigger = current;
        return true;
    }

    /**
     * 触发一次性动画（对应 GeckoLib 的 {@code triggerAnim(controller, key)}）。
     *
     * <p>两个形参都保留 GeckoLib 的写法，便于对照；但<b>只有一次性的控制器</b>能触发——
     * 循环控制器的动画由每帧的 selector 决定，触发它没有意义。</p>
     *
     * @param controllerName 一次性控制器名（本实体只有 {@value #ATTACK_CONTROLLER}）
     * @param animationKey   动画键（本实体只有 {@value #ATTACK_TRIGGER}）
     */
    public void triggerAnimation(String controllerName, String animationKey) {
        if (!ATTACK_CONTROLLER.equals(controllerName) || !ATTACK_TRIGGER.equals(animationKey)) {
            return;
        }
        // 令牌自增而不是置 true：两次相邻攻击会得到不同的值，边沿检测不会把它们合并
        this.entityData.set(DATA_ATTACK_TRIGGER, this.entityData.get(DATA_ATTACK_TRIGGER) + 1);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 行为
    // ══════════════════════════════════════════════════════════════════════

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.removeAllGoals(goal -> goal instanceof MeleeAttackGoal);
        this.goalSelector.addGoal(4, new ChargingMeleeAttackGoal<>(this, 1.0, true));
    }

    @Override
    protected boolean isImmobile() {
        return !this.isPreparingToAttack() && super.isImmobile();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.SPIDERLING_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.SPIDERLING_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.SPIDERLING_DEATH.get();
    }

    @Override
    public boolean onClimbable() {
        return !this.isPreparingToAttack() && super.onClimbable();
    }

    @Override
    public void setPreparingToAttackTick(int tick) {
        this.preparingToAttack = tick;
    }

    @Override
    public int getPreparingToAttackTick() {
        return this.preparingToAttack;
    }

    @Override
    public int getPreparationToAttackDuration() {
        return 13;
    }

    @Override
    public boolean prepareAttack(LivingEntity entity) {
        boolean result = WindupAttackMob.super.prepareAttack(entity);
        if (result) this.triggerAnimation(ATTACK_CONTROLLER, ATTACK_TRIGGER);
        return result;
    }

    @Override
    public void onAttackDone(LivingEntity target) {
        this.addDeltaMovement(target.getPosition(1.0f)
                .subtract(this.getPosition(1.0f))
                .normalize()
                .scale(0.7)
                .add(0.0, 0.45, 0.0));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel serverLevel && this.isAlive()) {
            this.processPreparingAttack(serverLevel, this);
        }
    }
}
