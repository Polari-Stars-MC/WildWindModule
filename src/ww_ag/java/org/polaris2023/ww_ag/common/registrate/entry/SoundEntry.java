package org.polaris2023.ww_ag.common.registrate.entry;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 13:21:23}
 */
public class SoundEntry<T extends SoundEvent> extends RegistryEntry<SoundEvent, T> {
    public SoundEntry(AbstractRegistrate<?> owner, DeferredHolder<SoundEvent, T> key) {
        super(owner, key);
    }
}
