package org.polaris2023.wildwind.hfas.recipe;

import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import org.jspecify.annotations.Nullable;

public final class WoolToStringRecipe extends CustomRecipe {
    public static final WoolToStringRecipe INSTANCE = new WoolToStringRecipe();
    private WoolToStringRecipe() {}

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 2) return false;
        boolean wool = false, shears = false;
        for (var stack : input.items()) {
            if (stack.isEmpty()) continue;
            if (!wool && stack.is(ItemTags.WOOL)) wool = true;
            else if (!shears && stack.is(Items.SHEARS) && !stack.isBroken()) shears = true;
            else return false;
        }
        return wool && shears;
    }

    @Override
    public ItemStack assemble(CraftingInput input) { return new ItemStack(Items.STRING, 2); }

    @Override
    public RecipeSerializer<WoolToStringRecipe> getSerializer() { return ModRecipes.WOOL_TO_STRING.get(); }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        var player = CommonHooks.getCraftingPlayer();
        return remainingItems(input, player != null && player.level() instanceof ServerLevel level ? level : null);
    }

    /** CrafterBlock also supplies its server level; recipe previews never roll durability RNG. */
    public NonNullList<ItemStack> remainingItems(CraftingInput input, @Nullable ServerLevel level) {
        var remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int slot = 0; slot < input.size(); slot++) {
            if (input.getItem(slot).is(Items.SHEARS)) {
                var shears = input.getItem(slot).copyWithCount(1);
                if (level != null) shears.hurtAndBreak(1, level, (LivingEntity) null, item -> {});
                remaining.set(slot, shears.isEmpty() ? ItemStack.EMPTY : shears);
            }
        }
        return remaining;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(List.of(new SlotDisplay.TagSlotDisplay(ItemTags.WOOL),
                new SlotDisplay.WithRemainder(new SlotDisplay.ItemSlotDisplay(Items.SHEARS), new SlotDisplay.ItemSlotDisplay(Items.SHEARS))),
                new SlotDisplay.ItemStackSlotDisplay(new ItemStackTemplate(Items.STRING, 2)),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}
