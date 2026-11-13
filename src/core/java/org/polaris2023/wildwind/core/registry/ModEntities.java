package org.polaris2023.wildwind.core.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.WildWindCore;

import java.util.function.Supplier;

/**
 * 核心模块的实体类型注册。
 *
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(WildWindCore.MOD_ID);

    /** 杜鹃木船 */
    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> AZALEA_BOAT =
            ENTITY_TYPES.registerEntityType("azalea_boat", boatFactory(ModItems.AZALEA_BOAT::get), MobCategory.MISC,
                    builder -> builder.noLootTable()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10));

    /** 杜鹃木运输船 */
    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> AZALEA_CHEST_BOAT =
            ENTITY_TYPES.registerEntityType("azalea_chest_boat", chestBoatFactory(ModItems.AZALEA_CHEST_BOAT::get), MobCategory.MISC,
                    builder -> builder.noLootTable()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10));

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }

    private static EntityType.EntityFactory<Boat> boatFactory(Supplier<Item> dropItem) {
        return (entityType, level) -> new Boat(entityType, level, dropItem);
    }

    private static EntityType.EntityFactory<ChestBoat> chestBoatFactory(Supplier<Item> dropItem) {
        return (entityType, level) -> new ChestBoat(entityType, level, dropItem);
    }

    private ModEntities() {
    }
}
