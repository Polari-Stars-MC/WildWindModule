package org.polaris2023.wildwind.core;

import lombok.extern.slf4j.Slf4j;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.block.entity.ModBlockEntities;
import org.polaris2023.wildwind.core.config.ModCommonConfig;
import org.polaris2023.wildwind.core.menu.ModMenus;
import org.polaris2023.wildwind.core.registry.ModCreativeTabs;
import org.polaris2023.wildwind.core.registry.ModDataComponents;
import org.polaris2023.wildwind.core.registry.ModEntities;
import org.polaris2023.wildwind.core.registry.ModItems;
import org.polaris2023.wildwind.core.registry.ModSoundEvents;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;

/**
 * 原野之风：核心（Wild Wind: Core）
 * <p>
 * 杜鹃木套件 / 制箭台 / 矢术 / 玫瑰 / 幼蛛等基础内容，另两个更新模块均依赖本模块。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
@Mod(WildWindCore.MOD_ID)
@Slf4j
public class WildWindCore {
    public static final String MOD_ID = "ww_core";

    public WildWindCore(IEventBus bus, ModContainer container) {
        ModBlocks.register(bus);
        ModBlockEntities.register(bus);
        ModMenus.register(bus);
        ModDataComponents.register(bus);
        ModSoundEvents.register(bus);

        container.registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC);

        ModItems.register(bus);
        ModCreativeTabs.register(bus);
        ModEntities.register(bus);
    }

    /**
     * 本模块所有资源 ID 的唯一出口。
     *
     * <p>返回跨版本稳定的 {@link ImpId}：ID 的原生类型在 26.x 是 {@code Identifier}、
     * 在 1.20/1.21 是 {@code ResourceLocation}，差异全部收在 imp 内。
     * 需要原生类型时调用 {@code id(...).unwrap()}。</p>
     */
    public static ImpId id(String path) {
        return ImpIds.idOf(MOD_ID, path);
    }
}
