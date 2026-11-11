package org.polaris2023.wildwind.core.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.config.ModCommonConfig;

/**
 * 制箭台替换处理器。
 * <p>
 * 玩家放置由 {@code BlockItemMixin} 直接换掉放置的方块，这里只负责
 * 把已生成世界（区块加载时）里的原版制箭台换成模组制箭台。
 *
 * @author baka4n
 * @author safe_saffawa
 * @since 2026/04/15
 */
@EventBusSubscriber(modid = WildWindCore.MOD_ID)
public class FletchingTableReplaceHandler {

    /**
     * 区块加载事件 - 替换区块内的原版制箭台
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!ModCommonConfig.shouldReplaceVanillaFletchingTable()) {
            return;
        }

        LevelChunk chunk = event.getChunk();
        LevelAccessor level = event.getLevel();

        if (!level.isClientSide()) {
            BlockPos.betweenClosedStream(new AABB(
                    chunk.getPos().getMinBlockX(), level.getMinY(), chunk.getPos().getMinBlockZ(),
                    chunk.getPos().getMaxBlockX(), 200, chunk.getPos().getMaxBlockZ()
                    )).forEach(pos -> {
                BlockState state = chunk.getBlockState(pos);
                if (state.is(Blocks.FLETCHING_TABLE)) {
                    BlockState replaced = ModBlocks.FLETCHING_TABLE.get().defaultBlockState();
                    for (Property.Value<?> value : state.getValues().toList()) {
                        replaced = copyProperty(replaced, value.property(), value.value());
                    }
                    chunk.setBlockState(pos, replaced);
                }
            });

        }
    }

    /** 把源状态上同名属性的值复制到目标状态；目标缺少该属性时原状态保留。 */
    private static <T extends Comparable<T>> BlockState copyProperty(BlockState target, Property<T> property, Comparable<?> value) {
        return target.trySetValue(property, property.getValueClass().cast(value));
    }
}
