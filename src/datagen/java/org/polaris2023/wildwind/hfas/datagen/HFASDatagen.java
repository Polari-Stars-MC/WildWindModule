package org.polaris2023.wildwind.hfas.datagen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.datagen.provider.ModDataMapsProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.ModDatapackProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.ModLootProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.ModModelProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.ModRecipeProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.lang.EnUsLanguageProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.lang.ZhCnLanguageProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModBiomeTagsProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModBlockTagsProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModEntityTypeTagsProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModItemTagsProvider;

@EventBusSubscriber(modid = HFASMod.MOD_ID)
public final class HFASDatagen {
    @SubscribeEvent
    private static void gatherClient(GatherDataEvent.Client event) {
        for (var module : ReleaseModule.values()) {
            var output = module.output(event.getGenerator().getPackOutput());
            add(event, module, new ModModelProvider(output, module));
            add(event, module, new EnUsLanguageProvider(output, module));
            add(event, module, new ZhCnLanguageProvider(output, module));
        }
    }

    @SubscribeEvent
    private static void gatherServer(GatherDataEvent.Server event) {
        for (var module : ReleaseModule.values()) {
            var output = module.output(event.getGenerator().getPackOutput());
            var lookup = event.getLookupProvider();
            add(event, module, new ModBlockTagsProvider(output, lookup, module));
            add(event, module, new ModItemTagsProvider(output, lookup, module));
            add(event, module, new ModLootProvider(output, lookup, module));
            add(event, module, new ModDatapackProvider(output, lookup, module));
            add(event, module, new ModDataMapsProvider(output, lookup, module));
            add(event, module, new ModRecipeProvider.Runner(output, lookup, module));
            if (module == ReleaseModule.WILDERNESS) {
                add(event, module, new ModEntityTypeTagsProvider(output, lookup));
                add(event, module, new ModBiomeTagsProvider(output, lookup));
            }
        }
    }

    /** Cache keys must distinguish identical provider types belonging to separate packs. */
    private static void add(GatherDataEvent event, ReleaseModule module, DataProvider provider) {
        event.addProvider(new DataProvider() {
            @Override
            public CompletableFuture<?> run(CachedOutput output) {
                return provider.run(output);
            }

            @Override
            public String getName() {
                return module.id() + ": " + provider.getName();
            }
        });
    }
}
