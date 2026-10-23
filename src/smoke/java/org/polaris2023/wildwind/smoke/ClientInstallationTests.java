package org.polaris2023.wildwind.smoke;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/** Opt-in client resource-load check; never packaged in a release mod. */
@EventBusSubscriber(modid = "wild_wind_smoke", value = Dist.CLIENT)
public final class ClientInstallationTests {
    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        if (!Boolean.getBoolean("wildwind.clientSmoke")) return;
        var client = Minecraft.getInstance();
        if (client.gui.overlay() != null || !(client.gui.screen() instanceof TitleScreen)) return;
        require(client, "wild_wind_core:textures/entity/signs/azalea.png");
        if (ModList.get().isLoaded("wild_wind_wilderness")) {
            require(client, "wild_wind_wilderness:textures/entity/pig/pig_muddy.png");
            require(client, "wild_wind_wilderness:textures/entity/pig/pig_muddy_baby.png");
            require(client, "wild_wind_wilderness:textures/entity/piranha.png");
            require(client, "wild_wind_wilderness:geckolib/models/entity/mudcrab.geo.json");
        }
        LogUtils.getLogger().info("Wild Wind client installation check passed");
        client.stop();
    }

    private static void require(Minecraft client, String id) {
        if (client.getResourceManager().getResource(Identifier.parse(id)).isEmpty()) {
            throw new IllegalStateException("Missing module resource: " + id);
        }
    }
}
