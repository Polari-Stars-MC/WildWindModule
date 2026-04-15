package org.polaris2023.wildwind.hfas.datagen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.datagen.provider.*;

@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class HFASDatagen {
    @SubscribeEvent
    private static void gatherClient(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        // 语言文件
        event.createProvider(EnUsLanguageProvider::new);
        event.createProvider(ZhCnLanguageProvider::new);
    }

    @SubscribeEvent
    private static void gatherServer(GatherDataEvent.Server event) {
        event.createProvider(ModLootProvider::new);
        event.createProvider(ModDatapackProvider::new);
        event.createProvider(ModDataMapsProvider::new);
    }
}
