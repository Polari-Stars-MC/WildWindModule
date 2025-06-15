package org.polaris2023.ww_deco;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.polaris2023.ww_ag.common.init.ModTabs;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_deco.common.init.ModBlocks;



@Mod(WWDecoMod.MODID)
@EventBusSubscriber(modid = WWDecoMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WWDecoMod {
    public static final String MODID = "ww_deco";
    public static final WWRegistrate REGISTRATE = new WWRegistrate(MODID);

    public WWDecoMod(IEventBus modBus, ModContainer container) {
        ModBlocks.register();

    }

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {

    }

}
