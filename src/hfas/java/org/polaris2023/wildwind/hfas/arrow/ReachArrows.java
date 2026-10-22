package org.polaris2023.wildwind.hfas.arrow;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.polaris2023.wildwind.hfas.registry.ModItems;
import org.polaris2023.wildwind.hfas.registry.ModPotions;

public final class ReachArrows {
    private ReachArrows() {}

    /** Preserve counts and potion/custom components when vanilla crafting or trades create Reach arrows. */
    public static ItemStack convert(ItemStack stack) {
        var potion = stack.get(DataComponents.POTION_CONTENTS);
        if (!stack.is(Items.TIPPED_ARROW) || potion == null || potion.potion().isEmpty()) return stack;
        var holder = potion.potion().get();
        if (!holder.is(ModPotions.REACH.getKey()) && !holder.is(ModPotions.LONG_REACH.getKey())) return stack;
        return stack.transmuteCopy(ModItems.TIPPED_ARROW.get());
    }
}
