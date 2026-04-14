package org.polaris2023.wildwind.hfas.groups;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
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
            // 杜鹃系列
            {
                ItemStack logStack = ModBlocks.AZALEA_LOG.toStack();
                event.insertAfter(new ItemStack(Blocks.CHERRY_BUTTON), logStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack woodStack = ModBlocks.AZALEA_WOOD.toStack();
                event.insertAfter(logStack, woodStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack strippedLogStack = ModBlocks.STRIPPED_AZALEA_LOG.toStack();
                event.insertAfter(woodStack, strippedLogStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack strippedWoodStack = ModBlocks.STRIPPED_AZALEA_WOOD.toStack();
                event.insertAfter(strippedLogStack, strippedWoodStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack planksStack = ModBlocks.AZALEA_PLANKS.toStack();
                event.insertAfter(strippedWoodStack, planksStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack stairsStack = ModBlocks.AZALEA_STAIRS.toStack();
                event.insertAfter(planksStack, stairsStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack slabStack = ModBlocks.AZALEA_SLAB.toStack();
                event.insertAfter(stairsStack, slabStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack fenceStack = ModBlocks.AZALEA_FENCE.toStack();
                event.insertAfter(slabStack, fenceStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack fenceGateStack = ModBlocks.AZALEA_FENCE_GATE.toStack();
                event.insertAfter(fenceStack, fenceGateStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack doorStack = ModBlocks.AZALEA_DOOR.toStack();
                event.insertAfter(fenceGateStack, doorStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack trapdoorStack = ModBlocks.AZALEA_TRAPDOOR.toStack();
                event.insertAfter(doorStack, trapdoorStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(trapdoorStack, ModBlocks.AZALEA_PRESSURE_PLATE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            // 焚烬系列
            {
                ItemStack logStack = ModBlocks.BLAZE_LOG.toStack();
                event.insertAfter(new ItemStack(Blocks.BAMBOO_BUTTON), logStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack woodStack = ModBlocks.BLAZE_WOOD.toStack();
                event.insertAfter(logStack, woodStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack strippedLogStack = ModBlocks.STRIPPED_BLAZE_LOG.toStack();
                event.insertAfter(woodStack, strippedLogStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack strippedWoodStack = ModBlocks.STRIPPED_BLAZE_WOOD.toStack();
                event.insertAfter(strippedLogStack, strippedWoodStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack planksStack = ModBlocks.BLAZE_PLANKS.toStack();
                event.insertAfter(strippedWoodStack, planksStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack stairsStack = ModBlocks.BLAZE_STAIRS.toStack();
                event.insertAfter(planksStack, stairsStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack slabStack = ModBlocks.BLAZE_SLAB.toStack();
                event.insertAfter(stairsStack, slabStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack fenceStack = ModBlocks.BLAZE_FENCE.toStack();
                event.insertAfter(slabStack, fenceStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack fenceGateStack = ModBlocks.BLAZE_FENCE_GATE.toStack();
                event.insertAfter(fenceStack, fenceGateStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack doorStack = ModBlocks.BLAZE_DOOR.toStack();
                event.insertAfter(fenceGateStack, doorStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack trapdoorStack = ModBlocks.BLAZE_TRAPDOOR.toStack();
                event.insertAfter(doorStack, trapdoorStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(trapdoorStack, ModBlocks.BLAZE_PRESSURE_PLATE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            // 灵焰系列
            {
                ItemStack logStack = ModBlocks.SOUL_LOG.toStack();
                event.insertAfter(ModBlocks.BLAZE_PRESSURE_PLATE.toStack(), logStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack woodStack = ModBlocks.SOUL_WOOD.toStack();
                event.insertAfter(logStack, woodStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack strippedLogStack = ModBlocks.STRIPPED_SOUL_LOG.toStack();
                event.insertAfter(woodStack, strippedLogStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack strippedWoodStack = ModBlocks.STRIPPED_SOUL_WOOD.toStack();
                event.insertAfter(strippedLogStack, strippedWoodStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack planksStack = ModBlocks.SOUL_PLANKS.toStack();
                event.insertAfter(strippedWoodStack, planksStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack stairsStack = ModBlocks.SOUL_STAIRS.toStack();
                event.insertAfter(planksStack, stairsStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack slabStack = ModBlocks.SOUL_SLAB.toStack();
                event.insertAfter(stairsStack, slabStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack fenceStack = ModBlocks.SOUL_FENCE.toStack();
                event.insertAfter(slabStack, fenceStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack fenceGateStack = ModBlocks.SOUL_FENCE_GATE.toStack();
                event.insertAfter(fenceStack, fenceGateStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack doorStack = ModBlocks.SOUL_DOOR.toStack();
                event.insertAfter(fenceGateStack, doorStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                ItemStack trapdoorStack = ModBlocks.SOUL_TRAPDOOR.toStack();
                event.insertAfter(doorStack, trapdoorStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(trapdoorStack, ModBlocks.SOUL_PRESSURE_PLATE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }


            // 焦灰草方块
//            event.accept(ModBlocks.SCORCHED_GRASS_BLOCK.get());
//            // 焦土
//            event.accept(ModBlocks.SCORCHED_DIRT.get());
        }

        // 自然方块物品栏
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            ItemStack azaleaLogStack = ModBlocks.AZALEA_LOG.toStack();
            event.insertAfter(new ItemStack(Items.CHERRY_LOG), azaleaLogStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            ItemStack blazeLogStack = ModBlocks.BLAZE_LOG.toStack();
            event.insertAfter(azaleaLogStack, blazeLogStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(blazeLogStack, ModBlocks.SOUL_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            // 焦灰草丛
//            event.accept(ModBlocks.SCORCHED_GRASS.get());
//            // 焦灰枝条
//            event.accept(ModBlocks.SCORCHED_TWIG.get());
//            // 仙人球
//            event.accept(ModBlocks.TINY_CACTUS.get());
//            // 焦灰草方块
//            event.accept(ModBlocks.SCORCHED_GRASS_BLOCK.get());
//            // 焦土
//            event.accept(ModBlocks.SCORCHED_DIRT.get());
        }

    }
}
