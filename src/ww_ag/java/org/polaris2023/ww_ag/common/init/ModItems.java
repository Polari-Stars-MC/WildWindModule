package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import lombok.experimental.ExtensionMethod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;
import org.polaris2023.ww_ag.common.items.CheeseItem;
import org.polaris2023.ww_ag.common.items.LivingTuberItem;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.RegUtil;

import java.util.Locale;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
@ExtensionMethod({RegUtil.class})
public class ModItems {
    public static final ItemEntry<Item> CANDY, SALT, FISH_BONE;
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
            FAILED_CUISINE, CHARRED_CUISINE, FLOUR;
    public static final ItemEntry<Item> LINGERING_MILK_BOTTLE;
    public static final ItemEntry<Item>
            MILK_BOTTLE,
            SPLASH_MILK_BOTTLE
            ;
    public static final ItemEntry<Item>
            SPLASH_HONEY_BOTTLE,
            LINGERING_HONEY_BOTTLE;

    static {
        {
            SALT = REGISTRATE
                    .defTab(ModTabs.INGREDIENTS.key())
                    .itemReg("salt", Item::new)
                    .ww_ag$zh_cn("盐")
                    .ww_ag$zh_tw("鹽")
                    .ww_ag$zh_hk("鹽")
                    .ww_ag$self()
                    .defaultModel()
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
            FISH_BONE = REGISTRATE
                    .itemReg("fish_bone", Item::new)
                    .ww_ag$zh_cn("鱼骨")
                    .ww_ag$zh_cn("魚骨")
                    .ww_ag$zh_cn("魚骨")
                    .ww_ag$self()
                    .lang("Fish Bone")
                    .properties(p -> p
                            .stacksTo(16))
                    .defaultModel()
                    .recipe((c, p) -> {
                        DataIngredient self = DataIngredient.items(c);
                        ShapelessRecipeBuilder
                                .shapeless(RecipeCategory.MISC, Items.BONE_MEAL)
                                .unlockedBy("has_" + p.safeName(self), self.getCriterion(p))
                                .requires(self.toVanilla())
                                .save(p, p.safeId(Items.BONE_MEAL));
                    })
                    .tag(Tags.Items.BONES)
                    .register();
        }//fish bone
        {
            SPLASH_HONEY_BOTTLE = REGISTRATE
                    .defTab(ModTabs.FOOD_AND_DRINK.key())
                    .itemReg("splash_honey_bottle", Item::new)
                    .ww_ag$zh_cn("喷溅型蜂蜜瓶")
                    .ww_ag$zh_tw("噴濺型蜂蜜瓶")
                    .ww_ag$zh_hk("噴濺型蜂蜜瓶")
                    .ww_ag$self()
                    .lang("Splash Honey Bottle")
                    .defaultModel()
                    .properties(p -> p
                            .stacksTo(1))
                    .register();
            LINGERING_HONEY_BOTTLE = REGISTRATE
                    .itemReg("lingering_honey_bottle", Item::new)
                    .ww_ag$zh_cn("滞留型蜂蜜瓶")
                    .ww_ag$zh_tw("滞留型蜂蜜瓶")
                    .ww_ag$zh_hk("滞留型蜂蜜瓶")
                    .ww_ag$self()
                    .lang("Lingering Honey Bottle")
                    .defaultModel()
                    .properties(p -> p
                            .stacksTo(1))
                    .register();

        }//(splash/lingering)honey bottle
        {
            MILK_BOTTLE = REGISTRATE
                    .itemReg("milk_bottle", Item::new)
                    .ww_ag$zh_cn("奶瓶")
                    .ww_ag$zh_tw("奶瓶")
                    .ww_ag$zh_hk("奶瓶")
                    .ww_ag$self()
                    .lang("Milk Bottle")
                    .defaultModel()
                    .properties(p -> p
                            .stacksTo(1)
                    )
                    .register();
            SPLASH_MILK_BOTTLE = REGISTRATE
                    .itemReg("splash_milk_bottle", Item::new)
                    .ww_ag$zh_cn("喷溅型奶瓶")
                    .ww_ag$zh_tw("噴濺型奶瓶")
                    .ww_ag$zh_hk("噴濺型奶瓶")
                    .ww_ag$self()
                    .lang("Splash Milk Bottle")
                    .defaultModel()
                    .properties(p -> p
                            .stacksTo(1)
                    )
                    .register();
            LINGERING_MILK_BOTTLE = REGISTRATE
                    .itemReg("lingering_milk_bottle", Item::new)
                    .ww_ag$zh_cn("滞留型奶瓶")
                    .ww_ag$zh_tw("滯留型奶瓶")
                    .ww_ag$zh_hk("滯留型奶瓶")
                    .ww_ag$self()
                    .lang("Lingering Milk Bottle")
                    .defaultModel()
                    .properties(p -> p
                            .stacksTo(1)
                    )
                    .register();

        }//(splash/lingering/)milk bottle
        {
            CANDY = REGISTRATE
                    .itemReg("candy", Item::new)
                    .ww_ag$zh_cn("糖果")
                    .ww_ag$zh_tw("糖果")
                    .ww_ag$zh_hk("糖果")
                    .ww_ag$self()
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
                    b.ww_ag$zh_tw("烤苹果");
                    b.ww_ag$zh_tw("烤蘋果");
                    b.ww_ag$zh_hk("烤蘋果");
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
                    b.ww_ag$zh_cn("下界合金苹果");
                    b.ww_ag$zh_tw("下界合金蘋果");
                    b.ww_ag$zh_hk("下界合金蘋果");
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
                    b.ww_ag$zh_cn("附魔下界合金苹果");
                    b.ww_ag$zh_hk("附魔下界合金蘋果");
                    b.ww_ag$zh_hk("附魔下界合金蘋果");
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
                    b.ww_ag$zh_cn("烤西瓜片");
                    b.ww_ag$zh_tw("烤西瓜片");
                    b.ww_ag$zh_hk("烤西瓜片");
                });
            }//baked melon slice
            {
                PUMPKIN_SLICE = baseFood("pumpkin_slice", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.ww_ag$zh_cn("南瓜片");
                    b.ww_ag$zh_tw("南瓜片");
                    b.ww_ag$zh_hk("南瓜片");
                });
                BAKED_PUMPKIN_SLICE = baseFood("baked_pumpkin_slice", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(0.3F)
                                .build()), (c, p) -> {
                    DataIngredient items = DataIngredient.items(ModItems.PUMPKIN_SLICE.get());
                    p.food(items, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.ww_ag$zh_cn("烤南瓜片");
                    b.ww_ag$zh_tw("烤南瓜片");
                    b.ww_ag$zh_hk("烤南瓜片");
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
                    b.ww_ag$zh_cn("烤蘑菇");
                    b.ww_ag$zh_tw("烤蘑菇");
                    b.ww_ag$zh_hk("烤蘑菇");
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
                    b.ww_ag$zh_cn("烤种子");
                    b.ww_ag$zh_tw("烤種子");
                    b.ww_ag$zh_hk("烤種子");
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
                    b.ww_ag$zh_cn("烤浆果");
                    b.ww_ag$zh_tw("烤漿果");
                    b.ww_ag$zh_hk("烤漿果");
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
                    b.ww_ag$zh_cn("烤胡萝卜");
                    b.ww_ag$zh_tw("烤胡蘿蔔");
                    b.ww_ag$zh_hk("烤胡蘿蔔");
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
                    b.ww_ag$zh_cn("烤甜菜");
                    b.ww_ag$zh_tw("烤甜菜");
                    b.ww_ag$zh_hk("烤甜菜");
                });
            }//baked beetroot
            {
                LIVING_TUBER = REGISTRATE
                        .itemReg("living_tuber", LivingTuberItem::new)
                        .ww_ag$zh_cn("活根")
                        .ww_ag$zh_tw("活根")
                        .ww_ag$zh_hk("活根")
                        .ww_ag$self()
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
                    b.ww_ag$zh_cn("烤活根");
                    b.ww_ag$zh_tw("烤活根");
                    b.ww_ag$zh_hk("烤活根");
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
                    b.ww_ag$zh_cn("鹿排");
                    b.ww_ag$zh_tw("鹿排");
                    b.ww_ag$zh_hk("鹿排");
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
                    b.ww_ag$zh_cn("熟鹿排");
                    b.ww_ag$zh_tw("熟鹿排");
                    b.ww_ag$zh_hk("熟鹿排");
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
                    b.ww_ag$zh_cn("生蝙蝠翼");
                    b.ww_ag$zh_tw("生蝙蝠翼");
                    b.ww_ag$zh_hk("生蝙蝠翼");
                });
                COOKED_BAT_WING = baseFood("cooked_bat_wing", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(4)
                                .saturationModifier(.8F)
                                .build()), (c, p) -> {
                    DataIngredient batWing = DataIngredient.items(BAT_WING.get());
                    p.food(batWing, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.ww_ag$zh_cn("熟蝙蝠翼");
                    b.ww_ag$zh_tw("熟蝙蝠翼");
                    b.ww_ag$zh_hk("熟蝙蝠翼");
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
                    b.ww_ag$zh_cn("生蛙腿");
                    b.ww_ag$zh_tw("生蛙腿");
                    b.ww_ag$zh_hk("生蛙腿");
                });
                COOKED_FROG_LEG = baseFood("cooked_frog_leg", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(.8F)
                                .build()), (c, p) -> {
                    DataIngredient frogLeg = DataIngredient.items(FROG_LEG.get());
                    p.food(frogLeg, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.ww_ag$zh_cn("熟蛙腿");
                    b.ww_ag$zh_tw("熟蛙腿");
                    b.ww_ag$zh_hk("熟蛙腿");
                });

            }//(cooked/)frog.leg
            {
                CALAMARI = baseFood("calamari", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(.1F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.ww_ag$zh_cn("生鱿鱼须");
                    b.ww_ag$zh_tw("生魷魚須");
                    b.ww_ag$zh_hk("生魷魚須");
                });
                GLOWING_CALAMARI = baseFood("glowing_calamari", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(2)
                                .saturationModifier(.1F)
                                .build()), (c, p) -> {

                }, b -> {
                    b.ww_ag$zh_cn("发光鱿鱼须");
                    b.ww_ag$zh_tw("發光魷魚須");
                    b.ww_ag$zh_hk("發光魷魚須");
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
                    b.ww_ag$zh_cn("熟鱿鱼须");
                    b.ww_ag$zh_cn("熟魷魚須");
                    b.ww_ag$zh_cn("熟魷魚須");
                });

            }//(cooked/glowing/)calamari
            {
                TROUT = baseFood("trout", p -> p
                        .food(TROUT_FOOD), (c, p) -> {

                }, b -> {
                    b.ww_ag$zh_cn("生鳟鱼");
                    b.ww_ag$zh_tw("生鱒魚");
                    b.ww_ag$zh_hk("生鱒魚")
                            .ww_ag$self()
                            .tag(Tags.Items.FOODS_RAW_FISH);
                });
                COOKED_TROUT = baseFood("cooked_trout", p -> p
                        .food(COOKED_TROUT_FOOD), (c, p) -> {
                    DataIngredient trout = DataIngredient.items(TROUT.get());
                    p.food(trout, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.ww_ag$zh_cn("熟鳟鱼");
                    b.ww_ag$zh_tw("熟鱒魚");
                    b.ww_ag$zh_hk("熟鱒魚")
                            .ww_ag$self()
                            .tag(Tags.Items.FOODS_COOKED_FISH);
                });

            }//(cooked/)trout
            {
                PIRANHA = baseFood("piranha", p -> p
                        .food(TROUT_FOOD), (c, p) -> {

                }, b -> {
                    b.ww_ag$zh_cn("生食人鱼");
                    b.ww_ag$zh_tw("生食人魚");
                    b.ww_ag$zh_hk("生食人魚")
                            .ww_ag$self()
                            .tag(Tags.Items.FOODS_RAW_FISH);
                });
                COOKED_PIRANHA = baseFood("cooked_piranha", p -> p
                        .food(COOKED_TROUT_FOOD), (c, p) -> {
                    DataIngredient piranha = DataIngredient.items(PIRANHA.get());
                    p.food(piranha, RecipeCategory.FOOD, c, 0.35F);
                }, b -> {
                    b.ww_ag$zh_cn("熟食人鱼");
                    b.ww_ag$zh_tw("熟食人魚");
                    b.ww_ag$zh_hk("熟食人魚")
                            .ww_ag$self()
                            .tag(Tags.Items.FOODS_COOKED_FISH);
                });

            }//(cooked/)piranha
            {
                DOUGH = baseFood("dough", p -> p
                        .food(new FoodProperties.Builder()
                                .nutrition(3)
                                .saturationModifier(.3F)
                                .build()
                        ), (c, p) -> {

                    DataIngredient flour = DataIngredient.items(ModItems.FLOUR.get());
                    DataIngredient water = DataIngredient.items(Items.WATER_BUCKET);
                    DataIngredient dough = DataIngredient.items(c.get());
                    ShapelessRecipeBuilder
                            .shapeless(RecipeCategory.FOOD, c.get(), 1)
                            .unlockedBy("has_" + p.safeName(flour), flour.getCriterion(p))
                            .requires(flour.toVanilla(), 3)
                            .requires(water.toVanilla())
                            .save(p, p.safeId(dough));

                    p.food(dough, RecipeCategory.FOOD, () -> Items.BREAD, .35F);

                }, b -> {
                    b.ww_ag$zh_cn("面团");
                    b.ww_ag$zh_cn("麵團");
                    b.ww_ag$zh_cn("麵團")
                            .ww_ag$self()
                            .tag(ItemTags.COW_FOOD);
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
                    b.ww_ag$zh_cn("下界合金苹果派");
                    b.ww_ag$zh_tw("下界合金蘋果派");
                    b.ww_ag$zh_hk("下界合金蘋果派");
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
                    b.ww_ag$zh_cn("附魔下界合金苹果派");
                    b.ww_ag$zh_tw("附魔下界合金蘋果派");
                    b.ww_ag$zh_hk("附魔下界合金蘋果派");
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
                    b.ww_ag$zh_cn("金苹果派");
                    b.ww_ag$zh_tw("金蘋果派");
                    b.ww_ag$zh_hk("金蘋果派");
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
                    b.ww_ag$zh_cn("附魔金苹果派");
                    b.ww_ag$zh_tw("附魔金蘋果派");
                    b.ww_ag$zh_hk("附魔金蘋果派");
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
                    b.ww_ag$zh_cn("苹果派");
                    b.ww_ag$zh_tw("蘋果派");
                    b.ww_ag$zh_hk("蘋果派");
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
                    b.ww_ag$zh_cn("浆果派");
                    b.ww_ag$zh_tw("漿果派");
                    b.ww_ag$zh_hk("漿果派");
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
                    b.ww_ag$zh_cn("煎蛋");
                    b.ww_ag$zh_tw("煎蛋");
                    b.ww_ag$zh_hk("煎蛋");
                });
            }//cooked_egg
            {

                CHEESE = REGISTRATE
                        .itemReg("cheese", CheeseItem::new)
                        .ww_ag$zh_cn("奶酪")
                        .ww_ag$zh_tw("乳酪")
                        .ww_ag$zh_hk("乳酪")
                        .ww_ag$self()
                        .lang("Cheese")
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
                    b.ww_ag$zh_cn("失败料理");
                    b.ww_ag$zh_cn("失敗料理");
                    b.ww_ag$zh_cn("失敗料理");
                });
                CHARRED_CUISINE = baseFood("charred_cuisine", p -> p
                        .food(
                                new FoodProperties.Builder()
                                        .build()), (c, p) -> {

                }, b-> {
                    b.ww_ag$zh_cn("焦糊料理");
                    b.ww_ag$zh_cn("焦糊料理");
                    b.ww_ag$zh_cn("焦糊料理");
                });
            }//(charred/failed)cuisine
            {
                FLOUR = baseFood(
                        "flour",
                        p -> p
                                .food(
                                        new FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationModifier(.6F)
                                                .build()
                                ),
                        (c, p) -> {
                            DataIngredient wheat = DataIngredient.tag(Tags.Items.CROPS_WHEAT);
                            ShapelessRecipeBuilder
                                    .shapeless(RecipeCategory.FOOD, c.getEntry(), 5)
                                    .unlockedBy("has_" + p.safeName(wheat), wheat.getCriterion(p))
                                    .requires(wheat.toVanilla(), 2)
                                    .save(p, p.safeId(c.get()));
                        },
                        b -> {
                            b.ww_ag$zh_cn("面粉");
                            b.ww_ag$zh_tw("麵粉");
                            b.ww_ag$zh_hk("麵粉");
                        });
            }//FLOUR

        }
    }

    public static ItemEntry<Item> baseFood(String name,
                                           NonNullUnaryOperator<Item.Properties> properties,
                                           NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe,
                                           NonNullConsumer<ILanguage<Item, Item, L2Registrate, ItemBuilder<Item, L2Registrate>>> consumer) {
        var item = REGISTRATE
                .itemReg(name, Item::new);
        consumer.accept(item);

        return item
                .ww_ag$self()
                .defaultModel()
                .recipe(recipe)
                .lang(__ -> {
                    var s = name.split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (String string : s) {
                        sb
                                .append(string.substring(0, 1).toLowerCase(Locale.ROOT))
                                .append(string.substring(1))
                                .append(" ");
                    }
                    return sb.toString().trim();
                })
                .properties(properties)
                .register();
    }
    public static ItemEntry<Item> parentVanillaFood(String name,
                                                    NonNullUnaryOperator<Item.Properties> properties,
                                                    Item vanilla,
                                                    NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe,
                                                    NonNullConsumer<ILanguage<Item, Item, L2Registrate, ItemBuilder<Item, L2Registrate>>> b) {
        var item = REGISTRATE
                .itemReg(name, Item::new);
        b.accept(item);
        return item
                .ww_ag$self()
                .model((c, p) -> p.withExistingParent(c.getId().getPath(), BuiltInRegistries.ITEM.getKey(vanilla).withPrefix("item/")))
                .recipe(recipe)
                .lang(__ -> {
                    var s = name.split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (String string : s) {
                        sb
                                .append(string.substring(0, 1).toLowerCase(Locale.ROOT))
                                .append(string.substring(1))
                                .append(" ");
                    }
                    return sb.toString().trim();
                })
                .properties(properties)
                .register();
    }
    public static ItemEntry<Item> parentFood(String name,
                                             NonNullUnaryOperator<Item.Properties> properties,
                                             ItemEntry<?> entry,
                                             NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe,
                                             NonNullConsumer<ILanguage<Item, Item, L2Registrate, ItemBuilder<Item, L2Registrate>>> consumer) {
        var item = REGISTRATE.itemReg(name, Item::new);
        consumer.accept(item);
        return item
                .ww_ag$self()
                .model((c, p) -> p.withExistingParent(c.getId().getPath(), entry.getId().withPrefix("item/")))
                .recipe(recipe)
                .lang(__ -> {
                    var s = name.split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (String string : s) {
                        sb
                                .append(string.substring(0, 1).toLowerCase(Locale.ROOT))
                                .append(string.substring(1))
                                .append(" ");
                    }
                    return sb.toString().trim();
                })
                .properties(properties)
                .register();
    }



    public static void register() {}
}
