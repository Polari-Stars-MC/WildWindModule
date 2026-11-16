package org.polaris2023.wildwind.barren.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
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
        // 硝化砂岩 / 硝化红砂岩 -> 硝石（熔炉）
        smeltNitrifiedSandstone(BarrenBlocks.NITRIFIED_SANDSTONE.get(), "nitrified_sandstone");
        smeltNitrifiedSandstone(BarrenBlocks.NITRIFIED_RED_SANDSTONE.get(), "nitrified_red_sandstone");
    }

    private void smeltNitrifiedSandstone(Block block, String name) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(block),
                        RecipeCategory.MISC,
                        CookingBookCategory.MISC,
                        ModItems.NITER.get(),
                        0.1F,
                        200
                )
                .unlockedBy("has_" + name, this.has(block))
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
