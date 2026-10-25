package org.polaris2023.wildwind.hfas.groups;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.ModWoodFamilies;
import org.polaris2023.wildwind.hfas.block.WoodFamily;

/**
 * 创造模式物品栏配置
 * 将模组物品放入原版创造模式物品栏并排序
 *
 * @author baka4n
 * @since 2026/04/15
 */
//TODO 考虑统一迁移至registry下，同时考虑合入自定义tab
@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class ModCreativeTabs {

    @SubscribeEvent
    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        // 建筑方块物品栏

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // 三种木材：按家族顺序插入（以原版/本模组方块作锚点）
            appendWoodSet(event, new ItemStack(Blocks.CHERRY_BUTTON), ModWoodFamilies.AZALEA);
            appendWoodSet(event, new ItemStack(Blocks.BAMBOO_BUTTON), ModWoodFamilies.BLAZE);
            appendWoodSet(event, ModBlocks.BLAZE_BUTTON.toStack(), ModWoodFamilies.SOUL);

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
            event.insertAfter(ModBlocks.SOUL_LOG.toStack(), ModBlocks.BLAZE_LEAVES.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZE_LEAVES.toStack(), ModBlocks.SOUL_LEAVES.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.SOUL_LEAVES.toStack(), ModBlocks.BLAZE_SAPLING.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZE_SAPLING.toStack(), ModBlocks.SOUL_SAPLING.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
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

    /** 物品栏可见性：主标签页 + 搜索页 */
    private static final CreativeModeTab.TabVisibility VISIBILITY = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

    /**
     * 将一个木材家族的建筑套件按顺序插到 {@code anchor} 之后。
     * <p>
     * 每插一个就把新 stack 当作下一个锚点，这样整段在标签页里保持连续。
     *
     * @param anchor 该家族第一个方块的前一个位置（须已存在于标签页中，否则 {@code insertAfter} 会抛异常）
     */
    private static void appendWoodSet(BuildCreativeModeTabContentsEvent event, ItemStack anchor, WoodFamily family) {
        ItemStack previous = anchor;
        for (DeferredBlock<? extends Block> block : family.buildingBlocks()) {
            ItemStack stack = block.toStack();
            event.insertAfter(previous, stack, VISIBILITY);
            previous = stack;
        }
    }
}
