package org.polaris2023.wildwind.hfas.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.polaris2023.wildwind.hfas.block.entity.KilnBlockEntity;
import org.polaris2023.wildwind.hfas.block.entity.ModBlockEntities;

public class KilnBlock extends AbstractFurnaceBlock {
    public static final MapCodec<KilnBlock> CODEC = simpleCodec(KilnBlock::new);

    public KilnBlock(Properties properties) { super(properties); }

    @Override
    public MapCodec<KilnBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new KilnBlockEntity(pos, state); }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level instanceof ServerLevel server ? createTickerHelper(type, ModBlockEntities.KILN.get(),
                (ignored, pos, blockState, kiln) -> KilnBlockEntity.tick(server, pos, blockState, kiln)) : null;
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof KilnBlockEntity kiln) player.openMenu(kiln);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        double x = pos.getX() + 0.5, y = pos.getY(), z = pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1, 1, false);
        }
        Direction facing = state.getValue(FACING);
        double offset = random.nextDouble() * 0.6 - 0.3;
        double dx = facing.getAxis() == Direction.Axis.X ? facing.getStepX() * 0.52 : offset;
        double dz = facing.getAxis() == Direction.Axis.Z ? facing.getStepZ() * 0.52 : offset;
        double dy = random.nextDouble() * 6 / 16;
        level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0, 0, 0);
    }
}
