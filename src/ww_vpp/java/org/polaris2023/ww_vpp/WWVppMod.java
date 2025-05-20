package org.polaris2023.ww_vpp;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(WWVppMod.MODID)
@EventBusSubscriber(modid = WWVppMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WWVppMod {
    public static final String MODID = "ww_vpp";

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {

    }
}
