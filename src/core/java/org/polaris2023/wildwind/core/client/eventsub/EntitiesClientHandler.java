package org.polaris2023.wildwind.core.client.eventsub;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.registry.ModEntities;

/**
 * 核心模块的实体渲染器注册。
 *
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
@EventBusSubscriber(modid = WildWindCore.MOD_ID, value = Dist.CLIENT)
public final class EntitiesClientHandler {

    private static final ModelLayerLocation AZALEA_BOAT_LAYER =
            new ModelLayerLocation(WildWindCore.id("boat/azalea"), "main");
    private static final ModelLayerLocation AZALEA_CHEST_BOAT_LAYER =
            new ModelLayerLocation(WildWindCore.id("chest_boat/azalea"), "main");

    @SubscribeEvent
    static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AZALEA_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(AZALEA_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.AZALEA_BOAT.get(), context -> new BoatRenderer(context, AZALEA_BOAT_LAYER));
        event.registerEntityRenderer(ModEntities.AZALEA_CHEST_BOAT.get(), context -> new BoatRenderer(context, AZALEA_CHEST_BOAT_LAYER));
    }

    private EntitiesClientHandler() {
    }
}
