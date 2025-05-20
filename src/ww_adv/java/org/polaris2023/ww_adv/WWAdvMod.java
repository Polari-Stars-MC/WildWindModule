package org.polaris2023.ww_adv;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(WWAdvMod.MODID)
@EventBusSubscriber(modid = WWAdvMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WWAdvMod {
    public static final String MODID = "ww_adv";
    public WWAdvMod() {

    }

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {

    }
}
