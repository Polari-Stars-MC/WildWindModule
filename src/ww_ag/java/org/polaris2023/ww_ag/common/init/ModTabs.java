package org.polaris2023.ww_ag.common.init;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Consumer;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 18:08:12}
 */
public class ModTabs {
    public static final SimpleEntry<CreativeModeTab> BUILDING_BLOCK =
            REGISTRATE
                    .buildWWCreativeTab("building_block", "Wild Wind: Building Block",
                            b -> b.icon(ModBlocks.SALT_BLOCK::asStack), 0,
                            l -> {
                        l.ww_ag$zh_cn("原野之风：建筑方块");
                        l.ww_ag$zh_tw("風靈荒野：建築方塊");
                        l.ww_ag$zh_hk("風與草之詩：建築積木");
                    });
    public static final SimpleEntry<CreativeModeTab> NATURAL_BLOCKS =
            REGISTRATE
                    .buildWWCreativeTab("natural_blocks", "Wild Wind: Natural Blocks",
                            b -> b.icon(ModBlocks.SALT_ORE::asStack), 1,
                            l -> {
                                l.ww_ag$zh_cn("原野之风：自然方块");
                                l.ww_ag$zh_tw("風靈荒野：自然方塊");
                                l.ww_ag$zh_hk("風與草之詩：自然積木");
                            });
    public static final SimpleEntry<CreativeModeTab> INGREDIENTS =
            REGISTRATE
                    .buildWWCreativeTab("ingredients", "Wild Wind: Ingredients",
                            b -> b.icon(ModItems.SALT::asStack), 2,
                            l -> {
                                l.ww_ag$zh_cn("原野之风：材料");
                                l.ww_ag$zh_tw("風靈荒野：材料");
                                l.ww_ag$zh_hk("風與草之詩：材料");
                            });
    public static final SimpleEntry<CreativeModeTab> FOOD_AND_DRINK =
            REGISTRATE
                    .buildWWCreativeTab("food_and_drink", "Wild Wind: Food And Drink",
                            b -> b.icon(ModItems.BAKED_CARROT::asStack), 3,
                            l -> {
                                l.ww_ag$zh_cn("原野之风：食物与饮品");
                                l.ww_ag$zh_tw("風靈荒野：食物與飲料");
                                l.ww_ag$zh_hk("風與草之詩：食物同飲品");
                            });
    public static final SimpleEntry<CreativeModeTab> TOOLS_AND_UTILITIES =
            REGISTRATE.buildWWCreativeTab("tools_and_utilities", "Wild Wind: Tools & Utilities",
                    b -> {
                        Optional<Item> bucket = ModFluids.HONEY.getBucket();
                        bucket.ifPresentOrElse(item -> b.icon(item::getDefaultInstance), () -> {
                            b.icon(Items.DIAMOND_PICKAXE::getDefaultInstance);
                        });
                    }, 4,
                    l -> {
                        l.ww_ag$zh_cn("原野之风：工具");
                        l.ww_ag$zh_tw("風靈荒野：工具");
                        l.ww_ag$zh_hk("風與草之詩：工具");
                    });



    public static void register() {

    }
}
