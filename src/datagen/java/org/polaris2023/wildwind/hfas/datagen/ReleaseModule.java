package org.polaris2023.wildwind.hfas.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

/** Explicit resource ownership. Each provider writes directly into one module's pack. */
public enum ReleaseModule {
    CORE("core"), WILDERNESS("wilderness"), BARREN("barren");

    private final String directory;
    ReleaseModule(String directory) {
        this.directory = directory;
    }

    public String id() {
        return "wild_wind_" + directory;
    }

    public boolean owns(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(id());
    }

    public PackOutput output(PackOutput parent) {
        return new PackOutput(parent.getOutputFolder().resolve(directory));
    }
}
