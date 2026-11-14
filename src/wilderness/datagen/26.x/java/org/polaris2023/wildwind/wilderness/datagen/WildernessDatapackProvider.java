package org.polaris2023.wildwind.wilderness.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.data.ImpDatapackWriter;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;
import org.polaris2023.wildwind.wilderness.data.WildernessDatapackSpecs;
import org.polaris2023.wildwind.wilderness.entity.animal.MudcrabVariant;
import org.polaris2023.wildwind.wilderness.worldgen.WildernessBiomeModifiers;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 荒野更新的数据包内建注册：群系修改器与泥沼蟹变种。
 *
 * <p>薄适配（ADR-0005）："生成什么"在 {@link WildernessDatapackSpecs}，
 * "怎么发射"在 {@link ImpDatapackWriter}，本类只把内容模块的
 * {@code bootstrap} 交给发射器。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author safe_saffawa
 */
public class WildernessDatapackProvider extends DatapackBuiltinEntriesProvider {

    public WildernessDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, buildBuilder(), Set.of(WildWindWilderness.MOD_ID, "minecraft"));
    }

    /**
     * 把规格 + 内容模块的引导器交给发射器，拿回该版本的 {@code RegistrySetBuilder}。
     *
     * <p>泥沼蟹变种的引导器是 {@code bootstrapDatagen}——它同样是
     * {@code Consumer<BootstrapContext<MudcrabVariant>>}，所以变种内容只能留在内容模块。</p>
     */
    private static RegistrySetBuilder buildBuilder() {
        return ImpDatapackWriter.apply(WildernessDatapackSpecs.datapacks(), new ImpDatapackWriter.Sinks() {
            @Override
            public Object bootstrap(ImpId registry) {
                if (WildernessDatapackSpecs.BIOME_MODIFIERS.equals(registry)) {
                    RegistrySetBuilder.RegistryBootstrap<BiomeModifier> bootstrap =
                        WildernessBiomeModifiers::bootstrap;
                    return bootstrap;
                }
                if (WildernessDatapackSpecs.MUDCRAB_VARIANT.equals(registry)) {
                    RegistrySetBuilder.RegistryBootstrap<MudcrabVariant> bootstrap =
                        MudcrabVariant::bootstrapDatagen;
                    return bootstrap;
                }
                throw new IllegalArgumentException("未知的动态注册表: " + registry);
            }
        });
    }
}
