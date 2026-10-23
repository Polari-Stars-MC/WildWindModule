package org.polaris2023.wildwind.wilderness.event;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.polaris2023.wildwind.wilderness.WildernessMod;

/**
 * 燃料事件处理器
 * 为木质方块添加燃料功能
 *
 * @author baka4n
 * @since 2026/04/15
 */
@EventBusSubscriber(modid = WildernessMod.MOD_ID)
public class FuelHandler {

    /**
     * 燃料燃烧时间事件
     * 木质方块可作为燃料，燃烧300 tick（与原版木板相同）
     */
    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        Item item = event.getItemStack().getItem();

        // 检查是否是木质方块物品
        int burnTime = getWoodBurnTime(item);
        if (burnTime > 0) {
            event.setBurnTime(burnTime);
        }
    }

    /**
     * 获取木质方块的燃烧时间
     */
    private static int getWoodBurnTime(Item item) {
        // 焚烬木 - 300 tick（15秒）
        if (isBlazeWoodItem(item)) return 300;
        // 灵焰木 - 300 tick
        if (isSoulWoodItem(item)) return 300;
        // 杜鹃木 - 300 tick

        return 0;
    }

    private static boolean isBlazeWoodItem(Item item) {
        return item == org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PLANKS.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SLAB.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE_GATE.get().asItem();
    }

    private static boolean isSoulWoodItem(Item item) {
        return item == org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PLANKS.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SLAB.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE.get().asItem()
                || item == org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE_GATE.get().asItem();
    }

}
