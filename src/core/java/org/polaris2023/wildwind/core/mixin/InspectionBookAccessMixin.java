package org.polaris2023.wildwind.core.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.world.item.ItemStack;
import org.polaris2023.wildwind.core.client.InspectionBookPages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookViewScreen.BookAccess.class)
public abstract class InspectionBookAccessMixin {
    @Inject(method = "fromItem", at = @At("RETURN"), cancellable = true)
    private static void wildwind$paginate(ItemStack stack, CallbackInfoReturnable<BookViewScreen.BookAccess> cir) {
        var access = cir.getReturnValue();
        if (access != null) {
            var pages = InspectionBookPages.layout(access.pages(), Minecraft.getInstance().font);
            if (!pages.equals(access.pages())) cir.setReturnValue(new BookViewScreen.BookAccess(pages));
        }
    }
}
