package org.polaris2023.wildwind.wilderness.worldgen;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import java.util.Optional;
import org.polaris2023.wildwind.wilderness.WildernessMod;
public final class ModTreeGrowers {
    public static final TreeGrower BLAZE = new TreeGrower("wild_wind_wilderness:blaze", Optional.empty(), Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, WildernessMod.id("blaze_tree"))), Optional.empty());
    public static final TreeGrower SOUL = new TreeGrower("wild_wind_wilderness:soul", Optional.empty(), Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, WildernessMod.id("soul_tree"))), Optional.empty());
}
