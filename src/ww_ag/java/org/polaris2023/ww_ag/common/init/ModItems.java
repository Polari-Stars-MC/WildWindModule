package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;

import java.util.function.Supplier;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
public class ModItems {
    public static final ItemEntry<Item> SALT;
    public static final ItemEntry<Item>
            BAKED_APPLE, NETHERITE_APPLE, ENCHANTED_NETHERITE_APPLE,
            BAKED_MELON_SLICE, PUMPKIN_SLICE, BAKED_PUMPKIN_SLICE,
            BAKED_MUSHROOM;

    static {
        {
            SALT = REGISTRATE
                    .item("salt", Item::new)
                    .defaultModel()
                    .tab(ModTabs.INGREDIENTS.key())
                    .recipe((c, p) -> {
                        DataIngredient items = DataIngredient.items(ModBlocks.SALT_BLOCK.get());
                        DataIngredient ores = DataIngredient.tag(WWItemTags.ORES$SALT.get());
                        p.storage(DataIngredient.items(c), RecipeCategory.MISC, c, items, ModBlocks.SALT_BLOCK);
                        p.square(items, RecipeCategory.MISC, c, false);
                        p.smelting(ores, RecipeCategory.MISC, c, 0.35F);
                        p.blasting(ores, RecipeCategory.MISC, c, 0.35F);
                    })
                    .lang("Salt")
                    .register();
        }
        //food
        {
            {
                BAKED_APPLE = baseFood("baked_apple", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(5)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {
                    DataIngredient items = DataIngredient.items(Items.APPLE);
                    p.smelting(items, RecipeCategory.FOOD, c, 0.35F);
                    p.smoking(items, RecipeCategory.FOOD, c, 0.35F);
                    p.campfire(items, RecipeCategory.FOOD, c, 0.35F);
                });
            }
            {
                NETHERITE_APPLE = baseFood("netherite_apple", p -> p
                        .rarity(Rarity.RARE)
                        .food(new FoodProperties.Builder()
                                .nutrition(5)
                                .saturationModifier(1.2F)
                                .build()), (c, p) -> {

                });
            }
            {
                ENCHANTED_NETHERITE_APPLE = parentFood("enchanted_netherite_apple", p -> p
                        .rarity(Rarity.EPIC)
                        .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                        .food(new FoodProperties.Builder()
                                .nutrition(5)
                                .saturationModifier(1.2F)
                                .build()), NETHERITE_APPLE, (c, p) -> {

                });
            }
            {
                BAKED_MELON_SLICE = baseFood("baked_melon_slice", p ->
                        p.food(new FoodProperties.Builder()
                                        .nutrition(4)
                                        .saturationModifier(0.3F)
                                .build()), (c, p) -> {
                    DataIngredient items = DataIngredient.items(Items.MELON_SLICE);
                    p.smelting(items, RecipeCategory.FOOD, c, 0.35F);
                    p.smoking(items, RecipeCategory.FOOD, c, 0.35F);
                    p.campfire(items, RecipeCategory.FOOD, c, 0.35F);
                });
            }
            {
                PUMPKIN_SLICE = baseFood("pumpkin_slice", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {

                });
                BAKED_PUMPKIN_SLICE = baseFood("baked_pumpkin_slice", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {
                    DataIngredient items = DataIngredient.items(ModItems.PUMPKIN_SLICE.get());
                    p.smelting(items, RecipeCategory.FOOD, c, 0.35F);
                    p.smoking(items, RecipeCategory.FOOD, c, 0.35F);
                    p.campfire(items, RecipeCategory.FOOD, c, 0.35F);
                });
            }
            {
                BAKED_MUSHROOM = baseFood("baked_mushroom", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(0.6F)
                                .build()), (c, p) -> {
                    DataIngredient mushrooms = DataIngredient.tag(Tags.Items.MUSHROOMS);
                    DataIngredient fungus = DataIngredient.tag(WWItemTags.FUNGUS.get());
                    p.smelting(mushrooms, RecipeCategory.FOOD, c, 0.35F);
                    p.smelting(fungus, RecipeCategory.FOOD, c, 0.35F);
                    p.smoking(mushrooms, RecipeCategory.FOOD, c, 0.35F);
                    p.smoking(fungus, RecipeCategory.FOOD, c, 0.35F);
                    p.campfire(mushrooms, RecipeCategory.FOOD, c, 0.35F);
                    p.campfire(fungus, RecipeCategory.FOOD, c, 0.35F);
                });
            }
        }
    }

    public static ItemEntry<Item> baseFood(String name, NonNullUnaryOperator<Item.Properties> properties, NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe) {
        return REGISTRATE
                .item(name, Item::new)
                .defaultModel()
                .recipe(recipe)
                .lang(name.substring(0, 1).toUpperCase() + name.substring(1).replace("_", " "))
                .properties(properties)
                .tab(ModTabs.FOOD_AND_DRINK.key())
                .register();
    }
    public static ItemEntry<Item> parentVanillaFood(String name, NonNullUnaryOperator<Item.Properties> properties, Item vanilla, NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe) {
        return REGISTRATE
                .item(name, Item::new)
                .model((c, p) -> {
                    p.withExistingParent(c.getId().getPath(), BuiltInRegistries.ITEM.getKey(vanilla).withPrefix("item/"));
                })
                .recipe(recipe)
                .lang(name.substring(0, 1).toUpperCase() + name.substring(1).replace("_", " "))
                .properties(properties)
                .tab(ModTabs.FOOD_AND_DRINK.key())
                .register();
    }
    public static ItemEntry<Item> parentFood(String name, NonNullUnaryOperator<Item.Properties> properties, ItemEntry<?> entry, NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe) {
        return REGISTRATE
                .item(name, Item::new)
                .model((c, p) -> {
                    p.withExistingParent(c.getId().getPath(), entry.getId().withPrefix("item/"));
                })
                .recipe(recipe)
                .lang(name.substring(0, 1).toUpperCase() + name.substring(1).replace("_", " "))
                .properties(properties)
                .tab(ModTabs.FOOD_AND_DRINK.key())
                .register();
    }



    public static void register() {}
}
