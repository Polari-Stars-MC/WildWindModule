package org.polaris2023.wildwind.hfas.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceFuelAccessor {
    // ContainerData deliberately scales long burn times to fit network shorts; ticking needs the raw value.
    @Accessor("litTimeRemaining")
    int wildwind$remainingFuel();
}
