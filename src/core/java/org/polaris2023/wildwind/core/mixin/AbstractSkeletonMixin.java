package org.polaris2023.wildwind.core.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.polaris2023.wildwind.core.event.SkeletonBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin {
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void wildWind$spawnWithSword(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        var self = (AbstractSkeleton) (Object) this;
        // Spawn initialization only: loading a saved entity must never reroll its weapon.
        if (SkeletonBehavior.canSpawnWithSword(self) && random.nextFloat() < 0.1F) {
            self.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
    }
}
