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
        // Verify conservative server pagination against the actual loaded Minecraft font.
        var pages = org.polaris2023.wildwind.core.inspection.BookPages.paginate(java.util.List.of(
                "原野之风·属性记录", "物品ID:wild_wind_wilderness:muddy_pig_spawn_egg",
                "活塞推动行为:不可推动，不可拉动", "烧炼时间:15秒（300刻）", "空气阻力修正系数:0.01"));
        for (var page : pages) {
            if (client.font.wordWrapHeight(net.minecraft.network.chat.Component.literal(page), 114) > 126) throw new IllegalStateException("Inspector page exceeds book height");
            for (var line : page.split("\n")) {
                if (client.font.width(line) > 114) throw new IllegalStateException("Inspector line exceeds book width: " + line);
            }
        }
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
