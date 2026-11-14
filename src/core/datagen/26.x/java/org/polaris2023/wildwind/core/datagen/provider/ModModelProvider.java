package org.polaris2023.wildwind.core.datagen.provider;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.data.ModModelSpecs;
import org.polaris2023.wildwind.imp.data.ImpModelWriter;

/**
 * 核心模块的模型生成器。
 *
 * <p>内容逻辑在 {@link ModModelSpecs}（ADR-0005，纯数据规格，三族共用）；
 * 本类只把该版本的生成器对象交出去。</p>
 *
 * @author safe_saffawa
 */
public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, WildWindCore.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ImpModelWriter.apply(ModModelSpecs.models(), new ImpModelWriter.Generators() {
            @Override
            public Object blockModels() {
                return blockModels;
            }

            @Override
            public Object itemModels() {
                return itemModels;
            }
        });
    }
}
