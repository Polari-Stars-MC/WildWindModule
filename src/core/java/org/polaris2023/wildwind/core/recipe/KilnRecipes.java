package org.polaris2023.wildwind.core.recipe;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.polaris2023.wildwind.core.CoreMod;

/** Reuses the current world's smelting recipes, including data-pack recipes and their experience. */
public final class KilnRecipes {
    public static final int COOK_TIME = 100;
    public static final TagKey<Item> EXCLUDED = TagKey.create(Registries.ITEM, CoreMod.id("kiln_excluded"));
    private static final TagKey<Item> METAL_OUTPUTS = TagKey.create(Registries.ITEM, CoreMod.id("kiln_metal_outputs"));

    private KilnRecipes() {}

    public static Optional<RecipeHolder<SmeltingRecipe>> find(Level level, ItemStack stack) {
        if (!(level instanceof ServerLevel server) || stack.isEmpty() || stack.is(EXCLUDED)) return Optional.empty();
        var input = new SingleRecipeInput(stack);
        // Blasting covers metal equipment recycling as well as ore/raw-metal processing.
        if (server.recipeAccess().getRecipeFor(RecipeType.BLASTING, input, server).isPresent()) return Optional.empty();
        var recipe = server.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, server);
        if (recipe.isPresent() && stack.isDamageableItem()
                && recipe.get().value().assemble(input).is(METAL_OUTPUTS)) {
            return Optional.empty();
        }
        return recipe;
    }
}
