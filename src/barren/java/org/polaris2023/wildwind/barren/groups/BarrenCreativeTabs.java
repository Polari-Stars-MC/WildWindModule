package org.polaris2023.wildwind.barren.groups;

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
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.block.BarrenWoodFamilies;
import org.polaris2023.wildwind.core.block.WoodFamily;

/**
 * 瘠土更新的原版物品栏排序（模组自己的页由 {@link org.polaris2023.wildwind.barren.registry.ModCreativeTabs} 注册）。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author CarrotLegend
 * @author mdjfnfn
 * @author safe_saffawa
 */
@EventBusSubscriber(modid = WildWindBarren.MOD_ID)
public final class BarrenCreativeTabs {

    private static final CreativeModeTab.TabVisibility VISIBILITY = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

    @SubscribeEvent
    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            appendWoodSet(event, new ItemStack(Blocks.BAMBOO_BUTTON), BarrenWoodFamilies.CINDER);
            appendWoodSet(event, BarrenBlocks.CINDER_BUTTON.toStack(), BarrenWoodFamilies.EMBER);
        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            ItemStack cinderLogStack = BarrenBlocks.CINDER_LOG.toStack();
            event.insertAfter(new ItemStack(Items.CHERRY_LOG), cinderLogStack, VISIBILITY);
            event.insertAfter(cinderLogStack, BarrenBlocks.EMBER_LOG.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.EMBER_LOG.toStack(), BarrenBlocks.CINDER_LEAVES.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.CINDER_LEAVES.toStack(), BarrenBlocks.EMBER_LEAVES.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.EMBER_LEAVES.toStack(), BarrenBlocks.CINDER_SAPLING.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.CINDER_SAPLING.toStack(), BarrenBlocks.EMBER_SAPLING.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.EMBER_SAPLING.toStack(), BarrenBlocks.QUICKSAND.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.QUICKSAND.toStack(), BarrenBlocks.RED_QUICKSAND.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.RED_QUICKSAND.toStack(), BarrenBlocks.QUARTZ_SAND.toStack(), VISIBILITY);
            event.insertAfter(BarrenBlocks.QUARTZ_SAND.toStack(), BarrenBlocks.TINY_CACTUS.toStack(), VISIBILITY);
        }
    }

    private static void appendWoodSet(BuildCreativeModeTabContentsEvent event, ItemStack anchor, WoodFamily family) {
        ItemStack previous = anchor;
        for (DeferredBlock<? extends Block> block : family.buildingBlocks()) {
            ItemStack stack = block.toStack();
            event.insertAfter(previous, stack, VISIBILITY);
            previous = stack;
        }
    }

    private BarrenCreativeTabs() {
    }
}
