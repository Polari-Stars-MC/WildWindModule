package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.arrow.ArrowFletching;
import org.polaris2023.wildwind.hfas.arrow.ArrowHead;
import org.polaris2023.wildwind.hfas.arrow.ArrowShaft;
import org.polaris2023.wildwind.hfas.component.ArrowComponent;

import java.util.concurrent.CompletableFuture;

/**
 * 箭矢配方数据生成器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModRecipeProvider extends RecipeProvider {


    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        // 生成所有箭矢组合配方
        generateArrowRecipes();
    }

    private void generateArrowRecipes() {

    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Wild Wind HFAS " + HFASMod.MOD_ID + " Recipe Provider";
        }
    }


}
