package org.polaris2023.wildwind.imp;

import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 世界高度查询的版本无关口（ADR-0003）。
 *
 * <p>差异：26.x 把 {@code getMinBuildHeight()} 改名成了更短的 {@code getMinY()}
 * （同时 {@code getMaxBuildHeight()} → {@code getMaxY()}），语义完全一致。</p>
 */
public final class ImpLevels {

    private ImpLevels() {}

    /** 26.x：{@code LevelHeightAccessor.getMinY()}。 */
    @Since("26.1")
    @Name("minY")
    public static int minYModern(net.minecraft.world.level.LevelHeightAccessor level) {
        return level.getMinY();
    }

    /** 1.20 / 1.21：{@code getMinBuildHeight()}。 */
    @Until("26.1")
    @Name("minY")
    public static int minYLegacy(net.minecraft.world.level.LevelHeightAccessor level) {
        return level.getMinBuildHeight();
    }

    /** 26.x：{@code getMaxY()}（与原版 {@code getMinY() + getHeight()} 一致）。 */
    @Since("26.1")
    @Name("maxY")
    public static int maxYModern(net.minecraft.world.level.LevelHeightAccessor level) {
        return level.getMaxY();
    }

    /** 1.20 / 1.21：{@code getMaxBuildHeight()}。 */
    @Until("26.1")
    @Name("maxY")
    public static int maxYLegacy(net.minecraft.world.level.LevelHeightAccessor level) {
        return level.getMaxBuildHeight();
    }

    /**
     * 直接在区块数据上写方块，不触发层级联更新。
     *
     * <p>差异：26.x 是 {@code setBlockState(BlockPos, BlockState)}（内部按 {@code flags = 3} 转发）
     * 或 {@code setBlockState(BlockPos, BlockState, @Block.UpdateFlags int)}；
     * 1.20 / 1.21 的第三参是 {@code boolean moved}（只在 {@code onRemove/onPlace} 里当「方块是被推动的」用），
     * 没有 {@code UpdateFlags} 概念。</p>
     *
     * <p>语义对齐：26.x 的 {@code flags = 3} 同时要求「更新邻居」与「通知客户端」，而 1.21 的这个重载
     * 本身不处理邻居/客户端更新（那是 {@code Level.setBlock} 的职责）。因此旧版传 {@code moved = false}
     * ——与 26.x 下「方块不是被活塞推动的」这一含义一致。</p>
     */
    @Since("26.1")
    @Name("setBlockInChunk")
    public static void setBlockInChunkModern(net.minecraft.world.level.chunk.ChunkAccess chunk,
                                             net.minecraft.core.BlockPos pos,
                                             net.minecraft.world.level.block.state.BlockState state) {
        chunk.setBlockState(pos, state);
    }

    /** 1.20 / 1.21：第三参是 {@code moved}，详见 {@link #setBlockInChunkModern} 的语义对齐说明。 */
    @Until("26.1")
    @Name("setBlockInChunk")
    public static void setBlockInChunkLegacy(net.minecraft.world.level.chunk.ChunkAccess chunk,
                                             net.minecraft.core.BlockPos pos,
                                             net.minecraft.world.level.block.state.BlockState state) {
        chunk.setBlockState(pos, state, false);
    }
}
