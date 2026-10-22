package org.polaris2023.wildwind.hfas.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.polaris2023.wildwind.hfas.recipe.WoolToStringRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrafterBlock.class)
abstract class CrafterBlockMixin {
    @WrapOperation(method = "dispenseFrom", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;getRemainingItems(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/core/NonNullList;"))
    private NonNullList<ItemStack> wildwind$damageShears(CraftingRecipe recipe, CraftingInput input,
            Operation<NonNullList<ItemStack>> original, BlockState state, ServerLevel level, BlockPos pos) {
        return recipe instanceof WoolToStringRecipe wool ? wool.remainingItems(input, level) : original.call(recipe, input);
    }
}
