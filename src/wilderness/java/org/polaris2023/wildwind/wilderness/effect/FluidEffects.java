package org.polaris2023.wildwind.wilderness.effect;

import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.polaris2023.wildwind.wilderness.WildernessMod;
import org.polaris2023.wildwind.wilderness.entity.ModEntityTypeTags;

@EventBusSubscriber(modid = WildernessMod.MOD_ID)
public final class FluidEffects {
    private FluidEffects() {}

    public static boolean cannotRise(Entity entity) {
        return entity instanceof LivingEntity living && living.is(ModEntityTypeTags.SINK_AFFECTED)
                && living.hasEffect(ModEffects.SINK)
                && entity.getFluidInteraction().isInFluidMatching(entity, (e, type, height) -> !type.isAir());
    }

    public static Vec3 restrictMovement(Entity entity, Vec3 movement) {
        if (movement.y > 0 && cannotRise(entity)) return new Vec3(movement.x, 0, movement.z);
        return movement;
    }

    @SubscribeEvent
    public static void beforeTick(EntityTickEvent.Pre event) {
        var entity = event.getEntity();
        entity.setDeltaMovement(restrictMovement(entity, entity.getDeltaMovement()));
    }

    @SubscribeEvent
    public static void afterTick(EntityTickEvent.Post event) {
        var entity = event.getEntity();
        entity.setDeltaMovement(restrictMovement(entity, entity.getDeltaMovement()));
    }

    @SubscribeEvent
    public static void breathe(LivingBreatheEvent event) {
        var entity = event.getEntity();
        if (!entity.isEyeInFluid(FluidTags.WATER)) return;
        if (entity.is(ModEntityTypeTags.WATER_BREATHING)) {
            event.setCanBreathe(true);
            event.setConsumeAirAmount(0);
            // Refill even an already empty air meter (e.g. after /reload) before the drowning check.
            event.setRefillAirAmount(Math.max(event.getRefillAirAmount(),
                    entity.getMaxAirSupply() - entity.getAirSupply()));
            return;
        }
        var effect = entity.getEffect(ModEffects.CHOKE);
        if (effect != null && entity.is(ModEntityTypeTags.CHOKE_AFFECTED)
                && !MobEffectUtil.hasWaterBreathing(entity)
                && !(entity instanceof Player player && player.getAbilities().invulnerable)
                && !entity.level().getBlockState(BlockPos.containing(entity.getEyePosition())).is(Blocks.BUBBLE_COLUMN)) {
            // The whitelist also includes innate water-breathers (e.g. frogs and turtles).
            event.setCanBreathe(false);
            // I consumes twice as much air, II three times, etc. Preserve respiration's zero-consumption ticks.
            long amount = (long) event.getConsumeAirAmount() * (effect.getAmplifier() + 2L);
            event.setConsumeAirAmount((int) Math.min(Integer.MAX_VALUE, amount));
        }
    }

    /** Used before vanilla land drying, which otherwise overwrites changes made by the breathing event. */
    public static int airBeforeDrying(LivingEntity entity, int air) {
        if ((entity.getType() != EntityTypes.TADPOLE && entity.getType() != EntityTypes.AXOLOTL)
                || entity.isInWater()) return air;
        var effect = entity.getEffect(ModEffects.CHOKE);
        if (effect == null) return air;
        // Vanilla subtracts the final one and performs its usual drying damage/reset afterwards.
        return (int) Math.max(Integer.MIN_VALUE + 1L, (long) air - effect.getAmplifier() - 1L);
    }
}
