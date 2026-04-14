package org.polaris2023.wildwind.hfas.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 制箭台方块实体
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class FletchingTableBlockEntity extends BlockEntity {

    public FletchingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLETCHING_TABLE.get(), pos, state);
    }
}
