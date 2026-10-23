package org.polaris2023.wildwind.core.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public final class SkeletonBehavior {
    private SkeletonBehavior() {}

    public static boolean canSpawnWithSword(Mob mob) {
        var type = mob.getType();
        return type == EntityTypes.SKELETON || type == EntityTypes.STRAY
                || type == EntityTypes.BOGGED || type == EntityTypes.PARCHED;
    }

    public static void afterMeleeHit(Mob attacker, LivingEntity target) {
        var type = attacker.getType();
        if (type == EntityTypes.STRAY) {
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600, 0), attacker);
        } else if (type == EntityTypes.BOGGED) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0), attacker);
        } else if (type == EntityTypes.PARCHED) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 0), attacker);
        }
    }
}
