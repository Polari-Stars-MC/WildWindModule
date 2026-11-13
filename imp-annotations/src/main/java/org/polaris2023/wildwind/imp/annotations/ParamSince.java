package org.polaris2023.wildwind.imp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记参数自指定 Minecraft 版本起才存在（ADR-0003 §3.2）。
 *
 * <p>APT 不修改既有签名，参数差异的表达方式为<b>重载 + 适配器分派</b>：
 * 开发者在源码里同时提供带参数与不带参数的重载（旧版本路径保留默认值），
 * 处理器生成的适配器按 {@code ImpManifest} 常量选择调用目标。旧签名会残留在
 * 产物中，这是方案 B 已接受的代价（见 ADR-0003"后果"节）。</p>
 *
 * <pre>{@code
 * public class WWScreen {
 *     public void render(ImpGuiGraphics g, @ParamSince("26.2") int legacyFlag) { ... }
 *     public void render(ImpGuiGraphics g) { render(g, 0); }  // 旧版本回退路径
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ParamSince {

    /** 起始 Minecraft 版本。 */
    String value();
}
