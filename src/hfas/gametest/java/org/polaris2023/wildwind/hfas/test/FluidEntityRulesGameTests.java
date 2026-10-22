package org.polaris2023.wildwind.hfas.test;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.polaris2023.wildwind.hfas.effect.FluidEffects;
import org.polaris2023.wildwind.hfas.effect.ModEffects;
import org.polaris2023.wildwind.hfas.entity.ModEntityTypeTags;

final class FluidEntityRulesGameTests {
    private FluidEntityRulesGameTests() {}

    private static void check(GameTestHelper h, boolean condition, String message) {
        h.assertTrue(condition, Component.literal(message));
    }

    private static LivingEntity submerged(GameTestHelper h, EntityType<?> type) {
        var entity = type == EntityTypes.PLAYER ? h.makeMockServerPlayer(GameType.SURVIVAL)
                : (LivingEntity) type.create(h.getLevel(), EntitySpawnReason.COMMAND);
        check(h, entity != null, "Must create test entity " + type);
        if (entity instanceof ServerPlayer player) {
            var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
            new io.netty.channel.embedded.EmbeddedChannel(connection);
            h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player,
                    net.minecraft.server.network.CommonListenerCookie.createInitial(player.getGameProfile(), false));
        }
        if (entity instanceof Mob mob) mob.setNoAi(true);
        entity.setNoGravity(true);
        entity.setPos(h.absoluteVec(new Vec3(3.5, 1.5, 3.5)));
        // Initialize actual fluid contact and eye immersion without adding mobs to the world.
        entity.baseTick();
        check(h, entity.isEyeInFluid(FluidTags.WATER), "Test entity must be submerged: " + type);
        return entity;
    }

    static void run(GameTestHelper h) {
        for (int x = 1; x <= 6; x++) for (int y = 1; y <= 4; y++) for (int z = 1; z <= 6; z++) {
            h.setBlock(x, y, z, Blocks.WATER);
        }
        var registry = h.getLevel().registryAccess().lookupOrThrow(Registries.ENTITY_TYPE);
        var protectedTypes = registry.getOrThrow(ModEntityTypeTags.WATER_BREATHING);
        check(h, protectedTypes.size() >= 25, "All 25 distinct requested vanilla water-breathing types must load");
        for (var holder : protectedTypes) {
            var entity = submerged(h, holder.value());
            check(h, entity.is(EntityTypeTags.CAN_BREATHE_UNDER_WATER), "Vanilla breathing tag must include " + holder);
            entity.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200, 3));
            entity.setAirSupply(-20);
            float health = entity.getHealth();
            CommonHooks.onLivingBreathe(entity, h.getLevel(), 1, 4);
            check(h, entity.getAirSupply() == entity.getMaxAirSupply() && entity.getHealth() == health,
                    "Protected type must recover empty air and resist Choke without drowning: " + holder);
            entity.addEffect(new MobEffectInstance(ModEffects.SINK, 200));
            check(h, !FluidEffects.cannotRise(entity), "Unlisted water-breather must not be affected by Sink: " + holder);
        }

        var affectedTypes = registry.getOrThrow(ModEntityTypeTags.SINK_AFFECTED);
        assertWhitelist(h, ModEntityTypeTags.SINK_AFFECTED, """
                player fox camel camel_husk chicken mooshroom cow donkey horse mule parrot pig rabbit sheep
                sniffer tadpole turtle wandering_trader goat villager axolotl cat frog ocelot snow_golem bee
                panda iron_golem llama polar_bear trader_llama wolf evoker hoglin piglin_brute pillager
                ravager vindicator warden witch piglin
                """);
        assertWhitelist(h, ModEntityTypeTags.CHOKE_AFFECTED, """
                player fox camel camel_husk chicken mooshroom cow donkey horse mule parrot pig rabbit sheep
                sniffer turtle wandering_trader goat villager cat frog ocelot snow_golem bee panda iron_golem
                llama polar_bear trader_llama wolf evoker hoglin piglin_brute pillager ravager vindicator
                warden witch creeper cave_spider spider enderman piglin
                """);
        for (var holder : affectedTypes) {
            var entity = submerged(h, holder.value());
            entity.addEffect(new MobEffectInstance(ModEffects.SINK, 200));
            double y = entity.getY();
            entity.move(MoverType.SELF, new Vec3(0, 0.1, 0));
            check(h, entity.getY() == y, "Sink must prevent actual upward motion for " + holder);
            entity.removeEffect(ModEffects.SINK);
            entity.move(MoverType.SELF, new Vec3(0, 0.1, 0));
            check(h, entity.getY() > y, "Removing Sink must restore upward motion for " + holder);
        }

        for (var type : List.of(EntityTypes.BAT, EntityTypes.CREEPER, EntityTypes.SPIDER,
                EntityTypes.PHANTOM, EntityTypes.COD)) {
            var entity = submerged(h, type);
            entity.addEffect(new MobEffectInstance(ModEffects.SINK, 200));
            double y = entity.getY();
            entity.move(MoverType.SELF, new Vec3(0, 0.1, 0));
            check(h, entity.getY() > y, "Unlisted type/subclass must retain upward movement: " + type);
        }

        for (var holder : registry.getOrThrow(ModEntityTypeTags.CHOKE_AFFECTED)) {
            var type = holder.value();
            var entity = submerged(h, type);
            entity.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200));
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() == 198, "Choke I must double actual underwater air loss: " + type);
            entity.setAirSupply(200);
            CommonHooks.onLivingBreathe(entity, h.getLevel(), 0, 4);
            check(h, entity.getAirSupply() == 200, "Choke must preserve Respiration's skipped consumption: " + type);
            entity.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200, 1));
            entity.baseTick();
            check(h, entity.getAirSupply() == 197, "Choke II must triple underwater air loss: " + type);
            entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200));
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() >= 200, "Water Breathing must protect against Choke: " + type);
            entity.removeEffect(MobEffects.WATER_BREATHING);
            entity.setPos(h.absoluteVec(new Vec3(3.5, 6, 3.5)));
            entity.baseTick();
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() == 204, "Ordinary Choke targets must recover air on land: " + type);
        }
        for (var type : List.of(EntityTypes.BAT, EntityTypes.ALLAY)) {
            var entity = submerged(h, type);
            entity.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200, 2));
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() == 199, "Unlisted types must retain normal air consumption: " + type);
        }
        var phantom = submerged(h, EntityTypes.PHANTOM);
        phantom.addEffect(new MobEffectInstance(ModEffects.SINK, 200));
        phantom.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200));
        phantom.setAirSupply(0);
        CommonHooks.onLivingBreathe(phantom, h.getLevel(), 1, 4);
        check(h, !FluidEffects.cannotRise(phantom) && phantom.getAirSupply() > 0,
                "Phantom must now resist both effects");
        var player = (ServerPlayer) submerged(h, EntityTypes.PLAYER);
        // The mock fixes gameMode() to its constructor value; set the abilities used by the breathing hook.
        GameType.CREATIVE.updatePlayerAbilities(player.getAbilities());
        player.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200));
        player.setAirSupply(200);
        player.baseTick();
        check(h, player.getAirSupply() >= 200, "Creative invulnerability must protect against Choke: air="
                + player.getAirSupply() + ", invulnerable=" + player.getAbilities().invulnerable);
        h.succeed();
    }

    private static void assertWhitelist(GameTestHelper h, TagKey<EntityType<?>> tag, String vanillaNames) {
        Set<String> expected = new HashSet<>();
        for (String name : vanillaNames.strip().split("\\s+")) expected.add("minecraft:" + name);
        expected.add("ww_hfas:muddy_pig");
        Set<String> actual = new HashSet<>();
        for (var holder : h.getLevel().registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getOrThrow(tag)) {
            actual.add(BuiltInRegistries.ENTITY_TYPE.getKey(holder.value()).toString());
        }
        check(h, actual.equals(expected), "Exact whitelist must match " + tag + ": " + actual);
    }

    static void landChoke(GameTestHelper h) {
        for (int x = 1; x <= 6; x++) for (int y = 1; y <= 4; y++) for (int z = 1; z <= 6; z++) {
            h.setBlock(x, y, z, Blocks.WATER);
        }
        // Keep the dry position sheltered so rain cannot rehydrate the axolotl.
        h.setBlock(3, 8, 3, Blocks.STONE);
        for (var type : List.of(EntityTypes.TADPOLE, EntityTypes.AXOLOTL)) {
            var entity = submerged(h, type);
            ((Mob) entity).setNoAi(false);
            check(h, !entity.is(ModEntityTypeTags.CHOKE_AFFECTED), "Land-only types must not enter the water Choke tag");
            entity.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200));
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() == entity.getMaxAirSupply(), "Choke must not affect submerged " + type);
            entity.setPos(h.absoluteVec(new Vec3(3.5, 6, 3.5)));
            entity.baseTick();
            check(h, !entity.isInWater(), "Special Choke test must be on dry land");
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() == 198, "Choke I must double real land drying: " + type);
            entity.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200, 1));
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() == 197, "Choke II must triple real land drying: " + type);
            entity.removeEffect(ModEffects.CHOKE);
            entity.setAirSupply(200);
            entity.baseTick();
            check(h, entity.getAirSupply() == 199, "Removing Choke must restore vanilla drying: " + type);
            entity.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200));
            entity.setAirSupply(-18);
            entity.invulnerableTime = 0;
            float health = entity.getHealth();
            entity.baseTick();
            check(h, entity.getHealth() == health - 2 && entity.getAirSupply() == 0,
                    "Land Choke must use vanilla drying damage exactly once: " + type
                            + ", health " + health + " -> " + entity.getHealth() + ", air=" + entity.getAirSupply());
            entity.setPos(h.absoluteVec(new Vec3(3.5, 1.5, 3.5)));
            entity.baseTick();
            check(h, entity.getAirSupply() == entity.getMaxAirSupply(), "Returning to water must stop Choke and refill air: " + type);
        }
        var cod = submerged(h, EntityTypes.COD);
        cod.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200, 2));
        cod.setPos(h.absoluteVec(new Vec3(3.5, 6, 3.5)));
        cod.baseTick();
        cod.setAirSupply(200);
        cod.baseTick();
        check(h, cod.getAirSupply() == 199, "Shared WaterAnimal mixin must not accelerate other fish drying");
        h.succeed();
    }
}
