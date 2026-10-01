package org.polaris2023.wildwind.hfas;

import lombok.extern.slf4j.Slf4j;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.entity.ModBlockEntities;
import org.polaris2023.wildwind.hfas.component.ModDataComponents;
import org.polaris2023.wildwind.hfas.config.ModCommonConfig;
import org.polaris2023.wildwind.hfas.entity.ModBoatEntities;
import org.polaris2023.wildwind.hfas.menu.ModMenus;

@Mod(HFASMod.MOD_ID)
@Slf4j
public class HFASMod {
    public static final String MOD_ID = "ww_hfas";

    public HFASMod(IEventBus bus, ModContainer container) {
        // 注册方块和物品
        ModBlocks.register(bus);
        // 注册三套木材的船实体
        ModBoatEntities.register(bus);
        // 让原版告示牌方块实体接受自定义告示牌
        bus.addListener(HFASMod::addWoodSignBlocks);
        // 注册方块实体
        ModBlockEntities.register(bus);
        // 注册菜单
        ModMenus.register(bus);
        // 注册数据组件
        ModDataComponents.register(bus);

        // 注册配置文件
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModCommonConfig.SPEC);
    }

    private static void addWoodSignBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityTypes.SIGN,
                ModBlocks.BLAZE_SIGN.get(), ModBlocks.BLAZE_WALL_SIGN.get(),
                ModBlocks.SOUL_SIGN.get(), ModBlocks.SOUL_WALL_SIGN.get(),
                ModBlocks.AZALEA_SIGN.get(), ModBlocks.AZALEA_WALL_SIGN.get());
        event.modify(BlockEntityTypes.HANGING_SIGN,
                ModBlocks.BLAZE_HANGING_SIGN.get(), ModBlocks.BLAZE_WALL_HANGING_SIGN.get(),
                ModBlocks.SOUL_HANGING_SIGN.get(), ModBlocks.SOUL_WALL_HANGING_SIGN.get(),
                ModBlocks.AZALEA_HANGING_SIGN.get(), ModBlocks.AZALEA_WALL_HANGING_SIGN.get());
    }
}
