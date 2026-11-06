package org.polaris2023.wildwind.barren.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.entity.WildWindBoat;
import org.polaris2023.wildwind.barren.entity.WildWindChestBoat;

import java.util.function.Supplier;

/**
 * 瘠土更新的实体类型注册：焚烬木 / 灵焰木的船与运输船。
 *
 * @author safe_saffawa
 */
public final class BarrenEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(WildWindBarren.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> CINDER_BOAT =
            ENTITY_TYPES.registerEntityType("cinder_boat", boatFactory(BarrenItems.CINDER_BOAT::get), MobCategory.MISC,
                    builder -> builder.noLootTable()
                            .fireImmune()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10));

    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> CINDER_CHEST_BOAT =
            ENTITY_TYPES.registerEntityType("cinder_chest_boat", chestBoatFactory(BarrenItems.CINDER_CHEST_BOAT::get), MobCategory.MISC,
                    builder -> builder.noLootTable()
                            .fireImmune()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10));

    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> EMBER_BOAT =
            ENTITY_TYPES.registerEntityType("ember_boat", boatFactory(BarrenItems.EMBER_BOAT::get), MobCategory.MISC,
                    builder -> builder.noLootTable()
                            .fireImmune()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10));

    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> EMBER_CHEST_BOAT =
            ENTITY_TYPES.registerEntityType("ember_chest_boat", chestBoatFactory(BarrenItems.EMBER_CHEST_BOAT::get), MobCategory.MISC,
                    builder -> builder.noLootTable()
                            .fireImmune()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10));

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }

    private static EntityType.EntityFactory<Boat> boatFactory(Supplier<Item> dropItem) {
        return (entityType, level) -> new WildWindBoat(entityType, level, dropItem);
    }

    private static EntityType.EntityFactory<ChestBoat> chestBoatFactory(Supplier<Item> dropItem) {
        return (entityType, level) -> new WildWindChestBoat(entityType, level, dropItem);
    }

    private BarrenEntities() {
    }
}
