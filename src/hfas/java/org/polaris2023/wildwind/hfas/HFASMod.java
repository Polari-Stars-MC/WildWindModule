package org.polaris2023.wildwind.hfas;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.entity.ModBlockEntities;
import org.polaris2023.wildwind.hfas.config.ModCommonConfig;
import org.polaris2023.wildwind.hfas.entity.ModBoatEntities;
import org.polaris2023.wildwind.hfas.entity.animal.MudcrabVariant;
import org.polaris2023.wildwind.hfas.menu.ModMenus;
import org.polaris2023.wildwind.hfas.registry.*;

@Mod(HFASMod.MOD_ID)
@Slf4j
@EventBusSubscriber
public class HFASMod {
    public static final String MOD_ID = "ww_hfas";

    public HFASMod(IEventBus bus, ModContainer container) {
        ModBlocks.register(bus);

        ModBoatEntities.register(bus);

        bus.addListener(HFASMod::addWoodSignBlocks);

        ModBlockEntities.register(bus);

        ModMenus.register(bus);

        ModDataComponents.register(bus);

        ModAttributes.register(bus);

        container.registerConfig(
                net.neoforged.fml.config.ModConfig.Type.COMMON,
                ModCommonConfig.SPEC
        );

        ModItems.register(bus);

        ModCreativeTabs.register(bus);

        ModEntityDataSerializers.register(bus);

        ModEntities.register(bus);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent
    public static void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                DatadrivenRegistryKey.MUDCRAB_VARIANT,
                MudcrabVariant.DIRECT_CODEC,
                MudcrabVariant.NETWORK_CODEC
        );
    }

    private static void addWoodSignBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(
                BlockEntityTypes.SIGN,
                ModBlocks.BLAZE_SIGN.get(),
                ModBlocks.BLAZE_WALL_SIGN.get(),
                ModBlocks.SOUL_SIGN.get(),
                ModBlocks.SOUL_WALL_SIGN.get(),
                ModBlocks.AZALEA_SIGN.get(),
                ModBlocks.AZALEA_WALL_SIGN.get()
        );

        event.modify(
                BlockEntityTypes.HANGING_SIGN,
                ModBlocks.BLAZE_HANGING_SIGN.get(),
                ModBlocks.BLAZE_WALL_HANGING_SIGN.get(),
                ModBlocks.SOUL_HANGING_SIGN.get(),
                ModBlocks.SOUL_WALL_HANGING_SIGN.get(),
                ModBlocks.AZALEA_HANGING_SIGN.get(),
                ModBlocks.AZALEA_WALL_HANGING_SIGN.get()
        );
    }
}