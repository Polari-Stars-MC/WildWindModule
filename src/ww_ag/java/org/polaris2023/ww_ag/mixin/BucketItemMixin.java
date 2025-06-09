package org.polaris2023.ww_ag.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.polaris2023.ww_ag.common.init.ModDataComponents;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author baka4n
 * @code @Date 2025/6/9 13:33:21
 */
@Debug(export = true)
@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Inject(method = "use",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResultHolder;sidedSuccess(Ljava/lang/Object;Z)Lnet/minecraft/world/InteractionResultHolder;", ordinal = 0))
    private void onUse(
            Level level,
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
            @Local(ordinal = 2) ItemStack milk_stack,
            @Local(ordinal = 0) BlockState state
    ) {
        if (milk_stack.is(Items.MILK_BUCKET)) {
            if (state.is(WWBlockTags.MILK.get())) {
                milk_stack.set(ModDataComponents.MILK_TYPE, BuiltInRegistries.BLOCK.getKey(state.getBlock()));
            }
        }
    }
}
