package org.polaris2023.wildwind.wilderness.event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.polaris2023.wildwind.core.registry.ModCreativeTabs;
import org.polaris2023.wildwind.wilderness.WildernessMod;
import org.polaris2023.wildwind.wilderness.block.ModBlocks;
import org.polaris2023.wildwind.wilderness.registry.*;

@EventBusSubscriber(modid = WildernessMod.MOD_ID)
public final class CreativeContents {
    @SubscribeEvent public static void populate(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(ModCreativeTabs.WILD_WIND.getKey())) return;
        ModBlocks.ITEMS.getEntries().forEach(item -> event.accept(item.get()));

        ModItems.ITEMS.getEntries().stream().filter(item -> item != ModItems.TIPPED_ARROW).forEach(item -> event.accept(item.get()));
        ModPotions.POTIONS.getEntries().forEach(potion -> {
            event.accept(PotionContents.createItemStack(Items.POTION, potion));
            event.accept(PotionContents.createItemStack(Items.SPLASH_POTION, potion));
            event.accept(PotionContents.createItemStack(Items.LINGERING_POTION, potion));
            event.accept(org.polaris2023.wildwind.wilderness.arrow.ReachArrows.convert(PotionContents.createItemStack(Items.TIPPED_ARROW, potion)));
        });

    }
}
