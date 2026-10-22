package org.polaris2023.wildwind.hfas.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.registry.ModEntities;

@EventBusSubscriber(modid = HFASMod.MOD_ID, value = Dist.CLIENT)
public final class ModEntityRenderers {
    private ModEntityRenderers() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MUDDY_PIG.get(), MuddyPigRenderer::new);
        event.registerEntityRenderer(ModEntities.REACH_ARROW.get(), net.minecraft.client.renderer.entity.TippableArrowRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MuddyPigRenderer.ADULT, MuddyPigModel::adultLayer);
        event.registerLayerDefinition(MuddyPigRenderer.BABY, MuddyPigModel::babyLayer);
        event.registerLayerDefinition(MuddyPigRenderer.SADDLE, MuddyPigModel::saddleLayer);
    }
}
