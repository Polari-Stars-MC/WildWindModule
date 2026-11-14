package org.polaris2023.wildwind.core.mixin;

import net.minecraft.world.entity.projectile.Projectile;
import org.polaris2023.wildwind.core.arrow.ArrowEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;
import org.polaris2023.wildwind.core.arrow.ArrowHead;
import org.polaris2023.wildwind.core.arrow.ArrowShaft;
import org.polaris2023.wildwind.core.component.ArrowComponent;
import org.polaris2023.wildwind.imp.ImpArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 矢术效果注入：把 {@link ArrowComponent} 的箭羽 / 箭杆 / 箭头数据翻译成实际玩法效果。
 * <p>
 * 组件由箭矢实体自己的 {@code pickupItemStack} 携带（原版就会存读档），所以这里只负责读取与应用。
 * 没有组件的普通箭走原版逻辑，本 mixin 全部方法都会提前返回。
 * <p>
 * 目标类用<b>字符串</b>（{@code @Mixin(targets = ...)}）而不是 {@code AbstractArrow.class}：
 * 箭矢类的包路径在 26.x 被移进了 {@code projectile.arrow} 子包，写类字面量会让另一族编译失败。
 * 实体本身一律以三族稳定的 {@link Projectile} 处理；只属于 {@code AbstractArrow} 的成员
 * 经 {@link AbstractArrowAccessor} 转发。
 *
 * @author baka4n
 * @author safe_saffawa
 */
@Mixin(targets = ImpArrow.TARGET)
public abstract class AbstractArrowMixin {

    /** 伤害加成每个箭矢只加一次，避免穿透箭反复命中时叠加 */
    @Unique
    private boolean wildwind$damageBoosted;

    /** 旋风棒箭杆的无重力只在首段飞行生效，否则减速后会永久悬停在空中 */
    @Unique
    private static final int WILDWIND$NO_GRAVITY_TICKS = 100;

    @Unique
    private @Nullable ArrowComponent wildwind$component() {
        return ArrowEffects.componentOf(this.wildwind$self());
    }

    @Unique
    private Projectile wildwind$self() {
        return (Projectile) (Object) this;
    }

    @Unique
    private AbstractArrowAccessor wildwind$accessor() {
        return (AbstractArrowAccessor) this;
    }

    /**
     * 飞行中：火尾粒子；竹子箭杆补 1 级穿透。
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void wildwind$tick(CallbackInfo ci) {
        ArrowComponent component = this.wildwind$component();
        if (component == null) {
            return;
        }

        ArrowEffects.tick(this.wildwind$self(), component);

        if (component.shaft() == ArrowShaft.BAMBOO && this.wildwind$accessor().wildwind$getPierceLevel() == 0) {
            this.wildwind$accessor().wildwind$setPierceLevel((byte) 1);
        }
    }

    /**
     * 命中实体前：箭杆的伤害加成（骨头 +1）。
     */
    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void wildwind$applyDamageBonus(EntityHitResult hitResult, CallbackInfo ci) {
        ArrowComponent component = this.wildwind$component();
        if (component == null || this.wildwind$damageBoosted) {
            return;
        }
        this.wildwind$damageBoosted = true;

        float bonus = component.getDamageBonus();
        if (bonus == 0.0F) {
            return;
        }
        AbstractArrowAccessor accessor = this.wildwind$accessor();
        accessor.wildwind$setBaseDamage(accessor.wildwind$getBaseDamage() + bonus);
    }

    /**
     * 命中实体后：箭头效果（爆炸 / 风弹 / 弹力 / 致燃 / 传送 / 交换位置）。
     */
    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void wildwind$onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        ArrowComponent component = this.wildwind$component();
        if (component == null) {
            return;
        }
        net.minecraft.world.entity.Entity target = hitResult.getEntity();
        if (target == null) {
            return;
        }
        ArrowEffects.onHitEntity(this.wildwind$self(), component, target);
    }

    /**
     * 命中方块后：箭头效果（爆炸 / 风弹 / 火把 / 破坏植物玻璃）与箭杆效果（生成火焰）。
     */
    @Inject(method = "onHitBlock", at = @At("TAIL"))
    private void wildwind$onHitBlock(BlockHitResult hitResult, CallbackInfo ci) {
        ArrowComponent component = this.wildwind$component();
        if (component == null) {
            return;
        }
        net.minecraft.core.BlockPos pos = hitResult.getBlockPos();
        if (pos == null) {
            return;
        }
        ArrowEffects.onHitBlock(this.wildwind$self(), component, hitResult);
    }

    /**
     * 不可回收的箭矢（烈焰棒 / 旋风棒箭杆、风弹 / 煤炭 / 紫颂果 / 末影之眼箭头）不允许捡回。
     */
    @Inject(method = "tryPickup", at = @At("HEAD"), cancellable = true)
    private void wildwind$tryPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        ArrowComponent component = this.wildwind$component();
        if (component != null && component.isNotRecoverable()) {
            this.wildwind$self().discard();
            cir.setReturnValue(false);
        }
    }

    /**
     * 箭杆重力倍率：旋风棒无重力，骨头下坠快，竹子下坠慢。
     * <p>
     * 旋风棒只在“刚出手且未入水”时无重力：无重力箭矢永远不会落地，
     * 也就永远不会进入原版的地面存活计时；若不限时，空气阻力会把速度衰减到接近 0，
     * 表现就是箭矢永久浮在空中、最后看起来定住了。
     */
    @Inject(method = "getDefaultGravity", at = @At("HEAD"), cancellable = true)
    private void wildwind$gravity(CallbackInfoReturnable<Double> cir) {
        ArrowComponent component = this.wildwind$component();
        if (component == null) {
            return;
        }

        if (component.shaft().hasNoGravity()) {
            Projectile self = this.wildwind$self();
            if (!self.isInFluidType() && self.tickCount < WILDWIND$NO_GRAVITY_TICKS) {
                cir.setReturnValue(0.0);
            }
            return;
        }

        float multiplier = component.getGravityMultiplier();
        if (multiplier != 1.0F) {
            // 原版箭矢重力为 0.05
            cir.setReturnValue(0.05 * multiplier);
        }
    }

    /**
     * 海晶碎片箭头：无视流体重力（水中阻尼从 0.6 提到 1.0）。
     */
    @Inject(method = "getWaterInertia", at = @At("HEAD"), cancellable = true)
    private void wildwind$waterInertia(CallbackInfoReturnable<Float> cir) {
        ArrowComponent component = this.wildwind$component();
        if (component != null && component.head() == ArrowHead.PRISMARINE_SHARD) {
            cir.setReturnValue(1.0F);
        }
    }

    /**
     * 箭羽：出手速度倍率（幻翼膜 1.5 倍）。
     */
    @ModifyVariable(method = "shoot", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float wildwind$speedMultiplier(float power) {
        ArrowComponent component = this.wildwind$component();
        if (component == null) {
            return power;
        }
        return power * component.getSpeedMultiplier();
    }

    /**
     * 箭杆：额外散布（骨头随机偏移大）。
     */
    @ModifyVariable(method = "shoot", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float wildwind$inaccuracy(float uncertainty) {
        ArrowComponent component = this.wildwind$component();
        if (component == null) {
            return uncertainty;
        }
        return uncertainty + component.getInaccuracy();
    }
}
