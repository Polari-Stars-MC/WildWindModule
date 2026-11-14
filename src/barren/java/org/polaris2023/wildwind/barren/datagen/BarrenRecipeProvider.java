package org.polaris2023.wildwind.barren.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.registry.ModItems;

import java.util.concurrent.CompletableFuture;

/**
 * 瘠土更新的配方生成器。
 *
 * @author mdjfnfn
 */
public class BarrenRecipeProvider extends RecipeProvider {

    protected BarrenRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        // 硝化砂岩 -> 硝石（熔炉）
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(BarrenBlocks.NITRIFIED_SANDSTONE.get()),
                        RecipeCategory.MISC,
                        CookingBookCategory.MISC,
                        ModItems.NITER.get(),
                        0.1F,
                        200
                )
                .unlockedBy("has_nitrified_sandstone", this.has(BarrenBlocks.NITRIFIED_SANDSTONE.get()))
                .save(this.output);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new BarrenRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Wild Wind " + WildWindBarren.MOD_ID + " Recipe Provider";
        }
    }
}
