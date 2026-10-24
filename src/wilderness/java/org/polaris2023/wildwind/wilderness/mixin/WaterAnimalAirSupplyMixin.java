package org.polaris2023.wildwind.wilderness.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import org.polaris2023.wildwind.wilderness.effect.FluidEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({Axolotl.class, WaterAnimal.class})
abstract class WaterAnimalAirSupplyMixin {
    @ModifyVariable(method = "handleAirSupply", at = @At("HEAD"), argsOnly = true)
    private int wildwind$accelerateLandDrying(int preTickAirSupply) {
        return FluidEffects.airBeforeDrying((LivingEntity) (Object) this, preTickAirSupply);
    }
}
