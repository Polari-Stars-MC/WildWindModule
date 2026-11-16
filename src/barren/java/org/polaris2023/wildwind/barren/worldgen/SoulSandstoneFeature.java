package org.polaris2023.wildwind.barren.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;

/**
 * 灵魂砂岩层。
 * <p>
 * 灵魂沙峡谷的地表（含天花板）由原版 {@code minecraft:nether} 的 surface rule 铺出灵魂沙 / 灵魂土，
 * 没有可挂载的特征，所以在顶层修改阶段整块扫一遍：按列找出每一段灵魂沙（或灵魂土）的底面，
 * 紧贴底面往下把 {@value #LAYER_THICKNESS} 格下界岩换成灵魂砂岩，效果对应原版沙漠的「沙上、砂岩下」。
 *
 * @author safe_saffawa
 */
public class SoulSandstoneFeature extends Feature<NoneFeatureConfiguration> {
    private static final int LAYER_THICKNESS = 2;

    public SoulSandstoneFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        ChunkPos chunkPos = ChunkPos.containing(context.origin());
        ChunkAccess chunk = level.getChunk(chunkPos.x(), chunkPos.z());
        BlockState soulSandstone = BarrenBlocks.SOUL_SANDSTONE.get().defaultBlockState();
        LevelChunkSection[] sections = chunk.getSections();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean placed = false;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int layerBottom = Integer.MIN_VALUE;
                for (int sectionIndex = sections.length - 1; sectionIndex >= 0; sectionIndex--) {
                    LevelChunkSection section = sections[sectionIndex];
                    if (section == null || section.hasOnlyAir()) {
                        // 空区段不可能跨着灵魂沙层，遇到就结算上一段
                        if (layerBottom != Integer.MIN_VALUE) {
                            placed |= fillUnderLayer(level, pos, chunkPos, localX, localZ, layerBottom, soulSandstone);
                            layerBottom = Integer.MIN_VALUE;
                        }
                        continue;
                    }
                    int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
                    for (int localY = 15; localY >= 0; localY--) {
                        if (isSoulLayer(section.getBlockState(localX, localY, localZ))) {
                            layerBottom = (sectionY << 4) + localY;
                        } else if (layerBottom != Integer.MIN_VALUE) {
                            placed |= fillUnderLayer(level, pos, chunkPos, localX, localZ, layerBottom, soulSandstone);
                            layerBottom = Integer.MIN_VALUE;
                        }
                    }
                }
            }
        }
        return placed;
    }

    private static boolean fillUnderLayer(WorldGenLevel level, BlockPos.MutableBlockPos pos, ChunkPos chunkPos,
                                          int localX, int localZ, int layerBottom, BlockState soulSandstone) {
        int x = chunkPos.getMinBlockX() + localX;
        int z = chunkPos.getMinBlockZ() + localZ;
        boolean placed = false;
        for (int offset = 1; offset <= LAYER_THICKNESS; offset++) {
            int y = layerBottom - offset;
            if (y < level.getMinY()) {
                break;
            }
            pos.set(x, y, z);
            // 只在紧贴的下界岩上铺，遇到洞穴 / 岩浆 / 砂砾就停手
            if (!level.getBlockState(pos).is(Blocks.NETHERRACK)) {
                break;
            }
            level.setBlock(pos, soulSandstone, 2);
            placed = true;
        }
        return placed;
    }

    private static boolean isSoulLayer(BlockState state) {
        return state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL);
    }
}
