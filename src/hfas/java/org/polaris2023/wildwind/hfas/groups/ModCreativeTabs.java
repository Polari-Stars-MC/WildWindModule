package org.polaris2023.wildwind.hfas.groups;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 创造模式物品栏配置
 * 将模组物品放入原版创造模式物品栏并排序
 *
 * @author baka4n
 * @since 2026/04/15
 */
@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class ModCreativeTabs {

    @SubscribeEvent
    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        // 建筑方块物品栏
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // 焦灰草方块
            event.accept(ModBlocks.SCORCHED_GRASS_BLOCK.get());
            // 焦土
            event.accept(ModBlocks.SCORCHED_DIRT.get());
        }

        // 自然方块物品栏
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            // 焦灰草丛
            event.accept(ModBlocks.SCORCHED_GRASS.get());
            // 焦灰枝条
            event.accept(ModBlocks.SCORCHED_TWIG.get());
            // 仙人球
            event.accept(ModBlocks.TINY_CACTUS.get());
            // 焦灰草方块
            event.accept(ModBlocks.SCORCHED_GRASS_BLOCK.get());
            // 焦土
            event.accept(ModBlocks.SCORCHED_DIRT.get());
        }

        // 注意：模组制箭台不需要放入创造模式物品栏
    }
}
