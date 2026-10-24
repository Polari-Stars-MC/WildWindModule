package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

/**
 * Generates each release module's wood and functional recipes.
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModRecipeProvider extends RecipeProvider {
    private final ReleaseModule module;


    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output, ReleaseModule module) {
        super(registries, output);
        this.module = module;
    }

    @Override
    protected void buildRecipes() {
        if (module == ReleaseModule.CORE) {
            woodRecipes("azalea");
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
        }
        if (module == ReleaseModule.WILDERNESS) {
            woodRecipes("blaze");
            woodRecipes("soul");
            // 生恐鱼 -> 熟恐鱼（熔炉）
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
    }


    /** Shared wood recipes keep the existing ingredients, output counts and recipe IDs. */
    private void woodRecipes(String name) {
        var log = block(name + "_log");
        var planks = block(name + "_planks");
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .requires(log).group("planks").unlockedBy("has_" + name + "_log", has(log)).save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.REDSTONE, block(name + "_button"))
                .requires(planks).unlockedBy("has_" + name + "_planks", has(planks)).save(output);
        woodShape(name, "wood", 3, RecipeCategory.BUILDING_BLOCKS, log, "##", "##");
        woodShape(name, "slab", 6, RecipeCategory.BUILDING_BLOCKS, planks, "###");
        woodShape(name, "stairs", 4, RecipeCategory.BUILDING_BLOCKS, planks, "#  ", "## ", "###");
        woodShape(name, "door", 3, RecipeCategory.REDSTONE, planks, "##", "##", "##");
        woodShape(name, "trapdoor", 2, RecipeCategory.REDSTONE, planks, "###", "###");
        woodShape(name, "pressure_plate", 1, RecipeCategory.REDSTONE, planks, "##");
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, block(name + "_fence"), 3)
                .define('#', Items.STICK).define('W', planks).pattern("W#W").pattern("W#W")
                .unlockedBy("has_" + name + "_planks", has(planks)).save(output);
        ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, block(name + "_fence_gate"))
                .define('#', Items.STICK).define('W', planks).pattern("#W#").pattern("#W#")
                .unlockedBy("has_" + name + "_planks", has(planks)).save(output);
    }

    private void woodShape(String wood, String suffix, int count, RecipeCategory category, Block ingredient, String... pattern) {
        var recipe = ShapedRecipeBuilder.shaped(items, category, block(wood + "_" + suffix), count).define('#', ingredient);
        for (var row : pattern) recipe.pattern(row);
        recipe.unlockedBy("has_" + BuiltInRegistries.BLOCK.getKey(ingredient).getPath(), has(ingredient)).save(output);
    }

    private Block block(String path) {
        var id = Identifier.fromNamespaceAndPath(module.id(), path);
        return BuiltInRegistries.BLOCK.getOptional(id).orElseThrow(() -> new IllegalStateException("Missing wood block " + id));
    }

    public static class Runner extends RecipeProvider.Runner {
        private final ReleaseModule module;

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, ReleaseModule module) {
            super(packOutput, registries);
            this.module = module;
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput, module);
        }

        @Override
        public String getName() {
            return "Wild Wind " + module.id() + " Recipe Provider";
        }
    }


}
