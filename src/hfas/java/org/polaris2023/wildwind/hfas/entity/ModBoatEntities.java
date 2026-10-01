package org.polaris2023.wildwind.hfas.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

public final class ModBoatEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(HFASMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> BLAZE_BOAT =
            ENTITY_TYPES.registerEntityType(
                    "blaze_boat",
                    (type, level) -> new Boat(type, level, () -> ModBlocks.BLAZE_BOAT.get()),
                    MobCategory.MISC,
                    builder -> builder.sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10)
            );

    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> BLAZE_CHEST_BOAT =
            ENTITY_TYPES.registerEntityType(
                    "blaze_chest_boat",
                    (type, level) -> new ChestBoat(type, level, () -> ModBlocks.BLAZE_CHEST_BOAT.get()),
                    MobCategory.MISC,
                    builder -> builder.sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10)
            );

    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> SOUL_BOAT =
            ENTITY_TYPES.registerEntityType(
                    "soul_boat",
                    (type, level) -> new Boat(type, level, () -> ModBlocks.SOUL_BOAT.get()),
                    MobCategory.MISC,
                    builder -> builder.sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10)
            );

    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> SOUL_CHEST_BOAT =
            ENTITY_TYPES.registerEntityType(
                    "soul_chest_boat",
                    (type, level) -> new ChestBoat(type, level, () -> ModBlocks.SOUL_CHEST_BOAT.get()),
                    MobCategory.MISC,
                    builder -> builder.sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10)
            );

    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> AZALEA_BOAT =
            ENTITY_TYPES.registerEntityType(
                    "azalea_boat",
                    (type, level) -> new Boat(type, level, () -> ModBlocks.AZALEA_BOAT.get()),
                    MobCategory.MISC,
                    builder -> builder.sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10)
            );

    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> AZALEA_CHEST_BOAT =
            ENTITY_TYPES.registerEntityType(
                    "azalea_chest_boat",
                    (type, level) -> new ChestBoat(type, level, () -> ModBlocks.AZALEA_CHEST_BOAT.get()),
                    MobCategory.MISC,
                    builder -> builder.sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10)
            );

    private ModBoatEntities() {
    }

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
