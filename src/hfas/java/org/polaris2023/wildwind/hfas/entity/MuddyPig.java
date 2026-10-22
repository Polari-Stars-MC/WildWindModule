package org.polaris2023.wildwind.hfas.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.registry.ModEntities;

/** Vanilla pig AI, food and adult/baby sounds with a distinct entity and offspring type. */
public class MuddyPig extends Pig {
    public static final TagKey<Block> SPAWNABLE_ON = TagKey.create(Registries.BLOCK,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "muddy_pig_spawnable_on"));

    public MuddyPig(EntityType<? extends MuddyPig> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable MuddyPig getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.MUDDY_PIG.get().create(level, EntitySpawnReason.BREEDING);
    }

    public static boolean checkSpawnRules(EntityType<MuddyPig> type, LevelAccessor level,
            EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(SPAWNABLE_ON)
                && (EntitySpawnReason.ignoresLightRequirements(reason) || isBrightEnoughToSpawn(level, pos));
    }
}
