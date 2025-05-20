package org.polaris2023.ww_deco;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(WWDecoMod.MODID)
@EventBusSubscriber(modid = WWDecoMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WWDecoMod {
    public static final String MODID = "ww_deco";

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {

    }

}
