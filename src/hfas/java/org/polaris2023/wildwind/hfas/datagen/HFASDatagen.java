package org.polaris2023.wildwind.hfas.datagen;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.polaris2023.wildwind.hfas.HFASMod;

@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class HFASDatagen {
    private static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
    }
}
