package org.polaris2023.wildwind.hfas.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.config.ModCommonConfig;

/**
 * 世界生成事件处理器
 * 用于替换结构内的原版制箭台为模组制箭台
 *
 * @author baka4n
 * @since 2026/04/15
 */
@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class FletchingTableReplaceHandler {

    /**
     * 方块放置事件 - 处理所有制箭台放置
     * 包括结构生成、玩家放置等
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!ModCommonConfig.shouldReplaceVanillaFletchingTable()) {
            return;
        }

        BlockState placedState = event.getPlacedBlock();

        if (placedState.getBlock() == Blocks.FLETCHING_TABLE) {
            LevelAccessor levelAccessor = event.getLevel();

            if (levelAccessor instanceof Level level && !level.isClientSide()) {
                BlockPos pos = event.getPos();
                // 取消原事件并设置模组制箭台
                event.setCanceled(true);
                level.setBlock(pos, ModBlocks.FLETCHING_TABLE.get().defaultBlockState(), 3);
            }
        }
    }

    /**
     * 区块加载事件 - 替换区块内的原版制箭台
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
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
                    BlockState returnState = ModBlocks.FLETCHING_TABLE.get().defaultBlockState();
                    state.getValues().forEach(v -> {
                        Property property = v.property();
                        Comparable value = v.value();
                        returnState.setValue(property, value);
                    });
                    chunk.setBlockState(pos, returnState);
                }
            });

        }
    }
}
