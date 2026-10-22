package org.polaris2023.wildwind.hfas.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.polaris2023.wildwind.hfas.enchantment.MeltingEnchantment;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;

@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HFASMod.MOD_ID);
    /**
     * 模组专用创造模式物品栏喵~
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WILD_WIND =
            CREATIVE_TABS.register("wild_wind", () -> CreativeModeTab.builder()
                    .title(Component.translatable("mod.wild_wind.name"))
                    .icon(() -> ModItems.MUDCRAB_BUCKET.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        addStacks(output);
                        output.accept(EnchantmentHelper.createBook(new EnchantmentInstance(
                                params.holders().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(MeltingEnchantment.MELTING), 1)));
                    })
                    .build()
            );

    /**
     * 向模组事件总线注册创造模式物品栏
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        CREATIVE_TABS.register(modBus);
    }

    @SubscribeEvent
    public static void keepModContentsInOwnTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().getType() != CreativeModeTab.Type.CATEGORY
                || !event.getTabKey().identifier().getNamespace().equals("minecraft")) return;
        // Vanilla generators also discover mod potions and enchanted books automatically.
        event.removeIf(stack -> {
            if (BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals(HFASMod.MOD_ID)) return true;
            var potion = stack.get(DataComponents.POTION_CONTENTS);
            if (potion != null && potion.potion().isPresent()
                    && potion.potion().get().unwrapKey().orElseThrow().identifier().getNamespace().equals(HFASMod.MOD_ID)) return true;
            var enchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
            return enchantments != null && enchantments.keySet().stream().anyMatch(holder -> holder.is(MeltingEnchantment.MELTING));
        }, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void addStacks(CreativeModeTab.Output output){
        ModBlocks.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
        ModItems.ITEMS.getEntries().stream().filter(item -> item != ModItems.TIPPED_ARROW).forEach(item -> output.accept(item.get()));
        ModPotions.POTIONS.getEntries().forEach(potion -> {
            output.accept(PotionContents.createItemStack(Items.POTION, potion));
            output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, potion));
            output.accept(PotionContents.createItemStack(Items.LINGERING_POTION, potion));
            output.accept(org.polaris2023.wildwind.hfas.arrow.ReachArrows.convert(PotionContents.createItemStack(Items.TIPPED_ARROW, potion)));
        });
    }
}
