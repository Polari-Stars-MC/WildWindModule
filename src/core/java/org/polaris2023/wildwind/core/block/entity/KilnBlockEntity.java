package org.polaris2023.wildwind.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.polaris2023.wildwind.core.menu.KilnMenu;
import org.polaris2023.wildwind.core.recipe.KilnRecipes;
import org.polaris2023.wildwind.core.mixin.FurnaceFuelAccessor;

/** Vanilla inventory, sided transfer, persistence, locking and experience accounting; kiln-specific cooking. */
public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KILN.get(), pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() { return Component.translatable("block.wild_wind_core.kiln"); }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new KilnMenu(id, inventory, this, dataAccess);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == SLOT_INPUT) return level != null && KilnRecipes.find(level, stack).isPresent();
        return super.canPlaceItem(slot, stack);
    }

    public static void tick(ServerLevel level, BlockPos pos, BlockState state, KilnBlockEntity kiln) {
        var data = kiln.dataAccess;
        int rawRemaining = ((FurnaceFuelAccessor) kiln).wildwind$remainingFuel();
        boolean wasLit = rawRemaining > 0;
        // Match the furnace's fuel countdown; fuel durations are unchanged (coal lasts 1600 ticks).
        int remaining = Math.max(0, rawRemaining - 1);
        data.set(DATA_LIT_TIME, remaining);
        data.set(DATA_COOKING_TOTAL_TIME, KilnRecipes.COOK_TIME);
        var input = kiln.items.get(SLOT_INPUT);
        var fuel = kiln.items.get(SLOT_FUEL);
        var recipe = KilnRecipes.find(level, input).orElse(null);
        var result = recipe == null ? ItemStack.EMPTY : recipe.value().assemble(new SingleRecipeInput(input));
        boolean canCook = !result.isEmpty() && kiln.canAcceptResult(result);
        boolean changed = wasLit;
        if (remaining == 0 && canCook && !fuel.isEmpty()) {
            remaining = kiln.getBurnDuration(level.fuelValues(), fuel);
            data.set(DATA_LIT_TIME, remaining);
            data.set(DATA_LIT_DURATION, remaining);
            if (remaining > 0) {
                var remainder = fuel.getCraftingRemainder();
                fuel.shrink(1);
                if (fuel.isEmpty()) kiln.items.set(SLOT_FUEL, remainder == null ? ItemStack.EMPTY : remainder.create());
                changed = true;
            }
        }
        int progress = data.get(DATA_COOKING_PROGRESS);
        if (remaining > 0 && canCook) {
            progress++;
            if (progress >= KilnRecipes.COOK_TIME) {
                progress = 0;
                var output = kiln.items.get(SLOT_RESULT);
                if (output.isEmpty()) kiln.items.set(SLOT_RESULT, result.copy());
                else output.grow(result.getCount());
                if (input.is(Items.WET_SPONGE) && kiln.items.get(SLOT_FUEL).is(Items.BUCKET)) {
                    kiln.items.set(SLOT_FUEL, new ItemStack(Items.WATER_BUCKET));
                }
                input.shrink(1);
                kiln.setRecipeUsed(recipe);
            }
            changed = true;
        } else if (!canCook) {
            changed |= progress != 0;
            progress = 0;
        } else if (progress > 0) {
            progress = Math.max(0, progress - BURN_COOL_SPEED);
            changed = true;
        }
        data.set(DATA_COOKING_PROGRESS, progress);
        boolean isLit = remaining > 0;
        if (state.getValue(AbstractFurnaceBlock.LIT) != isLit) {
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, isLit), 3);
            changed = true;
        }
        if (changed) kiln.setChanged();
    }

    private boolean canAcceptResult(ItemStack result) {
        var output = items.get(SLOT_RESULT);
        int limit = Math.min(getMaxStackSize(), result.getMaxStackSize());
        return output.isEmpty() ? result.getCount() <= limit
                : ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= limit;
    }
}
