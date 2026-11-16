package org.polaris2023.wildwind.barren.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.worldgen.SoulSandstoneFeature;

/**
 * 瘠土更新的世界生成特征类型注册。
 *
 * @author safe_saffawa
 */
public final class BarrenFeatures {

    private static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, WildWindBarren.MOD_ID);

    public static final DeferredHolder<Feature<?>, SoulSandstoneFeature> SOUL_SANDSTONE =
            FEATURES.register("soul_sandstone", () -> new SoulSandstoneFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }

    private BarrenFeatures() {
    }
}
