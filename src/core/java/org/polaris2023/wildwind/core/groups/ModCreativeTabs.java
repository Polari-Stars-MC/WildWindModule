package org.polaris2023.wildwind.core.groups;

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
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.block.ModWoodFamilies;
import org.polaris2023.wildwind.core.block.WoodFamily;
import org.polaris2023.wildwind.imp.ImpCreativeTabs;

/**
 * 核心模块的原版物品栏排序。
 *
 * <p>「插到锚点之后」走 {@link ImpCreativeTabs#insertAfter}：1.20.6 的
 * {@code BuildCreativeModeTabContentsEvent} 没有 {@code insertAfter}，
 * 只有 {@code getEntries()} 那张链式表（详见 imp 侧注释）。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author CarrotLegend
 * @author mdjfnfn
 * @author safe_saffawa
 */
@EventBusSubscriber(modid = WildWindCore.MOD_ID)
public final class ModCreativeTabs {

    private static final CreativeModeTab.TabVisibility VISIBILITY = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

    @SubscribeEvent
    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            appendWoodSet(event, new ItemStack(Blocks.CHERRY_BUTTON), ModWoodFamilies.AZALEA);
        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            ItemStack azaleaLogStack = ModBlocks.AZALEA_LOG.toStack();
            ImpCreativeTabs.insertAfter(event, new ItemStack(Items.CHERRY_LOG), azaleaLogStack, VISIBILITY);
            ImpCreativeTabs.insertAfter(event, azaleaLogStack, ModBlocks.ROSE.toStack(), VISIBILITY);
            ImpCreativeTabs.insertAfter(event, ModBlocks.ROSE.toStack(), ModBlocks.SOUL_ROSE.toStack(), VISIBILITY);
            ImpCreativeTabs.insertAfter(event, ModBlocks.SOUL_ROSE.toStack(), ModBlocks.SOUL_ROSE_BUSH.toStack(), VISIBILITY);
            ImpCreativeTabs.insertAfter(event, ModBlocks.SOUL_ROSE_BUSH.toStack(), ModBlocks.WITHER_ROSE_BUSH.toStack(), VISIBILITY);
        }
    }

    private static void appendWoodSet(BuildCreativeModeTabContentsEvent event, ItemStack anchor, WoodFamily family) {
        ItemStack previous = anchor;
        for (DeferredBlock<? extends Block> block : family.buildingBlocks()) {
            ItemStack stack = block.toStack();
            ImpCreativeTabs.insertAfter(event, previous, stack, VISIBILITY);
            previous = stack;
        }
    }

    private ModCreativeTabs() {
    }
}
