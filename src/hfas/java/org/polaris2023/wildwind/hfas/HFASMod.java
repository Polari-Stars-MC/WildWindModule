package org.polaris2023.wildwind.hfas;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.entity.ModBlockEntities;
import org.polaris2023.wildwind.hfas.config.ModCommonConfig;
import org.polaris2023.wildwind.hfas.entity.animal.MudcrabVariant;
import org.polaris2023.wildwind.hfas.menu.ModMenus;
import org.polaris2023.wildwind.hfas.registry.*;

@Mod(HFASMod.MOD_ID)
@Slf4j
@EventBusSubscriber
public class HFASMod {
    //TODO 对于该模块化项目，考虑分配专属ID
    public static final String MOD_ID = "ww_hfas";

    public HFASMod(IEventBus bus, ModContainer container) {
        // 注册方块和物品
        ModBlocks.register(bus);
        // 注册方块实体
        ModBlockEntities.register(bus);
        // 注册菜单
        ModMenus.register(bus);
        // 注册数据组件
        ModDataComponents.register(bus);

        // 注册配置文件
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModCommonConfig.SPEC);

        //注册物品
        ModItems.register(bus);
        //注册创造模式物品栏
        ModCreativeTabs.register(bus);

        //-----[实体相关]-----
        //实体信息同步器
        ModEntityDataSerializers.register(bus);
        //注册实体类型
        ModEntities.register(bus);
    }

    public static Identifier id(String path){
        return Identifier.fromNamespaceAndPath(MOD_ID,path);
    }

    //数据驱动项注册
    @SubscribeEvent
    public static void datapackRegistry(DataPackRegistryEvent.NewRegistry event){
        event.dataPackRegistry(DatadrivenRegistryKey.MUDCRAB_VARIANT, MudcrabVariant.DIRECT_CODEC, MudcrabVariant.NETWORK_CODEC);
    }
}
