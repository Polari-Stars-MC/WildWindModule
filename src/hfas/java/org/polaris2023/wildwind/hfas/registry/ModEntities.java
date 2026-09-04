package org.polaris2023.wildwind.hfas.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.client.entity.renderer.MudcrabRenderer;
import org.polaris2023.wildwind.hfas.entity.animal.Mudcrab;

@EventBusSubscriber
public class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(HFASMod.MOD_ID);

    private ModEntities() {
    }

    //泥沼蟹实体类型
    public static final DeferredHolder<EntityType<?>, EntityType<Mudcrab>> MUDCRAB =
            ENTITY_TYPES.registerEntityType("mudcrab", Mudcrab::new, MobCategory.WATER_CREATURE,
                    builder -> builder.sized(0.5F, 0.55F).clientTrackingRange(8)
            );

    /**
     * 向模组事件总线注册实体类型喵~
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }

    /**
     * 实体客户端事件处理器
     */
    @EventBusSubscriber(value = Dist.CLIENT)
    public static class EntitiesClientEvent {
        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.MUDCRAB.get(), MudcrabRenderer::new);
        }
    }

    /**
     * 实体服务端事件处理器
     */
    @EventBusSubscriber
    public static class EntitiesSeverEvent {

        @SubscribeEvent
        static void onAttributeCreate(EntityAttributeCreationEvent event) {
            event.put(ModEntities.MUDCRAB.get(), Mudcrab.createAttributes().build());
        }

        @SubscribeEvent
        static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
            event.register(MUDCRAB.get(), Mudcrab.SPAWN_PLACEMENT, Heightmap.Types.OCEAN_FLOOR, Mudcrab::checkMudcrabInWaterGroundSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
            event.register(MUDCRAB.get(), Mudcrab::checkMudcrabOnGroundSpawnRules);
        }

        private EntitiesSeverEvent() {
        }
    }
}
