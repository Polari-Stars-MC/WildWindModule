package org.polaris2023.wildwind.core.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.CoreMod;

/**
 * 菜单类型注册类
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, CoreMod.MOD_ID);

    // 制箭台菜单


    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }

    public static final DeferredHolder<MenuType<?>, MenuType<KilnMenu>> KILN =
            MENUS.register("kiln", () -> new MenuType<>(KilnMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
