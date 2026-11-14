package org.polaris2023.wildwind.barren.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.barren.WildWindBarren;

/**
 * 瘠土更新的地物类型注册。
 *
 * @author mdjfnfn
 */
public final class BarrenFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, WildWindBarren.MOD_ID);

    public static final DeferredHolder<Feature<?>, RandomSizeOreFeature> RANDOM_SIZE_ORE =
            FEATURES.register("random_size_ore", RandomSizeOreFeature::new);

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }

    private BarrenFeatures() {
    }
}
