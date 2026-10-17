package org.polaris2023.wildwind.hfas.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HFASMod.MOD_ID);
    /**
     * 模组专用创造模式物品栏喵~
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WILD_WIND =
            CREATIVE_TABS.register("wild_wind", () -> CreativeModeTab.builder()
                    .title(Component.translatable("mod.wild_wind.name"))
//                    .icon(() -> ModItems.CINDER.log().get().getDefaultInstance()) TODO 换成对应的物品
                    .icon(() -> ModItems.MUDCRAB_BUCKET.get().getDefaultInstance())
                    .displayItems((params, output) -> addStacks(output))
                    .build()
            );

    /**
     * 向模组事件总线注册创造模式物品栏喵~
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        CREATIVE_TABS.register(modBus);
    }

    private static void addStacks(CreativeModeTab.Output output){
        output.accept(ModItems.MUDCRAB_BUCKET);
        output.accept(ModItems.MUDCRAB_SPAWN_EGG);
        output.accept(ModItems.CRAB_CLAW);
        output.accept(ModItems.OMNI_CLAW);
        output.accept(ModItems.PIRANHA_BUCKET);
        output.accept(ModItems.PIRANHA);
        output.accept(ModItems.COOKED_PIRANHA);
        output.accept(ModItems.FANG);
        output.accept(ModItems.PIRANHA_SPAWN_EGG);
    }
}
