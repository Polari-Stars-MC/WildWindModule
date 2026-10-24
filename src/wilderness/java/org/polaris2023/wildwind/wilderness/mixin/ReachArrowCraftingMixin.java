package org.polaris2023.wildwind.wilderness.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ImbueRecipe;
import org.polaris2023.wildwind.wilderness.arrow.ReachArrows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ImbueRecipe.class)
public abstract class ReachArrowCraftingMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void wildwind$reachArrow(CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(ReachArrows.convert(cir.getReturnValue()));
    }
}
