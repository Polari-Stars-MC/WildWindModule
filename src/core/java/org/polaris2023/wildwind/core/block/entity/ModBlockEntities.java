package org.polaris2023.wildwind.core.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.imp.ImpBlockEntityTypes;

/**
 * 方块实体注册类
 *
 * <p>{@code BlockEntityType} 的构造器两族不兼容：26.x 是 {@code (supplier, Set<Block>)}
 * 且<b>没有</b> {@code BlockEntityType.Builder}；1.20 / 1.21 直接构造还需要多传一个
 * {@code com.mojang.datafixers.types.Type<?>}，便捷写法是
 * {@code BlockEntityType.Builder.of(supplier, blocks...).build(null)}。
 * 所以这里不直接写 {@code new BlockEntityType<>(...)}，统一走 {@link ImpBlockEntityTypes#create}。</p>
 *
 * @author baka4n
 * @author safe_saffawa
 * @since 2026/04/15
 */
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, WildWindCore.MOD_ID);

    /** 制箭台方块实体 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FletchingTableBlockEntity>> FLETCHING_TABLE =
            BLOCK_ENTITIES.register(
                    "fletching_table",
                    () -> ImpBlockEntityTypes.create(
                            FletchingTableBlockEntity::new,
                            ModBlocks.FLETCHING_TABLE.get()
                    )
            );

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
