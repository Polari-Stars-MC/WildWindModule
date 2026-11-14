package org.polaris2023.wildwind.barren.client.eventsub;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.registry.BarrenEntities;

/**
 * 瘠土更新的客户端注册：焚烬木 / 灵焰木的船与运输船渲染。
 *
 * @author safe_saffawa
 */
@EventBusSubscriber(modid = WildWindBarren.MOD_ID, value = Dist.CLIENT)
public final class BarrenClientEvents {

    private static final ModelLayerLocation CINDER_BOAT_LAYER =
            new ModelLayerLocation(WildWindBarren.id("boat/cinder").unwrap(), "main");
    private static final ModelLayerLocation CINDER_CHEST_BOAT_LAYER =
            new ModelLayerLocation(WildWindBarren.id("chest_boat/cinder").unwrap(), "main");
    private static final ModelLayerLocation EMBER_BOAT_LAYER =
            new ModelLayerLocation(WildWindBarren.id("boat/ember").unwrap(), "main");
    private static final ModelLayerLocation EMBER_CHEST_BOAT_LAYER =
            new ModelLayerLocation(WildWindBarren.id("chest_boat/ember").unwrap(), "main");

    @SubscribeEvent
    static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CINDER_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(CINDER_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
        event.registerLayerDefinition(EMBER_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(EMBER_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BarrenEntities.CINDER_BOAT.get(), context -> new BoatRenderer(context, CINDER_BOAT_LAYER));
        event.registerEntityRenderer(BarrenEntities.CINDER_CHEST_BOAT.get(), context -> new BoatRenderer(context, CINDER_CHEST_BOAT_LAYER));
        event.registerEntityRenderer(BarrenEntities.EMBER_BOAT.get(), context -> new BoatRenderer(context, EMBER_BOAT_LAYER));
        event.registerEntityRenderer(BarrenEntities.EMBER_CHEST_BOAT.get(), context -> new BoatRenderer(context, EMBER_CHEST_BOAT_LAYER));
    }

    private BarrenClientEvents() {
    }
}
