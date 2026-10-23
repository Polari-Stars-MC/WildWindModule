package org.polaris2023.wildwind.wilderness.event;

import java.util.ArrayList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import org.polaris2023.wildwind.wilderness.WildernessMod;
import org.polaris2023.wildwind.wilderness.mixin.LootPoolEntriesAccessor;
import org.polaris2023.wildwind.wilderness.mixin.LootTablePoolsAccessor;
import org.polaris2023.wildwind.wilderness.registry.ModPotions;

@EventBusSubscriber(modid = WildernessMod.MOD_ID)
public final class AzaleaAndArrowEvents {
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerProjectileBehavior(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get());
        });
    }

    @SubscribeEvent
    public static void gifts(LootTableLoadEvent event) {
        if (!event.getName().equals(Identifier.withDefaultNamespace("gameplay/hero_of_the_village/fletcher_gift"))) return;
        var pools = ((LootTablePoolsAccessor) event.getTable()).wildwind$pools();
        if (pools.isEmpty()) return;
        // Extend the existing random choice, preserving other mods' entries and the original number of rolls.
        var pool = (LootPoolEntriesAccessor) pools.getFirst();
        var entries = new ArrayList<>(pool.wildwind$entries());
        entries.add(LootItem.lootTableItem(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get())
                .apply(SetPotionFunction.setPotion(ModPotions.REACH)).build());
        pool.wildwind$setEntries(java.util.List.copyOf(entries));
    }

}
