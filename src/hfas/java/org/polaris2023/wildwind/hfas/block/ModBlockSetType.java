package org.polaris2023.wildwind.hfas.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.Locale;
import java.util.function.Supplier;

public enum ModBlockSetType implements Supplier<BlockSetType> {
    CINDER,
    EMBER
    ;

    private final BlockSetType type;

    ModBlockSetType() {
        type = BlockSetType.register(new BlockSetType(name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public BlockSetType get() {
        return type;
    }
}
