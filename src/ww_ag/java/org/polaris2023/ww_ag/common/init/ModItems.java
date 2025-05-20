package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
public class ModItems {
    public static final ItemEntry<Item> SALT = REGISTRATE
            .item("salt", Item::new)
            .defaultModel()
            .recipe((c, p) -> {
                DataIngredient items = DataIngredient.items(ModBlocks.SALT_BLOCK.get());
                DataIngredient ores = DataIngredient.tag(WWItemTags.ORES$SALT.get());
                p.storage(DataIngredient.items(c), RecipeCategory.MISC, c, items, ModBlocks.SALT_BLOCK);
                p.square(items, RecipeCategory.MISC, c, false);
                p.smelting(ores, RecipeCategory.MISC, c, 0.35F);
            })
            .lang("Salt")
            .register();
    public static final ItemEntry<Item> BAKED_APPLE = REGISTRATE
            .item("baked_apple", Item::new)
            .defaultModel()
            .properties(p ->
                    p.food(
                            new FoodProperties
                                    .Builder()
                                    .nutrition(5)
                                    .saturationModifier(0.3F)
                                    .build()))
            .recipe((c, p) -> {
                DataIngredient items = DataIngredient.items(Items.APPLE);
                p.smelting(items, RecipeCategory.FOOD, c, 0.35F);
                p.smoking(items, RecipeCategory.FOOD, c, 0.35F);
                p.campfire(items, RecipeCategory.FOOD, c, 0.35F);
            })
            .lang("Baked apple")
            .register();
    public static void register() {}
}
