package org.polaris2023.wildwind.hfas.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, HFASMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PIRANHA_ATTACK = SOUND_EVENTS.register(
            "entity.piranha.attack",
            SoundEvent::createVariableRangeEvent
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> PIRANHA_DEATH = SOUND_EVENTS.register(
            "entity.piranha.death",
            SoundEvent::createVariableRangeEvent
    );

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }

    private ModSoundEvents() {
    }
}