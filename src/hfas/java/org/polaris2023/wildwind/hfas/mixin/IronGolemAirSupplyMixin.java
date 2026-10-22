package org.polaris2023.wildwind.hfas.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.level.Level;
import org.polaris2023.wildwind.hfas.effect.ModEffects;
import org.polaris2023.wildwind.hfas.entity.ModEntityTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolem.class)
abstract class IronGolemAirSupplyMixin extends LivingEntity {
    protected IronGolemAirSupplyMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "decreaseAirSupply", at = @At("HEAD"), cancellable = true)
    private void wildwind$chokeAirConsumption(int currentSupply, CallbackInfoReturnable<Integer> cir) {
        if (is(ModEntityTypeTags.CHOKE_AFFECTED) && hasEffect(ModEffects.CHOKE)) {
            // Iron golems otherwise always return unchanged air, bypassing even Respiration's normal rule.
            cir.setReturnValue(super.decreaseAirSupply(currentSupply));
        }
    }
}
