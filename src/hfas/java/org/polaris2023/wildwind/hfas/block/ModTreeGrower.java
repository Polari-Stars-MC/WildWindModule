package org.polaris2023.wildwind.hfas.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.polaris2023.wildwind.hfas.worldgen.ModConfiguredFeatures;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public enum ModTreeGrower implements Supplier<TreeGrower> {
    CINDER(0.0F, null, null, ModConfiguredFeatures.CINDER.get(), null, null, null),
    EMBER(0.0F, null, null, ModConfiguredFeatures.EMBER.get(), null, null, null),
    ;

    private final TreeGrower grower;

    ModTreeGrower(float secondaryChance, ResourceKey<ConfiguredFeature<?, ?>> megaTree, ResourceKey<ConfiguredFeature<?, ?>> secondaryMegaTree, ResourceKey<ConfiguredFeature<?, ?>> tree, ResourceKey<ConfiguredFeature<?, ?>> secondaryTree, ResourceKey<ConfiguredFeature<?, ?>> flowers, ResourceKey<ConfiguredFeature<?, ?>> secondaryFlowers) {
        grower = new TreeGrower(name().toLowerCase(Locale.ROOT), secondaryChance, Optional.ofNullable(megaTree), Optional.ofNullable(secondaryMegaTree), Optional.ofNullable(tree), Optional.ofNullable(secondaryTree), Optional.ofNullable(flowers), Optional.ofNullable(secondaryFlowers));
    }

    @Override
    public TreeGrower get() {
        return grower;
    }
}
