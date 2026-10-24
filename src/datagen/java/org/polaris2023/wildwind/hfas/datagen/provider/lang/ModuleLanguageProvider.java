package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;

public abstract class ModuleLanguageProvider extends LanguageProvider {
    protected final ReleaseModule module;
    protected ModuleLanguageProvider(PackOutput output, ReleaseModule module, String locale) {
        super(output, module.id(), locale);
        this.module = module;
    }

    @Override
    public void add(String key, Component value) {
        boolean sharedCore = key.equals("mod.wild_wind.name") || key.equals("mod.wild_wind.core") || key.startsWith("sulfur_cube_archetype.minecraft.");
        boolean wilderness = key.startsWith("item.minecraft.") || key.startsWith("subtitles.entity.") || key.equals("mod.wild_wind.wilderness");
        if (key.contains("." + module.id() + ".")
                || module == ReleaseModule.CORE && sharedCore
                || module == ReleaseModule.WILDERNESS && wilderness
                || module == ReleaseModule.BARREN && key.equals("mod.wild_wind.barren")) super.add(key, value);
    }
}
