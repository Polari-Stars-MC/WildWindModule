package org.polaris2023.wildwind.barren.event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.polaris2023.wildwind.core.registry.ModCreativeTabs;
import org.polaris2023.wildwind.barren.BarrenMod;
import org.polaris2023.wildwind.barren.block.ModBlocks;

@EventBusSubscriber(modid = BarrenMod.MOD_ID)
public final class CreativeContents {
    @SubscribeEvent public static void populate(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(ModCreativeTabs.WILD_WIND.getKey())) return;
        ModBlocks.ITEMS.getEntries().forEach(item -> event.accept(item.get()));

    }
}
