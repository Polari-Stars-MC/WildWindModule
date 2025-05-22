package org.polaris2023.ww_ag.common.init;

import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.item.CreativeModeTab;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 18:08:12}
 */
public class ModTabs {
    public static final SimpleEntry<CreativeModeTab> BUILDING_BLOCK = REGISTRATE.buildModCreativeTab("building_block", "Wild Wind: Building Block", b -> b.icon(ModBlocks.SALT_BLOCK::asStack));
    public static final SimpleEntry<CreativeModeTab> NATURAL_BLOCKS = REGISTRATE.buildModCreativeTab("natural_blocks", "Wild Wind: Natural Blocks", b -> b.icon(ModBlocks.SALT_ORE::asStack));
    public static final SimpleEntry<CreativeModeTab> INGREDIENTS = REGISTRATE.buildModCreativeTab("ingredients", "Wild Wind: Ingredients", b -> b.icon(ModItems.SALT::asStack));
    public static final SimpleEntry<CreativeModeTab> FOOD_AND_DRINK = REGISTRATE.buildModCreativeTab("food_and_drink", "Wild Wind: Food And Drink", b -> b.icon(ModItems.BAKED_APPLE::asStack));



    public static void register() {

    }
}
