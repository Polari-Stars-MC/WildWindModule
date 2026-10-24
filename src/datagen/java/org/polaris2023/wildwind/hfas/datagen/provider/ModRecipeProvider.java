package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.polaris2023.wildwind.hfas.HFASMod;

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
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, org.polaris2023.wildwind.core.block.ModBlocks.KILN.get())
                .define('B', Items.BRICK).define('F', Items.FURNACE).define('M', Items.PACKED_MUD)
                .pattern(" B ").pattern("BFB").pattern("MMM")
                .unlockedBy("has_furnace", this.has(Items.FURNACE)).save(this.output);
        var planks = org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS.get();
        var log = org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG.get();
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get(), 3)
                .define('P', planks).define('S', Items.STICK).pattern("PPP").pattern("PPP").pattern(" S ")
                .unlockedBy("has_azalea_planks", this.has(planks)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get(), 6)
                .define('L', log).define('C', Items.IRON_CHAIN).pattern("C C").pattern("LLL").pattern("LLL")
                .unlockedBy("has_stripped_azalea_log", this.has(log)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get(), 6)
                .define('L', log).pattern("LLL").pattern("   ").pattern("LLL")
                .unlockedBy("has_stripped_azalea_log", this.has(log)).save(this.output);
        // 生成所有箭矢组合配方
        // 生食人鱼 -> 熟食人鱼（熔炉）
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.get()), RecipeCategory.FOOD, CookingBookCategory.FOOD, org.polaris2023.wildwind.wilderness.registry.ModItems.COOKED_PIRANHA.get(), 0.35F, 200)
                .unlockedBy("has_" + org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.getId().getPath(), this.has(org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.get()))
                .save(this.output);
        // 万用蟹钳：铜锭x6 + 蟹钳x3
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, org.polaris2023.wildwind.wilderness.registry.ModItems.OMNI_CLAW.get())
                .define('I', Items.COPPER_INGOT)
                .define('C', org.polaris2023.wildwind.wilderness.registry.ModItems.CRAB_CLAW.get())
                .pattern("ICC")
                .pattern("IIC")
                .pattern("III")
                .unlockedBy("has_" + org.polaris2023.wildwind.wilderness.registry.ModItems.CRAB_CLAW.getId().getPath(), this.has(org.polaris2023.wildwind.wilderness.registry.ModItems.OMNI_CLAW.get()))
                .save(this.output);
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
            return "Wild Wind " + HFASMod.MOD_ID + " Recipe Provider";
        }
    }


}
