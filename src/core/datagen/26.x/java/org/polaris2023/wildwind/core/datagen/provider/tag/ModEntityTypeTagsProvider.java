package org.polaris2023.wildwind.core.datagen.provider.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.data.ModTagSpecs;
import org.polaris2023.wildwind.imp.data.ImpTagWriter;

import java.util.concurrent.CompletableFuture;

/**
 * 核心模块的实体类型标签生成器。
 *
 * <p>内容逻辑在 {@link ModTagSpecs#entityTypeTags()}（ADR-0005），本类只做薄适配。</p>
 *
 * @author safe_saffawa
 */
public class ModEntityTypeTagsProvider extends net.minecraft.data.tags.EntityTypeTagsProvider {

    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, WildWindCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ImpTagWriter.apply(ModTagSpecs.entityTypeTags(), new ImpTagWriter.Appenders() {
            @Override
            public Object blockTag(TagKey<Block> key) {
                throw new UnsupportedOperationException("实体类型标签生成器不处理方块标签");
            }

            @Override
            public Object itemTag(TagKey<Item> key) {
                throw new UnsupportedOperationException("实体类型标签生成器不处理物品标签");
            }

            @Override
            public Object entityTypeTag(TagKey<EntityType<?>> key) {
                return tag(key);
            }
        });
    }
}
