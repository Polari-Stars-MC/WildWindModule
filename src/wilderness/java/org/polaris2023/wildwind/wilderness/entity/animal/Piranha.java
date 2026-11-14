package org.polaris2023.wildwind.wilderness.entity.animal;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.polaris2023.wildwind.core.entity.WindupAttackMob;
import org.polaris2023.wildwind.core.entity.ai.goal.AlertOthersNearestAttackableTargetGoal;
import org.polaris2023.wildwind.core.entity.ai.goal.ChargingMeleeAttackGoal;
import org.polaris2023.wildwind.imp.client.ImpAnimatedEntity;
import org.polaris2023.wildwind.imp.client.ImpRig;
import org.polaris2023.wildwind.wilderness.registry.ModItems;
import org.polaris2023.wildwind.wilderness.registry.ModSoundEvents;

import javax.annotation.Nullable;

/**
 * 恐鱼；成群结队、围猎残血目标喵~
 *
 * <p><b>动画部分已经与 GeckoLib 完全解耦。</b>用 imp 的版本无关契约
 * {@link ImpAnimatedEntity}（"我有哪些动画"）+ {@link ImpRig.Animated}（渲染侧真正需要的两点：
 * 交出动画集、报告一次性动画的触发边沿）。实体因此只描述<b>语义</b>，不接触任何
 * {@code ModelPart}/{@code EntityModel}/{@code RenderLayer}。</p>
 *
 * <h2>动画规则（与迁移前的 GeckoLib 写法逐条对应，语义不变）</h2>
 * <pre>{@code
 * // 迁移前（GeckoLib）
 * controllers.add(new AnimationController<>("Move", 3, this::moveAnimController));
 * controllers.add(new AnimationController<>("Attack", state -> PlayState.STOP)
 *         .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING));
 * this.triggerAnim("Attack", "attack");
 *
 * // 现在
 * set.controller("Move", 3, st -> st.isInWater() ? "move.swim" : "misc.idle");
 * set.oneShot("Attack", "attack.swing");
 * this.triggerAnimation("Attack", "attack");
 * }</pre>
 * <ul>
 *   <li>{@code Move}：<b>永远</b>有动画可播（水里 {@code move.swim}、岸上 {@code misc.idle}），
 *       与旧写法的 {@code state.setAndContinue(...)} 一样不会返回 STOP；</li>
 *   <li>{@code Attack}：一次性 {@code attack.swing}，播完自动回落到 {@code Move} 的循环动画。</li>
 * </ul>
 *
 * <h2>为什么触发令牌是同步字段</h2>
 * <p>攻击起手判定（{@link #prepareAttack}）跑在<b>服务端</b>（{@code processPreparingAttack}），
 * 而动画只在<b>客户端</b>播放。若用普通字段，服务端置位后客户端永远看不到——这正是 GeckoLib 4.x
 * 的 {@code triggerAnim} 在"服务端调用、客户端不见反应"上踩过的坑。所以令牌走
 * {@link SynchedEntityData}。</p>
 *
 * @author mdjfnfn
 * @author safe_saffawa
 * @see ImpRig.Animated
 * @see ImpRig.Driver
 */
public class Piranha extends AbstractSchoolingFish implements WindupAttackMob, ImpRig.Animated {

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
            SynchedEntityData.defineId(Piranha.class, EntityDataSerializers.INT);

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

    public Piranha(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
        // registerAnimations 在接口里是"每次实例化调用一次"的约定，这里显式完成
        this.registerAnimations(this.animations);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.95)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACK_TRIGGER, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PiranhaAttackGoal(this, 2.0f, false));
        super.registerGoals();
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(1, new AlertOthersNearestAttackableTargetGoal<>(this, Mob.class, true,
                (entity, level) -> !(entity instanceof Piranha) && (entity.getHealth() <= entity.getMaxHealth() * 0.25))
                .overrideFindEntityFollowDistance(15.0)
        );
        this.targetSelector.addGoal(1, new AlertOthersNearestAttackableTargetGoal<>(this, Player.class, true,
                (entity, level) -> entity.getHealth() <= entity.getMaxHealth() * 0.25)
                .overrideFindEntityFollowDistance(15.0)
        );
    }

    @Override
    public int getMaxSchoolSize() {
        return 5;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return ModSoundEvents.PIRANHA_FLOP.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSoundEvents.PIRANHA_DEATH.get();
    }

    protected SoundEvent getAttackSound() {
        return ModSoundEvents.PIRANHA_ATTACK.get();
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack((ItemLike) ModItems.PIRANHA_BUCKET);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 动画契约（ImpAnimatedEntity + ImpRig.Animated）
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 登记动画规则——与迁移前 GeckoLib 版本的语义逐条对应。
     *
     * <p>旧的 {@code moveAnimController} 是：</p>
     * <pre>{@code
     * return state.setAndContinue(this.isInWater() ? DefaultAnimations.SWIM : DefaultAnimations.IDLE);
     * }</pre>
     * <p>即"在水里就游、不在水里就待机"，<b>两种分支都返回 CONTINUE</b>、从不 STOP。
     * 这里逐字搬过来：判定用 {@link ImpAnimatedEntity.State#isInWater()}（由 {@link ImpRig.Driver}
     * 每帧从实体上取 {@code Entity#isInWater()}），返回值是动画文件里的键。</p>
     */
    @Override
    public void registerAnimations(ImpAnimatedEntity.AnimationSet set) {
        // 水里循环 move.swim，否则循环 misc.idle（旧写法同样永不 STOP）
        set.controller("Move", 3, state -> state.isInWater() ? "move.swim" : "misc.idle");
        // 攻击：由 triggerAnimation 插入，播完自动回落到循环动画
        set.oneShot(ATTACK_CONTROLLER, ATTACK_TRIGGER);
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
    public void setPreparingToAttackTick(int tick) {
        this.preparingToAttack = tick;
    }

    @Override
    public int getPreparingToAttackTick() {
        return this.preparingToAttack;
    }

    @Override
    public int getPreparationToAttackDuration() {
        return 6;
    }

    @Override
    public boolean prepareAttack(LivingEntity entity) {
        boolean result = WindupAttackMob.super.prepareAttack(entity);
        if (result) {
            this.triggerAnimation(ATTACK_CONTROLLER, ATTACK_TRIGGER);
            this.makeSound(this.getAttackSound());
        }
        return result;
    }

    @Override
    public boolean canContinuePreparingAttack() {
        return this.isInWater();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && this.isAlive()) {
            processPreparingAttack((ServerLevel) this.level(), this);
        }
    }

    protected static class PiranhaAttackGoal extends ChargingMeleeAttackGoal<Piranha> {
        public PiranhaAttackGoal(Piranha mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return this.mob.isInWater() && super.canUse();
        }
    }
}
