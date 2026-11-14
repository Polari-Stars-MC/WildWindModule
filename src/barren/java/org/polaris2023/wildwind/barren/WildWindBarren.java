package org.polaris2023.wildwind.barren;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.registry.BarrenEntities;
import org.polaris2023.wildwind.barren.registry.BarrenItems;
import org.polaris2023.wildwind.barren.registry.ModCreativeTabs;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;

/**
 * 原野之风：瘠土更新（Wild Wind: Barren Update）
 * <p>
 * 砂岩 / 沙 / 流沙 / 焦土 / 下界木质套件。
 *
 * @author safe_saffawa
 */
@Mod(WildWindBarren.MOD_ID)
public class WildWindBarren {
    public static final String MOD_ID = "ww_barren";

    public WildWindBarren(IEventBus bus) {
        // 实体先于物品注册：船物品在构造时需要拿到实体类型
        BarrenEntities.register(bus);
        BarrenBlocks.register(bus);
        BarrenItems.register(bus);
        bus.addListener(BlockEntityTypeAddBlocksEvent.class, BarrenBlocks::addBlockEntityBlocks);
        ModCreativeTabs.register(bus);
    }

    /**
     * 本模块所有资源 ID 的唯一出口，返回跨版本稳定的 {@link ImpId}。
     * 需要原生类型时调用 {@code id(...).unwrap()}。
     */
    public static ImpId id(String path) {
        return ImpIds.idOf(MOD_ID, path);
    }
}
