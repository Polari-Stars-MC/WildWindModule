package org.polaris2023.wildwind.imp.data;

import org.polaris2023.wildwind.imp.ImpId;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本无关的「标签规格」（ADR-0005）。
 *
 * <p>datagen 的知识分两层：<b>生成什么</b>（哪些标签、哪些条目）与
 * <b>怎么发射</b>（该版本的 Provider API）。前者是纯数据，可跨版本共用；
 * 后者因基类不同必须按版本分叉——一个类只能继承一个父类，注解改不了。</p>
 *
 * <p>本类承载前者：只用 {@link ImpId} 描述条目，不出现任何 MC 类型，
 * 因此三个版本族的 datagen 可以消费同一份规格。</p>
 *
 * @param kind   条目所属注册表
 * @param tag    标签 id，如 {@code minecraft:logs}
 * @param values 标签内的条目 id（按插入顺序，输出确定）
 */
public record ImpTagSpec(Kind kind, ImpId tag, List<ImpId> values) {

    /** 条目所属注册表。 */
    public enum Kind {
        BLOCK,
        ITEM,
        ENTITY_TYPE,
        FLUID
    }

    public ImpTagSpec {
        values = List.copyOf(values);
    }

    public static ImpTagSpec of(Kind kind, String tagPath, List<ImpId> values) {
        return new ImpTagSpec(kind, ImpId.of("minecraft", tagPath), values);
    }

    public static ImpTagSpec of(Kind kind, ImpId tag, List<ImpId> values) {
        return new ImpTagSpec(kind, tag, values);
    }

    /** 规格收集器：内容侧只关心"往哪个标签里放什么"。 */
    public static final class Collector {

        private final List<ImpTagSpec> specs = new ArrayList<>();

        public Collector add(Kind kind, String tagPath, ImpId... values) {
            specs.add(of(kind, tagPath, List.of(values)));
            return this;
        }

        public Collector add(Kind kind, String namespace, String tagPath, ImpId... values) {
            specs.add(new ImpTagSpec(kind, ImpId.of(namespace, tagPath), List.of(values)));
            return this;
        }

        public List<ImpTagSpec> build() {
            return List.copyOf(specs);
        }
    }
}
