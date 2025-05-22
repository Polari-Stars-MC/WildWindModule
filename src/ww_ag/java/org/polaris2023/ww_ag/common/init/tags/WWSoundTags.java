package org.polaris2023.ww_ag.common.init.tags;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 12:48:09}
 */
public enum WWSoundTags implements Supplier<TagKey<SoundEvent>> {
    GLARE$DEATH,
    GLARE$AMBIENT,
    ;
    private final TagKey<SoundEvent> tag;
    WWSoundTags() {
        tag = TagKey.create(Registries.SOUND_EVENT, WWAgMod.cLoc(name().toLowerCase(Locale.ROOT).replace("$", "/")));
    }
    WWSoundTags(Object mod) {
        tag = TagKey.create(Registries.SOUND_EVENT, REGISTRATE.loc(name().toLowerCase(Locale.ROOT).replace("$", "/")));
    }



    @Override
    public TagKey<SoundEvent> get() {
        return tag;
    }

    public void tagFor(Consumer<HolderSet.Named<SoundEvent>> consumer) {
        BuiltInRegistries.SOUND_EVENT.getTag(tag).ifPresent(consumer);
    }
}
