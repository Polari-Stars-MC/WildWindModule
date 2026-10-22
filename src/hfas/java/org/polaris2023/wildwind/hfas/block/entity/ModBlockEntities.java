package org.polaris2023.wildwind.hfas.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 方块实体注册类
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HFASMod.MOD_ID);

    // 制箭台方块实体
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FletchingTableBlockEntity>> FLETCHING_TABLE =
            BLOCK_ENTITIES.register(
                    "fletching_table",
                    () -> new BlockEntityType<>(
                            FletchingTableBlockEntity::new,
                            ModBlocks.FLETCHING_TABLE.get()
                    )
            );

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
        bus.addListener(ModBlockEntities::registerCapabilities);
        bus.addListener(ModBlockEntities::addVanillaBlocks);
    }

    private static void addVanillaBlocks(net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent event) {
        event.modify(net.minecraft.world.level.block.entity.BlockEntityTypes.SIGN, ModBlocks.AZALEA_SIGN.get(), ModBlocks.AZALEA_WALL_SIGN.get());
        event.modify(net.minecraft.world.level.block.entity.BlockEntityTypes.HANGING_SIGN, ModBlocks.AZALEA_HANGING_SIGN.get(), ModBlocks.AZALEA_WALL_HANGING_SIGN.get());
        event.modify(net.minecraft.world.level.block.entity.BlockEntityTypes.SHELF, ModBlocks.AZALEA_SHELF.get());
    }

    private static void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(net.neoforged.neoforge.capabilities.Capabilities.Item.BLOCK, KILN.get(),
                net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper::new);
    }

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KilnBlockEntity>> KILN =
            BLOCK_ENTITIES.register("kiln", () -> new BlockEntityType<>(KilnBlockEntity::new, ModBlocks.KILN.get()));
}
