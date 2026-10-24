package org.polaris2023.wildwind.core.mixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.polaris2023.wildwind.core.event.SkeletonBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Mob.class)
public abstract class SkeletonMeleeMixin {
    @Inject(method = "doHurtTarget", at = @At("RETURN"))
    private void wildWind$meleeEffects(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && target instanceof LivingEntity living)
            SkeletonBehavior.afterMeleeHit((Mob)(Object)this, living);
    }
}
