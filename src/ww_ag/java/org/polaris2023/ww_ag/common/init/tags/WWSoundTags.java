package org.polaris2023.ww_ag.common.init.tags;

import lombok.experimental.ExtensionMethod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 12:48:09}
 */
@ExtensionMethod({TagKey.class, WWAgMod.class})
public enum WWSoundTags implements Supplier<TagKey<SoundEvent>> {
    GLARE$DEATH,
    GLARE$AMBIENT,
    ;
    private final TagKey<SoundEvent> tag;
    WWSoundTags() {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = Registries.SOUND_EVENT.create(id.cLoc());
    }
    WWSoundTags(Object mod) {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = Registries.SOUND_EVENT.create(id.loc());
    }



    @Override
    public TagKey<SoundEvent> get() {
        return tag;
    }

    public void tagFor(Consumer<HolderSet.Named<SoundEvent>> consumer) {
        BuiltInRegistries.SOUND_EVENT.getTag(tag).ifPresent(consumer);
    }
}
