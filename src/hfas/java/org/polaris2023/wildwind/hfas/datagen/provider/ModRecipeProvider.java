package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.registry.ModItems;

import java.util.concurrent.CompletableFuture;

/**
 * 箭矢与木材配方数据生成器
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
        // ==================== 焚烬木配方 ====================
        buildWoodRecipes(
                "blaze",
                ModBlocks.BLAZE_LOG.get(),
                ModBlocks.BLAZE_WOOD.get(),
                ModBlocks.BLAZE_PLANKS.get(),
                ModBlocks.BLAZE_STAIRS.get(),
                ModBlocks.BLAZE_SLAB.get(),
                ModBlocks.BLAZE_FENCE.get(),
                ModBlocks.BLAZE_FENCE_GATE.get(),
                ModBlocks.BLAZE_DOOR.get(),
                ModBlocks.BLAZE_TRAPDOOR.get(),
                ModBlocks.BLAZE_PRESSURE_PLATE.get(),
                ModBlocks.BLAZE_BUTTON.get(),
                ModBlocks.BLAZE_SIGN_ITEM.get(),
                ModBlocks.BLAZE_HANGING_SIGN_ITEM.get(),
                ModBlocks.STRIPPED_BLAZE_LOG.get(),
                ModBlocks.BLAZE_BOAT.get(),
                ModBlocks.BLAZE_CHEST_BOAT.get()
        );

        // ==================== 灵焰木配方 ====================
        buildWoodRecipes(
                "soul",
                ModBlocks.SOUL_LOG.get(),
                ModBlocks.SOUL_WOOD.get(),
                ModBlocks.SOUL_PLANKS.get(),
                ModBlocks.SOUL_STAIRS.get(),
                ModBlocks.SOUL_SLAB.get(),
                ModBlocks.SOUL_FENCE.get(),
                ModBlocks.SOUL_FENCE_GATE.get(),
                ModBlocks.SOUL_DOOR.get(),
                ModBlocks.SOUL_TRAPDOOR.get(),
                ModBlocks.SOUL_PRESSURE_PLATE.get(),
                ModBlocks.SOUL_BUTTON.get(),
                ModBlocks.SOUL_SIGN_ITEM.get(),
                ModBlocks.SOUL_HANGING_SIGN_ITEM.get(),
                ModBlocks.STRIPPED_SOUL_LOG.get(),
                ModBlocks.SOUL_BOAT.get(),
                ModBlocks.SOUL_CHEST_BOAT.get()
        );

        // ==================== 杜鹃木配方 ====================
        buildWoodRecipes(
                "azalea",
                ModBlocks.AZALEA_LOG.get(),
                ModBlocks.AZALEA_WOOD.get(),
                ModBlocks.AZALEA_PLANKS.get(),
                ModBlocks.AZALEA_STAIRS.get(),
                ModBlocks.AZALEA_SLAB.get(),
                ModBlocks.AZALEA_FENCE.get(),
                ModBlocks.AZALEA_FENCE_GATE.get(),
                ModBlocks.AZALEA_DOOR.get(),
                ModBlocks.AZALEA_TRAPDOOR.get(),
                ModBlocks.AZALEA_PRESSURE_PLATE.get(),
                ModBlocks.AZALEA_BUTTON.get(),
                ModBlocks.AZALEA_SIGN_ITEM.get(),
                ModBlocks.AZALEA_HANGING_SIGN_ITEM.get(),
                ModBlocks.STRIPPED_AZALEA_LOG.get(),
                ModBlocks.AZALEA_BOAT.get(),
                ModBlocks.AZALEA_CHEST_BOAT.get()
        );

        // 生成所有箭矢组合配方
        generateArrowRecipes();

        // 万用蟹钳：铜锭x6 + 蟹钳x3
        ShapedRecipeBuilder.shaped(
                        this.items,
                        RecipeCategory.TOOLS,
                        ModItems.OMNI_CLAW.get()
                )
                .define('I', Items.COPPER_INGOT)
                .define('C', ModItems.CRAB_CLAW.get())
                .pattern("ICC")
                .pattern("IIC")
                .pattern("III")
                .unlockedBy(
                        "has_" + ModItems.CRAB_CLAW.getId().getPath(),
                        this.has(ModItems.OMNI_CLAW.get())
                )
                .save(this.output);
    }

    /**
     * 生成单套木材的完整配方。
     */
    private void buildWoodRecipes(
            String name,
            ItemLike log,
            ItemLike wood,
            ItemLike planks,
            ItemLike stairs,
            ItemLike slab,
            ItemLike fence,
            ItemLike fenceGate,
            ItemLike door,
            ItemLike trapdoor,
            ItemLike pressurePlate,
            ItemLike button,
            ItemLike sign,
            ItemLike hangingSign,
            ItemLike strippedLog,
            ItemLike boat,
            ItemLike chestBoat
    ) {
        // 原木 -> 木板
        shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .requires(log)
                .group("planks")
                .unlockedBy("has_" + name + "_log", has(log))
                .save(output);

        // 原木 -> 木
        shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
                .define('#', log)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_" + name + "_log", has(log))
                .save(output);

        // 楼梯
        stairBuilder(
                stairs,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 台阶
        slabBuilder(
                RecipeCategory.BUILDING_BLOCKS,
                slab,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 栅栏
        fenceBuilder(
                fence,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 栅栏门
        fenceGateBuilder(
                fenceGate,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 门
        doorBuilder(
                door,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 活板门
        trapdoorBuilder(
                trapdoor,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 压力板
        pressurePlateBuilder(
                RecipeCategory.REDSTONE,
                pressurePlate,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 按钮
        buttonBuilder(
                button,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 告示牌
        signBuilder(
                sign,
                net.minecraft.world.item.crafting.Ingredient.of(planks)
        )
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 悬挂告示牌
        hangingSignBuilder(
                hangingSign,
                net.minecraft.world.item.crafting.Ingredient.of(strippedLog)
        )
                .unlockedBy(
                        "has_" + name + "_stripped_log",
                        has(strippedLog)
                )
                .save(output);

        // 船
        shaped(RecipeCategory.TRANSPORTATION, boat)
                .define('#', planks)
                .pattern("# #")
                .pattern("###")
                .group("boat")
                .unlockedBy("has_" + name + "_planks", has(planks))
                .save(output);

        // 运输船
        shapeless(RecipeCategory.TRANSPORTATION, chestBoat)
                .requires(boat)
                .requires(Items.CHEST)
                .group("chest_boat")
                .unlockedBy("has_" + name + "_boat", has(boat))
                .save(output);
    }

    /**
     * 生成所有箭矢组合配方。
     *
     * 当前上游实现暂为空，保留方法入口用于之后继续实现箭头、箭杆等组合配方。
     */
    private void generateArrowRecipes() {

    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(
                PackOutput packOutput,
                CompletableFuture<HolderLookup.Provider> registries
        ) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(
                HolderLookup.Provider provider,
                RecipeOutput recipeOutput
        ) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Wild Wind " + HFASMod.MOD_ID + " Recipe Provider";
        }
    }
}