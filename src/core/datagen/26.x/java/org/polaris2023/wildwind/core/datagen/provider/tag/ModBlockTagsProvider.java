package org.polaris2023.wildwind.core.datagen.provider.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.data.ModTagSpecs;
import org.polaris2023.wildwind.imp.data.ImpTagWriter;

import java.util.concurrent.CompletableFuture;

/**
 * 核心模块的方块标签生成器。
 *
 * <p>内容逻辑已按 ADR-0005 迁到 {@link ModTagSpecs}（纯数据规格，三族共用）；
 * 本类只剩"把该版本的标签追加器交出去"这一层极薄适配。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WildWindCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ImpTagWriter.apply(ModTagSpecs.blockTags(), new ImpTagWriter.Appenders() {
            @Override
            public Object blockTag(TagKey<Block> key) {
                return tag(key);
            }

            @Override
            public Object itemTag(TagKey<Item> key) {
                throw new UnsupportedOperationException("方块标签生成器不处理物品标签");
            }

            @Override
            public Object entityTypeTag(TagKey<EntityType<?>> key) {
                throw new UnsupportedOperationException("方块标签生成器不处理实体类型标签");
            }
        });
    }
}
