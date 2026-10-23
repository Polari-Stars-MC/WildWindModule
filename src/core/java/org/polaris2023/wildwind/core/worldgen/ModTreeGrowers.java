package org.polaris2023.wildwind.core.worldgen;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import java.util.Optional;
import org.polaris2023.wildwind.core.CoreMod;
public final class ModTreeGrowers {
    public static final TreeGrower AZALEA = new TreeGrower("wild_wind_core:azalea", Optional.empty(), Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, CoreMod.id("azalea_tree"))), Optional.empty());
}
