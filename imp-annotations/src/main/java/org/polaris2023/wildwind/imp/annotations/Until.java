package org.polaris2023.wildwind.imp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记成员在指定 Minecraft 版本起被移除（ADR-0003）。
 *
 * <p>可配合 {@link #since()} 表达区间存在：<code>@Until("26.3")</code> 表示
 * 26.3 起移除；<code>@Until(value="26.3", since="26.1")</code> 表示只在
 * [26.1, 26.3) 区间存在。</p>
 *
 * <p>targetMcVersion 不在存在区间内时，处理器视为该成员"不存在"，生成
 * {@code ImpManifest} 中的 {@code false} 常量，适配分派走替代路径。</p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface Until {

    /** 移除起始 Minecraft 版本（含），如 {@code "26.2"}。 */
    String value();

    /**
     * 起始 Minecraft 版本（该版本起才存在）。
     * 默认空串表示从一开始就存在。
     */
    String since() default "";

    /**
     * 存在区间内需要排除的版本列表，如 {@code {"26.2"}}——表示该成员
     * 在 26.2 上不存在。默认空数组表示不排除任何版本。
     */
    String[] exclude() default {};
}
