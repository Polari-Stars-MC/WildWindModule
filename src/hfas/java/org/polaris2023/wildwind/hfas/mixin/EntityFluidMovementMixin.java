package org.polaris2023.wildwind.hfas.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.polaris2023.wildwind.hfas.effect.FluidEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
abstract class EntityFluidMovementMixin {
    // Intercept the actual displacement, including swimming AI and bubble-column upward motion.
    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
    private Vec3 wildwind$preventRising(Vec3 movement) {
        return FluidEffects.restrictMovement((Entity) (Object) this, movement);
    }
}
