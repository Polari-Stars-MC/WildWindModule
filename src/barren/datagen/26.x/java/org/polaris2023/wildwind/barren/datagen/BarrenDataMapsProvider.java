package org.polaris2023.wildwind.barren.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.polaris2023.wildwind.barren.data.BarrenDataMapSpecs;
import org.polaris2023.wildwind.imp.data.ImpDataMapWriter;

import java.util.concurrent.CompletableFuture;

/**
 * 瘠土更新的数据映射：去皮与燃料。
 *
 * <p>薄适配（ADR-0005）："生成什么"在 {@link BarrenDataMapSpecs}，
 * "怎么发射"在 {@link ImpDataMapWriter}，本类只交出两张数据映射的构建器——
 * 这就是全部内容，条目、值、{@code replace} 标志都不再出现在这里。</p>
 *
 * @author baka4n
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class BarrenDataMapsProvider extends DataMapProvider {

    public BarrenDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        ImpDataMapWriter.apply(BarrenDataMapSpecs.dataMaps(), new ImpDataMapWriter.Sinks() {
            @Override
            public Object furnaceFuels() {
                return builder(NeoForgeDataMaps.FURNACE_FUELS);
            }

            @Override
            public Object strippables() {
                return builder(NeoForgeDataMaps.STRIPPABLES);
            }
        });
    }
}
