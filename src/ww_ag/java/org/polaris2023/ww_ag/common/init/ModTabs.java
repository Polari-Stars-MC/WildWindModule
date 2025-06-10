package org.polaris2023.ww_ag.common.init;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.ModList;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 18:08:12}
 */
public class ModTabs {
    public static final SimpleEntry<CreativeModeTab> BUILDING_BLOCK =
            REGISTRATE
                    .buildModCreativeTab("building_block", "Wild Wind: Building Block",
                            b -> b.icon(ModBlocks.SALT_BLOCK::asStack));
    public static final SimpleEntry<CreativeModeTab> NATURAL_BLOCKS =
            REGISTRATE
                    .buildModCreativeTab("natural_blocks", "Wild Wind: Natural Blocks",
                            b -> b.icon(ModBlocks.SALT_ORE::asStack));
    public static final SimpleEntry<CreativeModeTab> INGREDIENTS =
            REGISTRATE
                    .buildModCreativeTab("ingredients", "Wild Wind: Ingredients",
                            b -> b.icon(ModItems.SALT::asStack));
    public static final SimpleEntry<CreativeModeTab> FOOD_AND_DRINK =
            REGISTRATE
                    .buildModCreativeTab("food_and_drink", "Wild Wind: Food And Drink",
                            b -> b.icon(ModItems.BAKED_APPLE::asStack));

    private synchronized SimpleEntry<CreativeModeTab> buildCreativeTabImpl(String name, Component comp, Consumer<CreativeModeTab.Builder> config) {
        return new SimpleEntry<>(REGISTRATE.generic(REGISTRATE, name, Registries.CREATIVE_MODE_TAB, () -> {
            CreativeModeTab.Builder builder = CreativeModeTab.builder().title(comp).withTabsBefore(CreativeModeTabs.SPAWN_EGGS);
            config.accept(builder);
            return builder.build();
        }).register());
    }


    public static void register() {

    }
}
