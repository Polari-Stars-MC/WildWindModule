package org.polaris2023.ww_ag.common.init;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinition.SoundType;
import org.polaris2023.ww_ag.common.init.tags.WWSoundTags;
import org.polaris2023.ww_ag.common.registrate.entry.SoundEntry;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 12:57:42}
 */
public class ModSounds {

    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_1 = REGISTRATE
            .variableRangeSound("glare_ambient_1")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_2 = REGISTRATE
            .variableRangeSound("glare_ambient_2")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_3 = REGISTRATE
            .variableRangeSound("glare_ambient_3")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_4 = REGISTRATE
            .variableRangeSound("glare_ambient_4")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_5 = REGISTRATE
            .variableRangeSound("glare_ambient_5")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_6 = REGISTRATE
            .variableRangeSound("glare_ambient_6")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_7 = REGISTRATE
            .variableRangeSound("glare_ambient_7")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_8 = REGISTRATE
            .variableRangeSound("glare_ambient_8")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_9 = REGISTRATE
            .variableRangeSound("glare_ambient_9")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_10 = REGISTRATE
            .variableRangeSound("glare_ambient_10")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_11 = REGISTRATE
            .variableRangeSound("glare_ambient_11")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_12 = REGISTRATE
            .variableRangeSound("glare_ambient_12")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_AMBIENT_13 = REGISTRATE
            .variableRangeSound("glare_ambient_13")
            .tag(WWSoundTags.GLARE$AMBIENT.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_DEATH_1 = REGISTRATE
            .variableRangeSound("glare_death_1")
            .tag(WWSoundTags.GLARE$DEATH.get())
            .register();
    public static final SoundEntry<SoundEvent> GLARE_DEATH_2 = REGISTRATE
            .variableRangeSound("glare_death_2")
            .tag(WWSoundTags.GLARE$DEATH.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();
    public static final SoundEntry<SoundEvent> GLARE_DEATH_3 = REGISTRATE
            .variableRangeSound("glare_death_3")
            .tag(WWSoundTags.GLARE$DEATH.get())
            .defineSingle(SoundType.SOUND, 1, 1, 2, 8, true, true)
            .register();




    public static void register() {}
}
