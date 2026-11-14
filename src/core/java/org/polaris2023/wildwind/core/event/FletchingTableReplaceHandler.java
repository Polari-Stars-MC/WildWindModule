package org.polaris2023.wildwind.core.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.config.ModCommonConfig;
import org.polaris2023.wildwind.imp.ImpLevels;

/**
 * 制箭台替换处理器。
 * <p>
 * 玩家放置由 {@code BlockItemMixin} 直接换掉放置的方块，这里只负责
 * 把已生成世界（区块加载时）里的原版制箭台换成模组制箭台。
 *
 * <p>三处版本差异已经收敛：</p>
 * <ul>
 *   <li>区块事件的 {@code getChunk()} 在 1.20 / 1.21 返回 {@code ChunkAccess}、
 *       26.x 返回 {@code LevelChunk}——这里统一按 {@code ChunkAccess} 用，只在写回时用
 *       {@link ImpLevels#setBlockInChunk}；</li>
 *   <li>最低建筑高度：26.x {@code getMinY()} / 旧版 {@code getMinBuildHeight()}，走 {@link ImpLevels#minY}；</li>
 *   <li>遍历状态属性：<b>两族都</b>有 {@code getProperties()} + {@code getValue(Property)}，
 *       所以直接用这对方法就跨版本通用，不必碰 26.x 才改形的 {@code getValues()} 流。</li>
 * </ul>
 *
 * @author baka4n
 * @author safe_saffawa
 * @since 2026/04/15
 */
@EventBusSubscriber(modid = WildWindCore.MOD_ID)
public class FletchingTableReplaceHandler {

    /** 扫描上界：原版制箭台只可能出现在建筑高度内的地表附近，取一个安全的上限 */
    private static final int SCAN_MAX_Y = 200;

    /**
     * 区块加载事件 - 替换区块内的原版制箭台
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!ModCommonConfig.shouldReplaceVanillaFletchingTable()) {
            return;
        }

        var chunk = event.getChunk();
        LevelAccessor level = event.getLevel();

        if (level.isClientSide()) {
            return;
        }

        int minY = ImpLevels.minY(level);
        // 上限不能超过该维度实际的建筑高度上界
        int maxY = Math.min(SCAN_MAX_Y, ImpLevels.maxY(level) - 1);

        BlockPos.betweenClosedStream(
                chunk.getPos().getMinBlockX(), minY, chunk.getPos().getMinBlockZ(),
                chunk.getPos().getMaxBlockX(), maxY, chunk.getPos().getMaxBlockZ()
        ).forEach(pos -> {
            BlockState state = chunk.getBlockState(pos);
            if (!state.is(Blocks.FLETCHING_TABLE)) {
                return;
            }
            BlockState replaced = ModBlocks.FLETCHING_TABLE.get().defaultBlockState();
            for (Property<?> property : state.getProperties()) {
                replaced = copyProperty(replaced, property, state.getValue(property));
            }
            ImpLevels.setBlockInChunk(chunk, pos.immutable(), replaced);
        });
    }

    /** 把源状态上同名属性的值复制到目标状态；目标缺少该属性时原状态保留。 */
    private static <T extends Comparable<T>> BlockState copyProperty(BlockState target, Property<T> property, Comparable<?> value) {
        return target.trySetValue(property, property.getValueClass().cast(value));
    }
}
