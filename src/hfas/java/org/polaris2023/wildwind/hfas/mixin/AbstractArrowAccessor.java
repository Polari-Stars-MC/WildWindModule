package org.polaris2023.wildwind.hfas.mixin;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractArrow.class)
public interface AbstractArrowAccessor {
    @org.spongepowered.asm.mixin.gen.Invoker("setPierceLevel")
    void wildwind$setPiercing(byte level);

    @Accessor("firedFromWeapon")
    void wildwind$setWeapon(ItemStack weapon);
}
