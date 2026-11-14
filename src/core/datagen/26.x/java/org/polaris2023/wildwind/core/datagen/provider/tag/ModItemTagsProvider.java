package org.polaris2023.wildwind.core.datagen.provider.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.data.ModTagSpecs;
import org.polaris2023.wildwind.imp.data.ImpTagWriter;

import java.util.concurrent.CompletableFuture;

/**
 * 核心模块的物品标签生成器。
 *
 * <p>内容逻辑在 {@link ModTagSpecs#itemTags()}（ADR-0005），本类只做薄适配。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author CarrotLegend
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WildWindCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ImpTagWriter.apply(ModTagSpecs.itemTags(), new ImpTagWriter.Appenders() {
            @Override
            public Object blockTag(TagKey<Block> key) {
                throw new UnsupportedOperationException("物品标签生成器不处理方块标签");
            }

            @Override
            public Object itemTag(TagKey<Item> key) {
                return tag(key);
            }

            @Override
            public Object entityTypeTag(TagKey<EntityType<?>> key) {
                throw new UnsupportedOperationException("物品标签生成器不处理实体类型标签");
            }
        });
    }
}
