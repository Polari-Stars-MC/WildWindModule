package org.polaris2023.ww_ag;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.polaris2023.ww_ag.common.init.ModBlocks;
import org.polaris2023.ww_ag.common.init.ModItems;

@Mod(WWAgMod.modid)
@EventBusSubscriber(modid = WWAgMod.modid, bus = EventBusSubscriber.Bus.MOD)
public class WWAgMod {
    public static final String modid = "ww_ag";
    public static final L2Registrate REGISTRATE =
            new L2Registrate(modid);
    public static ResourceLocation cLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
    public WWAgMod() {
        ModBlocks.register();
        ModItems.register();
    }

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {
        var gen = event.getGenerator();
    }
}
