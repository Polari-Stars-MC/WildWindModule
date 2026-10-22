package org.polaris2023.wildwind.hfas.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import org.polaris2023.wildwind.hfas.event.SkeletonBehavior;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.polaris2023.wildwind.hfas.registry.ModAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 让生物的物品拾取范围受额外拾取距离属性加成的混入类喵~
 */
@Mixin(Mob.class)
public abstract class MobMixin {

    @Inject(method = "doHurtTarget", at = @At("RETURN"))
    private void wildWind$meleeEffects(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && target instanceof LivingEntity living) {
            SkeletonBehavior.afterMeleeHit((Mob) (Object) this, living);
        }
    }

	@ModifyVariable(method = "aiStep", at = @At(value = "STORE", ordinal = 0))
	private Vec3i reachMobEffectPickupRange(Vec3i vec3i) {
		Mob self = (Mob) (Object) this;

		AttributeMap attributes = self.getAttributes();
		if (attributes.hasAttribute(ModAttributes.EXTRA_ITEM_PICKUP_RANGE)) {
			double extraItemPickupRange = attributes.getValue(ModAttributes.EXTRA_ITEM_PICKUP_RANGE);
			if (extraItemPickupRange != 0.0) {
				vec3i = vec3i.offset((int) extraItemPickupRange, (int) extraItemPickupRange, (int) extraItemPickupRange);
			}
		}

		return vec3i;
	}
}
