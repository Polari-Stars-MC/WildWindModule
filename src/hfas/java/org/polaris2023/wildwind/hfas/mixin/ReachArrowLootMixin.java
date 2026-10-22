package org.polaris2023.wildwind.hfas.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.SetRandomPotionFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import org.polaris2023.wildwind.hfas.arrow.ReachArrows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SetRandomPotionFunction.class, SetPotionFunction.class})
public abstract class ReachArrowLootMixin {
    @Inject(method = "run", at = @At("RETURN"), cancellable = true)
    private void wildwind$reachArrow(CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(ReachArrows.convert(cir.getReturnValue()));
    }
}
