package org.polaris2023.wildwind.hfas.event;

import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.polaris2023.wildwind.hfas.entity.animal.Mudcrab;
import org.polaris2023.wildwind.hfas.registry.ModEntities;

@EventBusSubscriber
public class EntitiesHandler {
    @SubscribeEvent
    static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MUDCRAB.get(), Mudcrab.createAttributes().build());
    }

    @SubscribeEvent
    static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.MUDCRAB.get(), Mudcrab.SPAWN_PLACEMENT, Heightmap.Types.OCEAN_FLOOR, Mudcrab::checkMudcrabInWaterGroundSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.MUDCRAB.get(), Mudcrab::checkMudcrabOnGroundSpawnRules);
    }
}
