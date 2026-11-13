package org.polaris2023.wildwind.imp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重新打包时把符号自动改名为目标名字（ADR-0003 §3.3）。
 *
 * <p>用途：源码里可以用任意顺手的名字书写，构建时由源码预处理器按目标版本
 * 替换成该版本真实的名字——既覆盖本仓库自己的类/方法/字段，也覆盖
 * Minecraft 自身符号的跨版本改名。</p>
 *
 * <pre>{@code
 * @Name("parse")
 * public static Object resourceId(String ns, String path) { ... }
 * }</pre>
 *
 * <p>重命名规则：</p>
 * <ul>
 *   <li>打在声明上：声明名与同模块内的调用点一起改写；</li>
 *   <li>同名映射若产生歧义（同一旧名映射到多个新名），处理器直接报错要求改名，
 *       不做猜测；</li>
 *   <li>重命名发生在源码级，产物里看到的是新名字，注解本身不进入产物。</li>
 * </ul>
 *
 * @see Since 版本存在性
 * @see Until 版本移除
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Name {

    /** 目标名字（该声明在产物中的实际名字）。 */
    String value();
}
