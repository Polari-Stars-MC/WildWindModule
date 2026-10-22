package org.polaris2023.wildwind.hfas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.polaris2023.wildwind.hfas.block.entity.FletchingTableBlockEntity;
import org.polaris2023.wildwind.hfas.menu.FletchingTableMenu;

import javax.annotation.Nullable;

/**
 * 制箭台方块 - 带有GUI功能
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class FletchingTableBlock extends Block implements EntityBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("container.fletching_table");

    public FletchingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        player.openMenu(state.getMenuProvider(level, pos));
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new FletchingTableMenu(containerId, playerInventory),
                CONTAINER_TITLE
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FletchingTableBlockEntity(pos, state);
    }
}
