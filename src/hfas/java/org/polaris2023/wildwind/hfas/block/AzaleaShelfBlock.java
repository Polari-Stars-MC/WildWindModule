package org.polaris2023.wildwind.hfas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class AzaleaShelfBlock extends ShelfBlock {
    public AzaleaShelfBlock(Properties properties) { super(properties); }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : 20;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : 30;
    }
}
