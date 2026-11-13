package org.polaris2023.wildwind.imp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记成员自指定 Minecraft 版本起才存在（ADR-0003）。
 *
 * <p>可配合 {@link #until()} 表达区间存在：<code>@Since("26.1")</code> 表示
 * 26.1 起存在；<code>@Since(value="26.1", until="26.3")</code> 表示只在
 * [26.1, 26.3) 区间存在，26.3 起移除。</p>
 *
 * <p>imp-processor 在编译期读取本注解，为不存在该成员的版本生成
 * {@code ImpManifest} 中的 {@code false} 常量与适配分派；本注解保留策略为
 * SOURCE，不会进入任何产物。</p>
 *
 * <p>版本比较语义见 {@link ImpVersionSpec}。</p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface Since {

    /** 起始 Minecraft 版本，如 {@code "26.2"}。该版本及以后才可能存在。 */
    String value();

    /**
     * 结束 Minecraft 版本（含，即该版本起移除），如 {@code "26.3"}。
     * 默认空串表示没有结束版本。
     */
    String until() default "";

    /**
     * 存在区间内需要排除的版本列表，如 {@code {"26.2"}}——表示该成员
     * 在 26.2 上不存在（版本中途移除过、下个版本又恢复的情形）。
     * 默认空数组表示不排除任何版本。
     */
    String[] exclude() default {};
}
