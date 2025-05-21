package org.polaris2023.ww_ag.common.registrate.build;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.common.registrate.entry.SoundEntry;

import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 13:06:54}
 */
public class SoundBuilder<T extends SoundEvent, P> extends AbstractBuilder<SoundEvent, T, P, SoundBuilder<T, P>> {
    private final Supplier<T> sound;
    private SoundBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Supplier<T> supplier) {
        super(owner, parent, name, callback, Registries.SOUND_EVENT);
        sound = supplier;
    }

    public static <T extends SoundEvent, P> SoundBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Supplier<T> supplier) {
        return new SoundBuilder<>(owner, parent, name, callback, supplier);
    }


    public SoundBuilder<T, P> lang(String name) {
        return super.lang(b -> b.getLocation().toLanguageKey("sound"), name);
    }
    public SoundBuilder<T, P> defaultLang() {
        return super.lang(b -> b.getLocation().toLanguageKey("sound"));
    }

    public SoundBuilder<T, P> define(NonNullBiConsumer<DataGenContext<SoundEvent, T>, SoundDefinition> definition) {
        return setData(WWProviderType.SOUND_DEFINE, (c, p) -> {
            SoundDefinition soundDefinition = SoundDefinition.definition();
            definition.accept(c, soundDefinition);
            p.add(c.get(), soundDefinition);
        });
    }

    public SoundBuilder<T, P> defineSingle(SoundDefinition.SoundType type,
                                           float volume,
                                           float pitch,
                                           int weight,
                                           int distance,
                                           boolean stream,
                                           boolean preload) {
        return setData(WWProviderType.SOUND_DEFINE, (c, p) -> {
            p.add(c.get(), SoundDefinition.definition()
                    .with(SoundDefinition.Sound
                            .sound(c.getId(), type)
                            .volume(volume)
                            .pitch(pitch)
                            .weight(weight)
                            .attenuationDistance(distance)
                            .stream(stream)
                            .preload(preload)
                    ));
        });
    }


    @Override
    protected @NonnullType T createEntry() {
        return sound.get();
    }

    @Override
    protected RegistryEntry<SoundEvent, T> createEntryWrapper(DeferredHolder<SoundEvent, T> delegate) {
        return new SoundEntry<>(getOwner(), delegate);
    }

    @SafeVarargs
    public final SoundBuilder<T, P> tag(TagKey<SoundEvent>... tags) {
        return setData(WWProviderType.SOUNDS, (c, p) -> {
            for (TagKey<SoundEvent> tag : tags) {
                //noinspection OptionalGetWithoutIsPresent
                p.addTag(tag).add(BuiltInRegistries.SOUND_EVENT.getResourceKey(c.get()).get());
            }
        });
    }

    @Override
    public SoundEntry<T> register() {
        return (SoundEntry<T>) super.register();
    }
}
