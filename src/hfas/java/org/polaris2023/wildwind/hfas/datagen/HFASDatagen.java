package org.polaris2023.wildwind.hfas.datagen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.datagen.provider.ModLootProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.ModModelProvider;

@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class HFASDatagen {
    @SubscribeEvent
    private static void gatherClient(GatherDataEvent.Client event) {
//        var generator = event.getGenerator();
//        var output = generator.getPackOutput();
        event.createProvider(ModModelProvider::new);
    }

    @SubscribeEvent
    private static void gatherServer(GatherDataEvent.Server event) {
        event.createProvider(ModLootProvider::new);
    }

}
