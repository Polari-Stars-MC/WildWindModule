package org.polaris2023.wildwind.hfas.event;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 燃料事件处理器
 * 为木质方块添加燃料功能
 *
 * @author baka4n
 * @since 2026/04/15
 */
@EventBusSubscriber(modid = HFASMod.MOD_ID)
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
        if (isAzaleaWoodItem(item)) return 300;

        return 0;
    }

    private static boolean isBlazeWoodItem(Item item) {
        return item == ModBlocks.BLAZE_LOG.get().asItem()
                || item == ModBlocks.BLAZE_WOOD.get().asItem()
                || item == ModBlocks.STRIPPED_BLAZE_LOG.get().asItem()
                || item == ModBlocks.STRIPPED_BLAZE_WOOD.get().asItem()
                || item == ModBlocks.BLAZE_PLANKS.get().asItem()
                || item == ModBlocks.BLAZE_SLAB.get().asItem()
                || item == ModBlocks.BLAZE_FENCE.get().asItem()
                || item == ModBlocks.BLAZE_FENCE_GATE.get().asItem();
    }

    private static boolean isSoulWoodItem(Item item) {
        return item == ModBlocks.SOUL_LOG.get().asItem()
                || item == ModBlocks.SOUL_WOOD.get().asItem()
                || item == ModBlocks.STRIPPED_SOUL_LOG.get().asItem()
                || item == ModBlocks.STRIPPED_SOUL_WOOD.get().asItem()
                || item == ModBlocks.SOUL_PLANKS.get().asItem()
                || item == ModBlocks.SOUL_SLAB.get().asItem()
                || item == ModBlocks.SOUL_FENCE.get().asItem()
                || item == ModBlocks.SOUL_FENCE_GATE.get().asItem();
    }

    private static boolean isAzaleaWoodItem(Item item) {
        return item == ModBlocks.AZALEA_LOG.get().asItem()
                || item == ModBlocks.AZALEA_WOOD.get().asItem()
                || item == ModBlocks.STRIPPED_AZALEA_LOG.get().asItem()
                || item == ModBlocks.STRIPPED_AZALEA_WOOD.get().asItem()
                || item == ModBlocks.AZALEA_PLANKS.get().asItem()
                || item == ModBlocks.AZALEA_SLAB.get().asItem()
                || item == ModBlocks.AZALEA_FENCE.get().asItem()
                || item == ModBlocks.AZALEA_FENCE_GATE.get().asItem();
    }
}
