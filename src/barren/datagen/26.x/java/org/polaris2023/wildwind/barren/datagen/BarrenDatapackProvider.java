package org.polaris2023.wildwind.barren.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.data.BarrenDatapackSpecs;
import org.polaris2023.wildwind.barren.worldgen.BarrenBiomeModifiers;
import org.polaris2023.wildwind.barren.worldgen.BarrenConfiguredFeatures;
import org.polaris2023.wildwind.barren.worldgen.BarrenPlacedFeatures;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.data.ImpDatapackWriter;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 瘠土更新的数据包内建注册：树木特征与群系修改器。
 *
 * <p>薄适配（ADR-0005）："生成什么"在 {@link BarrenDatapackSpecs}，
 * "怎么发射"在 {@link ImpDatapackWriter}，本类只把内容模块的
 * {@code bootstrap} 交给发射器。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author safe_saffawa
 */
public class BarrenDatapackProvider extends DatapackBuiltinEntriesProvider {

    public BarrenDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, buildBuilder(), Set.of(WildWindBarren.MOD_ID, "minecraft"));
    }

    /**
     * 把规格 + 内容模块的引导器交给发射器，拿回该版本的 {@code RegistrySetBuilder}。
     *
     * <p>引导器的类型是 {@code RegistrySetBuilder.RegistryBootstrap<T>}——它本身就是
     * {@code Consumer<BootstrapContext<T>>}，所以特征内容只能留在内容模块。</p>
     */
    private static RegistrySetBuilder buildBuilder() {
        return ImpDatapackWriter.apply(BarrenDatapackSpecs.datapacks(), new ImpDatapackWriter.Sinks() {
            @Override
            public Object bootstrap(ImpId registry) {
                if (BarrenDatapackSpecs.CONFIGURED_FEATURE.equals(registry)) {
                    RegistrySetBuilder.RegistryBootstrap<ConfiguredFeature<?, ?>> bootstrap =
                        BarrenConfiguredFeatures::bootstrap;
                    return bootstrap;
                }
                if (BarrenDatapackSpecs.PLACED_FEATURE.equals(registry)) {
                    RegistrySetBuilder.RegistryBootstrap<PlacedFeature> bootstrap =
                        BarrenPlacedFeatures::bootstrap;
                    return bootstrap;
                }
                if (BarrenDatapackSpecs.BIOME_MODIFIERS.equals(registry)) {
                    RegistrySetBuilder.RegistryBootstrap<BiomeModifier> bootstrap =
                        BarrenBiomeModifiers::bootstrap;
                    return bootstrap;
                }
                throw new IllegalArgumentException("未知的动态注册表: " + registry);
            }
        });
    }
}
