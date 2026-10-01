package org.polaris2023.wildwind.hfas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class FlammableWoodBlocks {
    private FlammableWoodBlocks() {
    }

    private interface FireValues {
        int fireSpreadSpeed();
        int flammability();
    }

    private static int spread(FireValues values) {
        return values.fireSpreadSpeed();
    }

    private static int burn(FireValues values) {
        return values.flammability();
    }

    public static class Pillar extends RotatedPillarBlock implements FireValues {
        private final int fireSpreadSpeed;
        private final int flammability;

        public Pillar(Properties properties) {
            this(properties, 5, 5);
        }

        public Pillar(Properties properties, int fireSpreadSpeed, int flammability) {
            super(properties);
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.flammability = flammability;
        }

        @Override
        public int fireSpreadSpeed() {
            return fireSpreadSpeed;
        }

        @Override
        public int flammability() {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return spread(this);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return burn(this);
        }
    }

    public static class Basic extends Block implements FireValues {
        private final int fireSpreadSpeed;
        private final int flammability;

        public Basic(Properties properties) {
            this(properties, 5, 20);
        }

        public Basic(Properties properties, int fireSpreadSpeed, int flammability) {
            super(properties);
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.flammability = flammability;
        }

        @Override
        public int fireSpreadSpeed() {
            return fireSpreadSpeed;
        }

        @Override
        public int flammability() {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return spread(this);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return burn(this);
        }
    }

    public static class Stairs extends StairBlock implements FireValues {
        private final int fireSpreadSpeed;
        private final int flammability;

        public Stairs(BlockState baseState, Properties properties) {
            this(baseState, properties, 5, 20);
        }

        public Stairs(BlockState baseState, Properties properties, int fireSpreadSpeed, int flammability) {
            super(baseState, properties);
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.flammability = flammability;
        }

        @Override
        public int fireSpreadSpeed() {
            return fireSpreadSpeed;
        }

        @Override
        public int flammability() {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return spread(this);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return burn(this);
        }
    }

    public static class Slab extends SlabBlock implements FireValues {
        private final int fireSpreadSpeed;
        private final int flammability;

        public Slab(Properties properties) {
            this(properties, 5, 20);
        }

        public Slab(Properties properties, int fireSpreadSpeed, int flammability) {
            super(properties);
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.flammability = flammability;
        }

        @Override
        public int fireSpreadSpeed() {
            return fireSpreadSpeed;
        }

        @Override
        public int flammability() {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return spread(this);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return burn(this);
        }
    }

    public static class Fence extends FenceBlock implements FireValues {
        private final int fireSpreadSpeed;
        private final int flammability;

        public Fence(Properties properties) {
            this(properties, 5, 20);
        }

        public Fence(Properties properties, int fireSpreadSpeed, int flammability) {
            super(properties);
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.flammability = flammability;
        }

        @Override
        public int fireSpreadSpeed() {
            return fireSpreadSpeed;
        }

        @Override
        public int flammability() {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return spread(this);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return burn(this);
        }
    }

    public static class Gate extends FenceGateBlock implements FireValues {
        private final int fireSpreadSpeed;
        private final int flammability;

        public Gate(WoodType woodType, Properties properties) {
            this(woodType, properties, 5, 20);
        }

        public Gate(WoodType woodType, Properties properties, int fireSpreadSpeed, int flammability) {
            super(woodType, properties);
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.flammability = flammability;
        }

        @Override
        public int fireSpreadSpeed() {
            return fireSpreadSpeed;
        }

        @Override
        public int flammability() {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return spread(this);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return burn(this);
        }
    }

    public static class Leaves extends TintedParticleLeavesBlock implements FireValues {
        private final int fireSpreadSpeed;
        private final int flammability;

        public Leaves(float leafParticleChance, Properties properties) {
            this(leafParticleChance, properties, 30, 60);
        }

        public Leaves(float leafParticleChance, Properties properties, int fireSpreadSpeed, int flammability) {
            super(leafParticleChance, properties);
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.flammability = flammability;
        }

        @Override
        public int fireSpreadSpeed() {
            return fireSpreadSpeed;
        }

        @Override
        public int flammability() {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return spread(this);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
            return burn(this);
        }
    }
}
