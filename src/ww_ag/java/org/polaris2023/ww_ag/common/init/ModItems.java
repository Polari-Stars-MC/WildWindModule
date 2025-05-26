package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;
import org.polaris2023.ww_ag.common.items.CheeseItem;
import org.polaris2023.ww_ag.common.items.LivingTuberItem;
import org.polaris2023.ww_ag.common.registrate.build.ItemBuilder;

import java.util.function.Consumer;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
@SuppressWarnings("unused")
public class ModItems {
    public static final ItemEntry<Item> CANDY, SALT;
    public static final ItemEntry<LivingTuberItem> LIVING_TUBER;
    public static final ItemEntry<CheeseItem> CHEESE;
    public static final ItemEntry<Item>
            BAKED_APPLE,
            BAKED_BEETROOT, BAKED_BERRIES,
            BAKED_CARROT,
            BAKED_LIVING_TUBER,
            BAKED_MELON_SLICE, BAKED_MUSHROOM,
            BAKED_PUMPKIN_SLICE,
            BAKED_SEEDS;
    public static final ItemEntry<Item>
            COOKED_BAT_WING,
            COOKED_CALAMARI,
            COOKED_EGG,
            COOKED_FROG_LEG,
            COOKED_PIRANHA,
            COOKED_TROUT,
            COOKED_VENISON;
    public static final ItemEntry<Item>
            ENCHANTED_NETHERITE_APPLE_PIE, NETHERITE_APPLE_PIE,
            ENCHANTED_GOLDEN_APPLE_PIE, GOLDEN_APPLE_PIE,
            APPLE_PIE, BEERY_PIE;
    public static final ItemEntry<Item> GLOWING_CALAMARI;
    public static final ItemEntry<Item>
            NETHERITE_APPLE, ENCHANTED_NETHERITE_APPLE,
            PUMPKIN_SLICE,
            VENISON, BAT_WING, FROG_LEG,
            CALAMARI, TROUT, PIRANHA, DOUGH,
            FAILED_CUISINE, CHARRED_CUISINE;

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
                        p.smeltingAndBlasting(ores, RecipeCategory.MISC, c, 0.35F);
                    })
                    .lang("Salt")
                    .register();
        }//salt
        {
            CANDY = REGISTRATE
                    .item("candy", Item::new)
                    .zh_cn("糖果")
                    .zh_tw("糖果")
                    .zh_hk("糖果")
                    .lang("Candy")
                    .properties(properties -> properties
                            .stacksTo(16))
                    .recipe((c ,p) -> {
                        DataIngredient apple = DataIngredient.items(Items.APPLE);
                        DataIngredient glow = DataIngredient.items(Items.GLOW_BERRIES);
                        DataIngredient sweet = DataIngredient.items(Items.SWEET_BERRIES);
                        ShapelessRecipeBuilder
                                .shapeless(RecipeCategory.FOOD, c.get())
                                .unlockedBy("has_" + p.safeName(apple), apple.getCriterion(p))
                                .unlockedBy("has_" + p.safeName(glow), glow.getCriterion(p))
                                .unlockedBy("has_" + p.safeName(sweet), sweet.getCriterion(p))
                                .requires(glow.toVanilla())
                                .requires(sweet.toVanilla(), 2)
                                .requires(apple.toVanilla())
                                .save(p, p.safeId(c.get()));

                    })
                    .defaultModel()
                    .tab(ModTabs.FOOD_AND_DRINK.key())
                    .register();
        }//candy
        //food
        {
            FoodProperties TROUT_FOOD = new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationModifier(.1F)
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .3F)
                    .build();
            FoodProperties COOKED_TROUT_FOOD = new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(.8F)
                    .build();
            {
                BAKED_APPLE = baseFood("baked_apple", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(5)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {
                    DataIngredient items = DataIngredient.items(Items.APPLE);
                    p.food(items, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("烤苹果");
                    b.zh_tw("烤蘋果");
                    b.zh_hk("烤蘋果");
                });
            }//Baked apple
            {
                NETHERITE_APPLE = baseFood("netherite_apple", p -> p
                        .rarity(Rarity.RARE)
                        .food(new FoodProperties.Builder()
                                .nutrition(5)
                                .saturationModifier(1.2F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.zh_cn("下界合金苹果");
                    b.zh_tw("下界合金蘋果");
                    b.zh_hk("下界合金蘋果");
                });
            }//netherite apple
            {
                ENCHANTED_NETHERITE_APPLE = parentFood("enchanted_netherite_apple", p -> p
                        .rarity(Rarity.EPIC)
                        .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                        .food(new FoodProperties.Builder()
                                .nutrition(5)
                                .saturationModifier(1.2F)
                                .build()), NETHERITE_APPLE, (c, p) -> {

                }, b -> {
                    b.zh_cn("附魔下界合金苹果");
                    b.zh_tw("附魔下界合金蘋果");
                    b.zh_hk("附魔下界合金蘋果");
                });
            }//enchanted netherite apple
            {
                BAKED_MELON_SLICE = baseFood("baked_melon_slice", p ->
                        p.food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {
                    DataIngredient items = DataIngredient.items(Items.MELON_SLICE);
                    p.food(items, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("烤西瓜片");
                    b.zh_tw("烤西瓜片");
                    b.zh_hk("烤西瓜片");
                });
            }//baked melon slice
            {
                PUMPKIN_SLICE = baseFood("pumpkin_slice", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.zh_cn("南瓜片");
                    b.zh_tw("南瓜片");
                    b.zh_hk("南瓜片");
                });
                BAKED_PUMPKIN_SLICE = baseFood("baked_pumpkin_slice", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {
                    DataIngredient items = DataIngredient.items(ModItems.PUMPKIN_SLICE.get());
                    p.food(items, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("烤南瓜片");
                    b.zh_tw("烤南瓜片");
                    b.zh_hk("烤南瓜片");
                });
            }//(baked/) pumpkin slice
            {
                BAKED_MUSHROOM = baseFood("baked_mushroom", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(0.6F)
                                .build()), (c, p) -> {
                    DataIngredient[] ingredients = new DataIngredient[]{
                            DataIngredient.tag(Tags.Items.MUSHROOMS),
                            DataIngredient.tag(WWItemTags.FUNGUS.get())
                    };
                    for (DataIngredient ingredient : ingredients) {
                        p.food(ingredient, RecipeCategory.FOOD, c, 0.35F);
                    }
                }, b -> {
                    b.zh_cn("烤蘑菇");
                    b.zh_tw("烤蘑菇");
                    b.zh_hk("烤蘑菇");
                });
            }//baked mushroom
            {
                BAKED_SEEDS = baseFood("baked_seeds", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(.1F)
                                .build()), (c, p) -> {
                    DataIngredient[] ingredients = new DataIngredient[]{
                            DataIngredient.tag(Tags.Items.SEEDS_WHEAT),
                            DataIngredient.tag(Tags.Items.SEEDS_PUMPKIN),
                            DataIngredient.tag(Tags.Items.SEEDS_MELON),
                            DataIngredient.tag(Tags.Items.SEEDS_BEETROOT),
                            DataIngredient.tag(Tags.Items.SEEDS_TORCHFLOWER),
                            DataIngredient.items(Items.PITCHER_POD),
                    };
                    for (DataIngredient ingredient : ingredients) {
                        p.food(ingredient, RecipeCategory.FOOD, c, 0.35F);
                    }
                }, b -> {
                    b.zh_cn("烤种子");
                    b.zh_tw("烤種子");
                    b.zh_hk("烤種子");
                });
            }//baked seeds
            {
                BAKED_BERRIES = baseFood("baked_berries", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(3)
                                .saturationModifier(.1F)
                                .build()), (c, p) -> {
                    DataIngredient berry = DataIngredient.tag(Tags.Items.FOODS_BERRY);
                    p.food(berry, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("烤浆果");
                    b.zh_tw("烤漿果");
                    b.zh_hk("烤漿果");
                });
            }//baked berries
            {
                BAKED_CARROT = baseFood("baked_carrot", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(7)
                                .saturationModifier(.6F)
                                .build()), (c, p) -> {
                    DataIngredient carrot = DataIngredient.tag(Tags.Items.CROPS_CARROT);
                    p.food(carrot, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("烤胡萝卜");
                    b.zh_tw("烤胡蘿蔔");
                    b.zh_hk("烤胡蘿蔔");
                });
            }//baked carrot
            {
                BAKED_BEETROOT = baseFood("baked_beetroot", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(6)
                                .saturationModifier(.6F)
                                .build()), (c, p) -> {
                    DataIngredient beetroot = DataIngredient.tag(Tags.Items.CROPS_BEETROOT);
                    p.food(beetroot, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("烤甜菜");
                    b.zh_tw("烤甜菜");
                    b.zh_hk("烤甜菜");
                });
            }//baked beetroot
            {
                LIVING_TUBER = REGISTRATE
                        .item("living_tuber", LivingTuberItem::new)
                        .zh_cn("活根")
                        .zh_tw("活根")
                        .zh_hk("活根")
                        .defaultModel()
                        .lang("Living Tuber")
                        .properties(p -> p
                                .stacksTo(16)
                                .food(new FoodProperties.Builder()
                                        .nutrition(4)
                                        .saturationModifier(.1F)
                                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 6), 1)
                                        .effect(() -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 3), 1)
                                        .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 600, 0), 1)
                                        .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 600, 0), 1)
                                        .build()))
                        .register();
                BAKED_LIVING_TUBER = baseFood("baked_living_tuber", p -> p
                        .stacksTo(16)
                        .food(new FoodProperties.Builder()
                                .nutrition(8)
                                .saturationModifier(.1F)
                                .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 6), 1)
                                .effect(() -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 3), 1)
                                .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 600, 0), 1)
                                .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 600, 0), 1)
                                .build()), (c, p) -> {
                    DataIngredient data = DataIngredient.items(LIVING_TUBER.get());
                    p.food(data, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("烤活根");
                    b.zh_tw("烤活根");
                    b.zh_hk("烤活根");
                });
            }//(baked/)living tuber
            {
                VENISON = baseFood("venison", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(3)
                                .saturationModifier(.3F)
                                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
                                .build()
                        ), (c, p) -> {

                }, b -> {
                    b.zh_cn("鹿排");
                    b.zh_tw("鹿排");
                    b.zh_hk("鹿排");
                });
                COOKED_VENISON = baseFood("cooked_venison", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(8)
                                .saturationModifier(.8F)
                                .build()
                        ), (c, p) -> {
                    DataIngredient venison = DataIngredient.items(VENISON.get());
                    p.food(venison, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("熟鹿排");
                    b.zh_tw("熟鹿排");
                    b.zh_hk("熟鹿排");
                });
            }//(baked/)venison
            {
                BAT_WING = baseFood("bat_wing", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(.1F)
                                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .3F)
                                .build()), (c, p) -> {
                }, b -> {
                    b.zh_cn("生蝙蝠翼");
                    b.zh_tw("生蝙蝠翼");
                    b.zh_hk("生蝙蝠翼");
                });
                COOKED_BAT_WING = baseFood("cooked_bat_wing", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(.8F)
                                .build()), (c, p) -> {
                    DataIngredient batWing = DataIngredient.items(BAT_WING.get());
                    p.food(batWing, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("熟蝙蝠翼");
                    b.zh_tw("熟蝙蝠翼");
                    b.zh_hk("熟蝙蝠翼");
                });
            }//(cooked/)bat wing
            {
                FROG_LEG = baseFood("frog_leg", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(1)
                                .saturationModifier(.8F)
                                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .3F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.zh_cn("生蛙腿");
                    b.zh_tw("生蛙腿");
                    b.zh_hk("生蛙腿");
                });
                COOKED_FROG_LEG = baseFood("cooked_frog_leg", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(.8F)
                                .build()), (c, p) -> {
                    DataIngredient frogLeg = DataIngredient.items(FROG_LEG.get());
                    p.food(frogLeg, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("熟蛙腿");
                    b.zh_tw("熟蛙腿");
                    b.zh_hk("熟蛙腿");
                });

            }//(cooked/)frog.leg
            {
                CALAMARI = baseFood("calamari", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(.1F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.zh_cn("生鱿鱼须");
                    b.zh_tw("生魷魚須");
                    b.zh_hk("生魷魚須");
                });
                GLOWING_CALAMARI = baseFood("glowing_calamari", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(.1F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.zh_cn("发光鱿鱼须");
                    b.zh_tw("發光魷魚須");
                    b.zh_hk("發光魷魚須");
                });
                COOKED_CALAMARI = baseFood("cooked_calamari", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(.8F)
                                .build()), (c, p) -> {
                    DataIngredient[] items = new DataIngredient[]{
                            DataIngredient.items(CALAMARI.get()),
                            DataIngredient.items(GLOWING_CALAMARI.get())
                    };
                    for (DataIngredient item : items) {
                        p.food(item, RecipeCategory.FOOD, c, 0.35F);
                    }
                }, b -> {
                    b.zh_cn("熟鱿鱼须");
                    b.zh_cn("熟魷魚須");
                    b.zh_cn("熟魷魚須");
                });

            }//(cooked/glowing/)calamari
            {
                TROUT = baseFood("trout", p -> p
                        .food(TROUT_FOOD), (c, p) -> {

                }, b -> {
                    b.zh_cn("生鳟鱼");
                    b.zh_tw("生鱒魚");
                    b.zh_hk("生鱒魚");
                });
                COOKED_TROUT = baseFood("cooked_trout", p -> p
                        .food(COOKED_TROUT_FOOD), (c, p) -> {
                    DataIngredient trout = DataIngredient.items(TROUT.get());
                    p.food(trout, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("熟鳟鱼");
                    b.zh_tw("熟鱒魚");
                    b.zh_hk("熟鱒魚");
                });

            }//(cooked/)trout
            {
                PIRANHA = baseFood("piranha", p -> p
                        .food(TROUT_FOOD), (c, p) -> {

                }, b -> {
                    b.zh_cn("生食人鱼");
                    b.zh_tw("生食人魚");
                    b.zh_hk("生食人魚");
                });
                COOKED_PIRANHA = baseFood("cooked_piranha", p -> p
                        .food(COOKED_TROUT_FOOD), (c, p) -> {
                    DataIngredient piranha = DataIngredient.items(PIRANHA.get());
                    p.food(piranha, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.zh_cn("熟食人鱼");
                    b.zh_tw("熟食人魚");
                    b.zh_hk("熟食人魚");
                });

            }//(cooked/)piranha
            {
                DOUGH = baseFood("dough", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(3)
                                .saturationModifier(.3F)
                                .build()
                        ), (c, p) -> {

                    DataIngredient wheat = DataIngredient.tag(Tags.Items.CROPS_WHEAT);
                    DataIngredient water = DataIngredient.items(Items.WATER_BUCKET);
                    DataIngredient dough = DataIngredient.items(c.get());
                    ShapelessRecipeBuilder
                            .shapeless(RecipeCategory.FOOD, c.get(), 4)
                            .unlockedBy("has_" + p.safeName(wheat), wheat.getCriterion(p))
                            .unlockedBy("has_" + p.safeName(water), water.getCriterion(p))
                            .requires(wheat.toVanilla(), 3)
                            .requires(water.toVanilla())
                            .save(p, p.safeId(dough));
                    ShapelessRecipeBuilder
                            .shapeless(RecipeCategory.FOOD, Items.BREAD, 1)
                            .unlockedBy("has_" + p.safeName(dough), dough.getCriterion(p))
                            .requires(dough.toVanilla(), 3)
                            .save(p, p.safeId(Items.BREAD));
                    p.food(dough, RecipeCategory.FOOD, () -> Items.BREAD, .35F);

                }, b -> {

                });
            }//dough
            {
                NETHERITE_APPLE_PIE = baseFood("netherite_apple_pie", p -> p
                        .food(
                                new FoodProperties.Builder()
                                        .nutrition(10)
                                        .saturationModifier(1.2F)
                                        .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 3600, 0), 1.0F)
                                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 140, 1), 1.0F)
                                        .build()
                        ), (c, p) -> {

                }, b -> {
                    b.zh_cn("下界合金苹果派");
                    b.zh_tw("下界合金蘋果派");
                    b.zh_hk("下界合金蘋果派");
                });
                ENCHANTED_NETHERITE_APPLE_PIE = parentFood("enchanted_netherite_apple_pie", p -> p
                        .food(
                                new FoodProperties.Builder()
                                        .nutrition(10)
                                        .saturationModifier(1.2F)
                                        .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 3600, 0), 1.0F)
                                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0F)
                                        .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 8400, 0), 1.0F)
                                        .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 8400, 0), 1.0F)
                                        .build()
                        ), NETHERITE_APPLE_PIE, (c, p) -> {

                }, b -> {
                    b.zh_cn("附魔下界合金苹果派");
                    b.zh_tw("附魔下界合金蘋果派");
                    b.zh_hk("附魔下界合金蘋果派");
                });
                GOLDEN_APPLE_PIE = baseFood("golden_apple_pie", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(8)
                                .saturationModifier(1.2F)
                                .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 3600, 0), 1.0F)
                                .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 140, 1), 1.0F)
                                .build()
                        ), (c, p) -> {

                }, b -> {
                    b.zh_cn("金苹果派");
                    b.zh_tw("金蘋果派");
                    b.zh_hk("金蘋果派");
                });
                ENCHANTED_GOLDEN_APPLE_PIE = parentFood("enchanted_golden_apple_pie", p -> p
                        .food(
                                new FoodProperties.Builder()
                                        .nutrition(8)
                                        .saturationModifier(1.2F)
                                        .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 3600, 0), 1.0F)
                                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0F)
                                        .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 8400, 0), 1.0F)
                                        .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 8400, 0), 1.0F)
                                        .build()
                        ), GOLDEN_APPLE_PIE, (c, p) -> {

                }, b -> {
                    b.zh_cn("附魔金苹果派");
                    b.zh_tw("附魔金蘋果派");
                    b.zh_hk("附魔金蘋果派");
                });
                APPLE_PIE = baseFood("apple_pie", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(6)
                                .saturationModifier(.3F)
                                .build()), (c, p) -> {
                    DataIngredient egg = DataIngredient.tag(Tags.Items.EGGS);
                    DataIngredient apple = DataIngredient.items(Items.APPLE);
                    DataIngredient sugar = DataIngredient.items(Items.SUGAR);
                    ShapelessRecipeBuilder
                            .shapeless(RecipeCategory.FOOD, c.get())
                            .unlockedBy("has_" + p.safeName(egg), egg.getCriterion(p))
                            .unlockedBy("has_" + p.safeName(apple), apple.getCriterion(p))
                            .unlockedBy("has_" + p.safeName(sugar), sugar.getCriterion(p))
                            .requires(apple.toVanilla())
                            .requires(sugar.toVanilla())
                            .requires(egg.toVanilla())
                            .save(p, p.safeId(c.get()));
                }, b -> {
                    b.zh_cn("苹果派");
                    b.zh_tw("蘋果派");
                    b.zh_hk("蘋果派");
                });
                BEERY_PIE = baseFood("berry_pie", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(8)
                                .saturationModifier(.1F)
                                .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 6000, 0), 1)
                                .build()), (c, p) -> {
                    DataIngredient egg = DataIngredient.tag(Tags.Items.EGGS);
                    DataIngredient glow = DataIngredient.items(Items.GLOW_BERRIES);
                    DataIngredient sweet = DataIngredient.items(Items.SWEET_BERRIES);
                    ShapelessRecipeBuilder
                            .shapeless(RecipeCategory.FOOD, c.get())
                            .unlockedBy("has_" + p.safeName(egg), egg.getCriterion(p))
                            .unlockedBy("has_" + p.safeName(glow), glow.getCriterion(p))
                            .unlockedBy("has_" + p.safeName(sweet), sweet.getCriterion(p))
                            .requires(glow.toVanilla())
                            .requires(sweet.toVanilla(), 2)
                            .requires(egg.toVanilla())
                            .save(p, p.safeId(c.get()));
                }, b -> {
                    b.zh_cn("浆果派");
                    b.zh_tw("漿果派");
                    b.zh_hk("漿果派");
                });
            }//((enchanted/)(golden/netherite)/)apple/berry)pie
            {
                COOKED_EGG = baseFood("cooked_egg", p -> p
                        .food(
                                new FoodProperties.Builder()
                                        .nutrition(5)
                                        .saturationModifier(.6F)
                                        .build()
                        ), (c, p) -> {
                    DataIngredient egg = DataIngredient.tag(Tags.Items.EGGS);
                    p.food(egg, RecipeCategory.FOOD, c, .35F);
                }, b -> {
                    b.zh_cn("煎蛋");
                    b.zh_tw("煎蛋");
                    b.zh_hk("煎蛋");
                });
            }//cooked_egg
            {
                CHEESE = REGISTRATE
                        .item("cheese", CheeseItem::new)
                        .zh_cn("奶酪")
                        .zh_tw("乳酪")
                        .zh_hk("乳酪")
                        .defaultLang()
                        .defaultModel()
                        .tab(ModTabs.FOOD_AND_DRINK.key())
                        .properties(p -> p
                                .stacksTo(16)
                                .food(
                                        new FoodProperties.Builder()
                                                .nutrition(3)
                                                .saturationModifier(1.2F)
                                                .build()
                                ))
                        .recipe((c, p) -> {
                            DataIngredient brown = DataIngredient.items(Items.BROWN_MUSHROOM);
                            DataIngredient sugar = DataIngredient.items(Items.SUGAR);
                            DataIngredient milk = DataIngredient.items(Items.MILK_BUCKET);
                            ShapelessRecipeBuilder
                                    .shapeless(RecipeCategory.FOOD, c.get())
                                    .unlockedBy("has_" + p.safeName(brown), brown.getCriterion(p))
                                    .unlockedBy("has_" + p.safeName(sugar), sugar.getCriterion(p))
                                    .unlockedBy("has_" + p.safeName(milk), milk.getCriterion(p))
                                    .requires(brown.toVanilla())
                                    .requires(sugar.toVanilla())
                                    .requires(milk.toVanilla())
                                    .save(p, p.safeId(c.get()));
                        })
                        .register();
            }//cheese
            {
                FAILED_CUISINE = baseFood("failed_cuisine", p -> p
                        .food(
                                new FoodProperties.Builder()
                                        .build()), (c, p) -> {

                }, b-> {
                    b.zh_cn("失败料理");
                    b.zh_cn("失敗料理");
                    b.zh_cn("失敗料理");
                });
                CHARRED_CUISINE = baseFood("charred_cuisine", p -> p
                        .food(
                                new FoodProperties.Builder()
                                        .build()), (c, p) -> {

                }, b-> {
                    b.zh_cn("焦糊料理");
                    b.zh_cn("焦糊料理");
                    b.zh_cn("焦糊料理");
                });
            }//(charred/failed)cuisine
        }
    }

    public static ItemEntry<Item> baseFood(String name, NonNullUnaryOperator<Item.Properties> properties, NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe, Consumer<ItemBuilder<Item, L2Registrate>> consumer) {
        ItemBuilder<Item, L2Registrate> item = REGISTRATE
                .item(name, Item::new);
        consumer.accept(item);
        return item
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
                .model((c, p) -> p.withExistingParent(c.getId().getPath(), BuiltInRegistries.ITEM.getKey(vanilla).withPrefix("item/")))
                .recipe(recipe)
                .lang(name.substring(0, 1).toUpperCase() + name.substring(1).replace("_", " "))
                .properties(properties)
                .tab(ModTabs.FOOD_AND_DRINK.key())
                .register();
    }
    public static ItemEntry<Item> parentFood(String name, NonNullUnaryOperator<Item.Properties> properties, ItemEntry<?> entry, NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe, Consumer<ItemBuilder<Item, L2Registrate>> consumer) {
        ItemBuilder<Item, L2Registrate> item = REGISTRATE
                .item(name, Item::new);
        consumer.accept(item);
        return item
                .model((c, p) -> p.withExistingParent(c.getId().getPath(), entry.getId().withPrefix("item/")))
                .recipe(recipe)
                .lang(name.substring(0, 1).toUpperCase() + name.substring(1).replace("_", " "))
                .properties(properties)
                .tab(ModTabs.FOOD_AND_DRINK.key())
                .register();
    }



    public static void register() {}
}
