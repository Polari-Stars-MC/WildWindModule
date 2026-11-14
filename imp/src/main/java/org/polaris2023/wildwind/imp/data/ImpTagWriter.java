package org.polaris2023.wildwind.imp.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;

/**
 * 版本无关的「标签发射器」（ADR-0005）。
 *
 * <p>分工：</p>
 * <ul>
 *   <li>{@link ImpTagSpec} = <b>生成什么</b>（纯数据，三族共用）</li>
 *   <li>本类 = <b>怎么发射</b>（按版本分段，预处理器裁掉不适用分支，{@code @Name} 把留存段改回同名）</li>
 *   <li>{@link Appenders} = 内容模块提供的极薄适配（交出该版本的标签追加器，通常三行）</li>
 * </ul>
 *
 * <p>各版本唯一必须重写的只有 {@link Appenders} 的三行，而不是整套 Provider 逻辑。</p>
 */
public final class ImpTagWriter {

    private ImpTagWriter() {}

    /** 内容模块实现的薄适配：把该版本的标签追加器以 {@code Object} 交出来。 */
    public interface Appenders {
        Object blockTag(TagKey<Block> key);

        Object itemTag(TagKey<Item> key);

        Object entityTypeTag(TagKey<EntityType<?>> key);
    }

    /** 按规格逐条发射；版本分支在内部完成，调用方无需关心。 */
    public static void apply(List<ImpTagSpec> specs, Appenders appenders) {
        for (ImpTagSpec spec : specs) {
            switch (spec.kind()) {
                case BLOCK -> writeBlocks(spec, appenders);
                case ITEM -> writeItems(spec, appenders);
                case ENTITY_TYPE -> writeEntityTypes(spec, appenders);
                default -> throw new IllegalArgumentException("未支持的标签类型：" + spec.kind());
            }
        }
    }

    // ── 26.x：TagAppender（按 ResourceKey 追加）───────────────────────────

    @Since("26.1")
    @SuppressWarnings("unchecked")
    private static void writeBlocksModern(ImpTagSpec spec, Appenders appenders) {
        TagKey<Block> key = TagKey.create(Registries.BLOCK, spec.tag().unwrap());
        var appender = (net.minecraft.data.tags.TagAppender<Block>) appenders.blockTag(key);
        appender.addAll(spec.values().stream().map(value -> BuiltInRegistries.BLOCK
            .getResourceKey(BuiltInRegistries.BLOCK.getValue(value.unwrap())).orElseThrow()));
    }

    @Since("26.1")
    @SuppressWarnings("unchecked")
    private static void writeItemsModern(ImpTagSpec spec, Appenders appenders) {
        TagKey<Item> key = TagKey.create(Registries.ITEM, spec.tag().unwrap());
        var appender = (net.minecraft.data.tags.TagAppender<Item>) appenders.itemTag(key);
        appender.addAll(spec.values().stream().map(value -> BuiltInRegistries.ITEM
            .getResourceKey(BuiltInRegistries.ITEM.getValue(value.unwrap())).orElseThrow()));
    }

    @Since("26.1")
    @SuppressWarnings("unchecked")
    private static void writeEntityTypesModern(ImpTagSpec spec, Appenders appenders) {
        TagKey<EntityType<?>> key = TagKey.create(Registries.ENTITY_TYPE, spec.tag().unwrap());
        var appender = (net.minecraft.data.tags.TagAppender<EntityType<?>>) appenders.entityTypeTag(key);
        appender.addAll(spec.values().stream().map(value -> BuiltInRegistries.ENTITY_TYPE
            .getResourceKey(BuiltInRegistries.ENTITY_TYPE.getValue(value.unwrap())).orElseThrow()));
    }

    // ── 1.20 / 1.21：IntrinsicTagAppender（直接追加值）────────────────────

    @Until("26.1")
    @Name("writeBlocks")
    @SuppressWarnings("unchecked")
    private static void writeBlocksLegacy(ImpTagSpec spec, Appenders appenders) {
        TagKey<Block> key = TagKey.create(Registries.BLOCK, spec.tag().unwrap());
        var appender = (net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>)
            appenders.blockTag(key);
        for (var value : spec.values()) {
            appender.add(BuiltInRegistries.BLOCK.get(value.unwrap()));
        }
    }

    @Until("26.1")
    @Name("writeItems")
    @SuppressWarnings("unchecked")
    private static void writeItemsLegacy(ImpTagSpec spec, Appenders appenders) {
        TagKey<Item> key = TagKey.create(Registries.ITEM, spec.tag().unwrap());
        var appender = (net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>)
            appenders.itemTag(key);
        for (var value : spec.values()) {
            appender.add(BuiltInRegistries.ITEM.get(value.unwrap()));
        }
    }

    @Until("26.1")
    @Name("writeEntityTypes")
    @SuppressWarnings("unchecked")
    private static void writeEntityTypesLegacy(ImpTagSpec spec, Appenders appenders) {
        TagKey<EntityType<?>> key = TagKey.create(Registries.ENTITY_TYPE, spec.tag().unwrap());
        var appender = (net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender<EntityType<?>>)
            appenders.entityTypeTag(key);
        for (var value : spec.values()) {
            appender.add(BuiltInRegistries.ENTITY_TYPE.get(value.unwrap()));
        }
    }

    // ── 现代分支的方法名由 @Name 统一 ────────────────────────────────────

    @Since("26.1")
    @Name("writeBlocks")
    private static void writeBlocksEntry(ImpTagSpec spec, Appenders appenders) {
        writeBlocksModern(spec, appenders);
    }

    @Since("26.1")
    @Name("writeItems")
    private static void writeItemsEntry(ImpTagSpec spec, Appenders appenders) {
        writeItemsModern(spec, appenders);
    }

    @Since("26.1")
    @Name("writeEntityTypes")
    private static void writeEntityTypesEntry(ImpTagSpec spec, Appenders appenders) {
        writeEntityTypesModern(spec, appenders);
    }
}
