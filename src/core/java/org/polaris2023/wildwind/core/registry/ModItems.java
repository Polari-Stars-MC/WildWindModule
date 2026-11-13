package org.polaris2023.wildwind.core.registry;

import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.WildWindCore;

/**
 * 核心模块的物品注册。
 *
 * @author safe_saffawa
 */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WildWindCore.MOD_ID);

    /** 杜鹃木船 */
    public static final DeferredItem<BoatItem> AZALEA_BOAT = ITEMS.registerItem(
            "azalea_boat",
            p -> new BoatItem(ModEntities.AZALEA_BOAT.get(), p),
            () -> new Item.Properties().stacksTo(1)
    );

    /** 杜鹃木运输船 */
    public static final DeferredItem<BoatItem> AZALEA_CHEST_BOAT = ITEMS.registerItem(
            "azalea_chest_boat",
            p -> new BoatItem(ModEntities.AZALEA_CHEST_BOAT.get(), p),
            () -> new Item.Properties().stacksTo(1)
    );

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private ModItems() {
    }
}
