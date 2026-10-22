package org.polaris2023.wildwind.hfas.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;

public final class ModEffects {
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HFASMod.MOD_ID);
    public static final DeferredHolder<MobEffect, MobEffect> SINK = EFFECTS.register("sink", () -> new HarmfulEffect(0x344D78));
    public static final DeferredHolder<MobEffect, MobEffect> CHOKE = EFFECTS.register("choke", () -> new HarmfulEffect(0x737C81));
    private ModEffects() {}
    public static void register(IEventBus bus) { EFFECTS.register(bus); }
    private static final class HarmfulEffect extends MobEffect {
        HarmfulEffect(int color) { super(MobEffectCategory.HARMFUL, color); }
    }
}
