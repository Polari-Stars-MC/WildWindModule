package org.polaris2023.wildwind.barren.registry;

import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.barren.WildWindBarren;

/**
 * 瘠土更新的非方块物品注册：焚烬木 / 灵焰木的船与运输船。
 *
 * @author safe_saffawa
 */
public final class BarrenItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WildWindBarren.MOD_ID);

    public static final DeferredItem<BoatItem> CINDER_BOAT = ITEMS.registerItem(
            "cinder_boat",
            p -> new BoatItem(BarrenEntities.CINDER_BOAT.get(), p),
            () -> new Item.Properties().stacksTo(1)
    );

    public static final DeferredItem<BoatItem> CINDER_CHEST_BOAT = ITEMS.registerItem(
            "cinder_chest_boat",
            p -> new BoatItem(BarrenEntities.CINDER_CHEST_BOAT.get(), p),
            () -> new Item.Properties().stacksTo(1)
    );

    public static final DeferredItem<BoatItem> EMBER_BOAT = ITEMS.registerItem(
            "ember_boat",
            p -> new BoatItem(BarrenEntities.EMBER_BOAT.get(), p),
            () -> new Item.Properties().stacksTo(1)
    );

    public static final DeferredItem<BoatItem> EMBER_CHEST_BOAT = ITEMS.registerItem(
            "ember_chest_boat",
            p -> new BoatItem(BarrenEntities.EMBER_CHEST_BOAT.get(), p),
            () -> new Item.Properties().stacksTo(1)
    );

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private BarrenItems() {
    }
}
