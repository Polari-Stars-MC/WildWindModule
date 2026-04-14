package org.polaris2023.wildwind.hfas;

import lombok.extern.slf4j.Slf4j;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.entity.ModBlockEntities;
import org.polaris2023.wildwind.hfas.config.ModCommonConfig;
import org.polaris2023.wildwind.hfas.menu.ModMenus;

@Mod(HFASMod.MOD_ID)
@Slf4j
public class HFASMod {
    public static final String MOD_ID = "ww_hfas";

    public HFASMod(IEventBus bus, ModContainer container) {
        // 注册方块和物品
        ModBlocks.register(bus);
        // 注册方块实体
        ModBlockEntities.register(bus);
        // 注册菜单
        ModMenus.register(bus);
        // 注册配置文件
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModCommonConfig.SPEC);
    }
}
