package org.polaris2023.wildwind.core.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.imp.ImpLegacy;

/**
 * 核心模块的物品注册。
 *
 * <p>刷怪蛋的构造方式两族不同（旧版颜色是构造器参数，26.x 改由
 * {@code Item.Properties.spawnEgg} 承载），走 {@link ImpLegacy#spawnEgg}。</p>
 *
 * @author safe_saffawa
 */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WildWindCore.MOD_ID);

    /**
     * 幼蛛刷怪蛋。
     *
     * <p>颜色对齐原版蜘蛛刷怪蛋（暗灰底 + 深灰斑点）。</p>
     */
    public static final DeferredItem<SpawnEggItem> SPIDERLING_SPAWN_EGG =
            ITEMS.registerItem("spiderling_spawn_egg",
                    p -> ImpLegacy.spawnEgg(p, ModEntities.SPIDERLING::value, 0x2B2B2B, 0x8B5A2B));

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private ModItems() {
    }
}
