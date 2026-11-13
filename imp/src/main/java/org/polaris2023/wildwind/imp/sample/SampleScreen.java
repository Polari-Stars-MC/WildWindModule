package org.polaris2023.wildwind.imp.sample;

import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.ParamSince;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * <b>模板示例</b>：单一类里并排书写多个版本的实现，构建时按目标版本裁剪（ADR-0003）。
 *
 * <p>主导模式是<b>删除式</b>：不适用的实现整块被预处理器去掉，而不是保留下来
 * 再在运行时 {@code if} 判断。因此源码里可以毫无顾忌地使用只属于某版本的 API
 * （包括已改名的类）——只要它落在会被裁掉的那一段里。</p>
 *
 * <p>本类同时充当构建期自检：三种形态每次构建都会走一遍，任何一处失效立刻暴露。
 * 真实迁移内容落地后可删除本包。</p>
 */
public final class SampleScreen {

    private SampleScreen() {}

    /** 26.x 形态。 */
    @Since("26.1")
    @Name("draw")
    public static void drawModern() {
        // 只有 26.x 才有的写法
        net.minecraft.resources.Identifier.parse("ww:sample");
    }

    /** 1.21 形态。 */
    @Since("1.21")
    @Until("26.1")
    @Name("draw")
    public static void drawTen21() {
        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("ww", "sample");
    }

    /** 1.20 形态。 */
    @Until("1.21")
    @Name("draw")
    public static void drawTen20() {
        new net.minecraft.resources.ResourceLocation("ww", "sample");
    }

    // ── 参数差异：预处理器不删参数，仍用重载 + ImpManifest 常量分派 ──────────
    // （成员级差异已由删除处理，只有"同一成员在某版本多/少一个参数"才需要这条路径）

    /** 版本无关入口。 */
    public static void render(String graphics) {
        if (org.polaris2023.wildwind.imp.runtime.ImpManifest.HAS_SAMPLESCREEN_RENDER__STRING__INT) {
            render(graphics, 0);
        } else {
            renderLegacy(graphics);
        }
    }

    /** 26.2 起才接受该参数。 */
    public static void render(String graphics, @ParamSince("26.2") int modernFlag) {
        // 新版本实现
    }

    /** 旧版本回退实现（ParamSinceValidator 强制要求存在）。 */
    private static void renderLegacy(String graphics) {
    }
}
