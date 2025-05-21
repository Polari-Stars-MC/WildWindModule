package org.polaris2023.ww_ag.datagen;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 16:43:10}
 */
public class WWSoundDefineProvider extends SoundDefinitionsProvider implements RegistrateProvider {
    public final AbstractRegistrate<?> parent;
    public WWSoundDefineProvider(AbstractRegistrate<?> parent, PackOutput output, ExistingFileHelper helper) {
        super(output, parent.getModid(), helper);
        this.parent = parent;
    }



    public void add(SoundEvent soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public void registerSounds() {
        parent.genData(WWProviderType.SOUND_DEFINE, this);
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }
}
