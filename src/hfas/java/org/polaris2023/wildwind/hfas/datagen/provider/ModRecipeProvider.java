package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        buildWoodRecipes(
                "blaze",
                ModBlocks.BLAZE_LOG.get(), ModBlocks.BLAZE_WOOD.get(),
                ModBlocks.BLAZE_PLANKS.get(), ModBlocks.BLAZE_STAIRS.get(), ModBlocks.BLAZE_SLAB.get(),
                ModBlocks.BLAZE_FENCE.get(), ModBlocks.BLAZE_FENCE_GATE.get(),
                ModBlocks.BLAZE_DOOR.get(), ModBlocks.BLAZE_TRAPDOOR.get(),
                ModBlocks.BLAZE_PRESSURE_PLATE.get(), ModBlocks.BLAZE_BUTTON.get(),
                ModBlocks.BLAZE_SIGN_ITEM.get(), ModBlocks.BLAZE_HANGING_SIGN_ITEM.get(),
                ModBlocks.STRIPPED_BLAZE_LOG.get(),
                ModBlocks.BLAZE_BOAT.get(), ModBlocks.BLAZE_CHEST_BOAT.get()
        );
        buildWoodRecipes(
                "soul",
                ModBlocks.SOUL_LOG.get(), ModBlocks.SOUL_WOOD.get(),
                ModBlocks.SOUL_PLANKS.get(), ModBlocks.SOUL_STAIRS.get(), ModBlocks.SOUL_SLAB.get(),
                ModBlocks.SOUL_FENCE.get(), ModBlocks.SOUL_FENCE_GATE.get(),
                ModBlocks.SOUL_DOOR.get(), ModBlocks.SOUL_TRAPDOOR.get(),
                ModBlocks.SOUL_PRESSURE_PLATE.get(), ModBlocks.SOUL_BUTTON.get(),
                ModBlocks.SOUL_SIGN_ITEM.get(), ModBlocks.SOUL_HANGING_SIGN_ITEM.get(),
                ModBlocks.STRIPPED_SOUL_LOG.get(),
                ModBlocks.SOUL_BOAT.get(), ModBlocks.SOUL_CHEST_BOAT.get()
        );
        buildWoodRecipes(
                "azalea",
                ModBlocks.AZALEA_LOG.get(), ModBlocks.AZALEA_WOOD.get(),
                ModBlocks.AZALEA_PLANKS.get(), ModBlocks.AZALEA_STAIRS.get(), ModBlocks.AZALEA_SLAB.get(),
                ModBlocks.AZALEA_FENCE.get(), ModBlocks.AZALEA_FENCE_GATE.get(),
                ModBlocks.AZALEA_DOOR.get(), ModBlocks.AZALEA_TRAPDOOR.get(),
                ModBlocks.AZALEA_PRESSURE_PLATE.get(), ModBlocks.AZALEA_BUTTON.get(),
                ModBlocks.AZALEA_SIGN_ITEM.get(), ModBlocks.AZALEA_HANGING_SIGN_ITEM.get(),
                ModBlocks.STRIPPED_AZALEA_LOG.get(),
                ModBlocks.AZALEA_BOAT.get(), ModBlocks.AZALEA_CHEST_BOAT.get()
        );
    }

    private void buildWoodRecipes(
            String name,
            ItemLike log, ItemLike wood,
            ItemLike planks, ItemLike stairs, ItemLike slab,
            ItemLike fence, ItemLike fenceGate,
            ItemLike door, ItemLike trapdoor,
            ItemLike pressurePlate, ItemLike button,
            ItemLike sign, ItemLike hangingSign,
            ItemLike strippedLog,
            ItemLike boat, ItemLike chestBoat
    ) {
        shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .requires(log)
                .group("planks")
                .unlockedBy("has_" + name + "_log", has(log))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
                .define('#', log)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_" + name + "_log", has(log))
                .save(output);

        stairBuilder(stairs, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        fenceBuilder(fence, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        fenceGateBuilder(fenceGate, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        doorBuilder(door, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        trapdoorBuilder(trapdoor, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        pressurePlateBuilder(RecipeCategory.REDSTONE, pressurePlate, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        buttonBuilder(button, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        signBuilder(sign, net.minecraft.world.item.crafting.Ingredient.of(planks))
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        hangingSignBuilder(hangingSign, net.minecraft.world.item.crafting.Ingredient.of(strippedLog))
                .unlockedBy("has_" + name + "_stripped_log", has(strippedLog))
                .save(output);

        shaped(RecipeCategory.TRANSPORTATION, boat)
                .define('#', planks)
                .pattern("# #")
                .pattern("###")
                .group("boat")
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        shapeless(RecipeCategory.TRANSPORTATION, chestBoat)
                .requires(boat)
                .requires(Items.CHEST)
                .group("chest_boat")
                .unlockedBy("has_" + name + "_boat", has(boat))
                .save(output);
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
