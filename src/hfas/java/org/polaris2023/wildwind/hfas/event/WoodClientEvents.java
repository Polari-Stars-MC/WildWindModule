package org.polaris2023.wildwind.hfas.event;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.entity.ModBoatEntities;

@EventBusSubscriber(modid = HFASMod.MOD_ID, value = Dist.CLIENT)
public final class WoodClientEvents {
    public static final ModelLayerLocation BLAZE_BOAT_LAYER = layer("boat/blaze");
    public static final ModelLayerLocation BLAZE_CHEST_BOAT_LAYER = layer("chest_boat/blaze");
    public static final ModelLayerLocation SOUL_BOAT_LAYER = layer("boat/soul");
    public static final ModelLayerLocation SOUL_CHEST_BOAT_LAYER = layer("chest_boat/soul");
    public static final ModelLayerLocation AZALEA_BOAT_LAYER = layer("boat/azalea");
    public static final ModelLayerLocation AZALEA_CHEST_BOAT_LAYER = layer("chest_boat/azalea");

    private WoodClientEvents() {
    }

    private static ModelLayerLocation layer(String path) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, path), "main");
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BLAZE_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(BLAZE_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
        event.registerLayerDefinition(SOUL_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(SOUL_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
        event.registerLayerDefinition(AZALEA_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(AZALEA_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModBoatEntities.BLAZE_BOAT.get(), context -> new BoatRenderer(context, BLAZE_BOAT_LAYER));
        event.registerEntityRenderer(ModBoatEntities.BLAZE_CHEST_BOAT.get(), context -> new BoatRenderer(context, BLAZE_CHEST_BOAT_LAYER));
        event.registerEntityRenderer(ModBoatEntities.SOUL_BOAT.get(), context -> new BoatRenderer(context, SOUL_BOAT_LAYER));
        event.registerEntityRenderer(ModBoatEntities.SOUL_CHEST_BOAT.get(), context -> new BoatRenderer(context, SOUL_CHEST_BOAT_LAYER));
        event.registerEntityRenderer(ModBoatEntities.AZALEA_BOAT.get(), context -> new BoatRenderer(context, AZALEA_BOAT_LAYER));
        event.registerEntityRenderer(ModBoatEntities.AZALEA_CHEST_BOAT.get(), context -> new BoatRenderer(context, AZALEA_CHEST_BOAT_LAYER));
    }
}
