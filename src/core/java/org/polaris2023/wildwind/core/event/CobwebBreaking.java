package org.polaris2023.wildwind.core.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.polaris2023.wildwind.core.CoreMod;

@EventBusSubscriber(modid = CoreMod.MOD_ID)
public final class CobwebBreaking {
    private static final double EPSILON = 1.0E-7;
    private static final Map<Entity, ContactState> STATES = new WeakHashMap<>();
    private CobwebBreaking() {}

    private static boolean exempt(Entity entity) {
        return entity instanceof Spider || entity.isSpectator()
                || entity instanceof Player player && player.isCreative() && player.getAbilities().flying;
    }

    @SubscribeEvent
    public static void beforeTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel) || exempt(entity)) return;
        // Keep a previous position across ticks for players, whose network movement occurs outside tick().
        var state = STATES.get(entity);
        if (state == null && !touching(entity).isEmpty()) STATES.put(entity, new ContactState(entity.position()));
    }

    @SubscribeEvent
    public static void afterTick(EntityTickEvent.Post event) { tick(event.getEntity()); }

    public static void tick(Entity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;
        if (exempt(entity) || entity.isRemoved()) { STATES.remove(entity); return; }
        Set<BlockPos> contacts = touching(entity);
        if (contacts.isEmpty()) { STATES.remove(entity); return; }
        var state = STATES.get(entity);
        if (state == null) {
            STATES.put(entity, new ContactState(entity.position()));
            return;
        }
        Vec3 delta = entity.position().subtract(state.previous);
        state.previous = entity.position();
        state.progress.keySet().retainAll(contacts);
        boolean moving = delta.lengthSqr() > EPSILON * EPSILON;
        if (!moving) { state.progress.clear(); return; }
        boolean falling = !entity.onGround() && delta.y < -EPSILON;
        for (var pos : contacts) {
            var progress = state.progress.computeIfAbsent(pos, ignored -> new Progress(falling));
            if (++progress.ticks >= (progress.falling ? 160 : 30)) {
                if (level.getBlockState(pos).is(Blocks.COBWEB)) level.destroyBlock(pos, true, entity);
                state.progress.remove(pos);
            }
        }
    }

    private static Set<BlockPos> touching(Entity entity) {
        var result = new HashSet<BlockPos>();
        for (var pos : BlockPos.betweenClosed(entity.getBoundingBox().deflate(1.0E-7))) {
            if (entity.level().getBlockState(pos).is(Blocks.COBWEB)) result.add(pos.immutable());
        }
        return result;
    }

    private static final class ContactState {
        Vec3 previous;
        final Map<BlockPos, Progress> progress = new HashMap<>();
        ContactState(Vec3 previous) { this.previous = previous; }
    }
    private static final class Progress {
        final boolean falling;
        int ticks;
        Progress(boolean falling) { this.falling = falling; }
    }
}
