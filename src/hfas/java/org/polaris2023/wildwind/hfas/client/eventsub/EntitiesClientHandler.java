package org.polaris2023.wildwind.hfas.client.eventsub;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.polaris2023.wildwind.hfas.client.entity.renderer.MudcrabRenderer;
import org.polaris2023.wildwind.hfas.registry.ModEntities;

@EventBusSubscriber(value = Dist.CLIENT)
public class EntitiesClientHandler {
    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MUDCRAB.get(), MudcrabRenderer::new);
    }
}