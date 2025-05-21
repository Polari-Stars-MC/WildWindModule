package org.polaris2023.ww_ag.common.registrate;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.polaris2023.ww_ag.common.registrate.build.SoundBuilder;

import java.util.function.Function;

/**
* @author baka4n
* {@code @Date 2025/05/19 22:44:01}
*/
@SuppressWarnings("unused")
public class WWRegistrate extends L2Registrate {
    public WWRegistrate(String modid) {
        super(modid);
    }

    public SoundBuilder<SoundEvent, L2Registrate> fixRangeSound(String name, float range) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> SoundEvent.createFixedRangeEvent(loc(name), range)));
    }
    public SoundBuilder<SoundEvent, L2Registrate> variableRangeSound(String name) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> SoundEvent.createVariableRangeEvent(loc(name))));
    }

    public <T extends SoundEvent> SoundBuilder<T, L2Registrate> customSound(String name, Function<ResourceLocation, T> function) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> function.apply(loc(name))));
    }

}
