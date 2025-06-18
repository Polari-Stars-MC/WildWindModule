package org.polaris2023.ww_ag.common.block;

import com.google.common.base.Supplier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author baka4n
 * @code @Date 2025/6/18 02:05:34
 */
public class StrippableBlock extends RotatedPillarBlock {
    private final Supplier<RotatedPillarBlock> lazy;

    public StrippableBlock(Properties properties, Supplier<RotatedPillarBlock> lazy) {
        super(properties);
        this.lazy = lazy;

    }

    @ParametersAreNonnullByDefault
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        return itemAbility == ItemAbilities.AXE_STRIP ? this.lazy.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
    }
}
