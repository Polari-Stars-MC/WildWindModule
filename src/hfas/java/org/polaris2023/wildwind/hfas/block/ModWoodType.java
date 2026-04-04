package org.polaris2023.wildwind.hfas.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Locale;
import java.util.function.Supplier;

public enum ModWoodType implements Supplier<WoodType> {
    CINDER,
    EMBER,
    ;

    private final WoodType type;

    ModWoodType() {

        this.type = WoodType.register(new WoodType(name().toLowerCase(Locale.ROOT), ModBlockSetType.valueOf(name()).get()));
    }

    ModWoodType(BlockSetType type) {
        this.type = WoodType.register(new WoodType(type.name(), type));
    }

    ModWoodType(ModBlockSetType type) {
        this.type = WoodType.register(new WoodType(type.name().toLowerCase(Locale.ROOT), type.get()));
    }

    @Override
    public WoodType get() {
        return type;
    }
}
