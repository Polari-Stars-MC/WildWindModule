package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.Locale;
import java.util.function.Supplier;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 18:08:12}
 */
public class ModTabs {
    public static final SimpleEntry<CreativeModeTab> BUILDING_BLOCK = register(
            "Building Block", ModBlocks.SALT_BLOCK::asStack
    );
    public static final SimpleEntry<CreativeModeTab> NATURAL_BLOCKS = register(
            "Natural Blocks", ModBlocks.SALT_ORE::asStack
    );
    public static final SimpleEntry<CreativeModeTab> INGREDIENTS = register(
            "Ingredients", ModItems.SALT::asStack
    );
    public static final SimpleEntry<CreativeModeTab> FOOD_AND_DRINK = register(
            "Food And Drink", ModItems.SALT::asStack
    );

    public static SimpleEntry<CreativeModeTab> register(String name, String display, Supplier<ItemStack> supplier) {
        return REGISTRATE.buildModCreativeTab(name, "Wild Wind: " + display, b -> {
            b.icon(supplier);
        });
    }

    public static SimpleEntry<CreativeModeTab> register(String display, Supplier<ItemStack> supplier) {
        String name = display.toLowerCase(Locale.ROOT).replace(" ", "_").replace("-", "_").replace("$", "_");
        return register(name, display, supplier);
    }




    public static void register() {

    }
}
