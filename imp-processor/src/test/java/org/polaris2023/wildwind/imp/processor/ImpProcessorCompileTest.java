package org.polaris2023.wildwind.imp.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 注解语义的回归测试：用真实 javac 跑处理器，断言 {@code ImpManifest} 的生成结果。
 *
 * <p>覆盖：成员存在（{@code @Since}）、成员移除（{@code @Until}）、区间（{@code until}）、
 * 区间内缺失（{@code exclude}）、参数差异（{@code @ParamSince}）、以及三类必须失败的场景。</p>
 */
final class ImpProcessorCompileTest {

    /** 覆盖全部四种正常语义的测试源。 */
    private static final String SOURCE = """
        package smoke;

        import org.polaris2023.wildwind.imp.annotations.ParamSince;
        import org.polaris2023.wildwind.imp.annotations.Since;
        import org.polaris2023.wildwind.imp.annotations.Until;

        public class Target {

            @Since("26.2")
            public static final int NEW_FIELD = 1;

            @Until("26.2")
            public static void legacyMethod() {
            }

            @Since(value = "26.1", until = "26.3")
            public static final int RANGED = 2;

            @Since(value = "26.1", exclude = {"26.2"})
            public static final int GAP = 3;

            public static void render(String graphics) {
            }

            public static void render(String graphics, @ParamSince("26.2") int flag) {
            }
        }
        """;

    @Test
    void manifestsConstantsForModernTarget() {
        var result = ProcessorHarness.compile("Target", SOURCE, "26.2");

        assertTrue(result.success(), "编译应成功，诊断：\n" + result.diagnostics());
        assertEquals(Boolean.TRUE, result.constant("HAS_TARGET_NEW_FIELD"), "@Since 26.2 在 26.2 应存在");
        assertEquals(Boolean.FALSE, result.constant("UNTIL_TARGET_LEGACYMETHOD"), "@Until 26.2 在 26.2 应已移除");
        assertEquals(Boolean.TRUE, result.constant("HAS_TARGET_RANGED"), "[26.1,26.3) 区间内应存在");
        assertEquals(Boolean.FALSE, result.constant("HAS_TARGET_GAP"), "被 exclude 的版本应判为不存在");
        assertEquals(Boolean.TRUE, result.constant("HAS_TARGET_RENDER__STRING__INT"),
            "@ParamSince 的宿主方法应生成常量（回退重载靠它分派）");
    }

    @Test
    void manifestsConstantsForOlderTarget() {
        var result = ProcessorHarness.compile("Target", SOURCE, "26.1");

        assertTrue(result.success(), "编译应成功，诊断：\n" + result.diagnostics());
        assertEquals(Boolean.FALSE, result.constant("HAS_TARGET_NEW_FIELD"), "@Since 26.2 在 26.1 不应存在");
        assertEquals(Boolean.TRUE, result.constant("UNTIL_TARGET_LEGACYMETHOD"), "@Until 26.2 在 26.1 仍存在");
        assertEquals(Boolean.TRUE, result.constant("HAS_TARGET_RANGED"), "区间起点版本应存在");
        assertEquals(Boolean.TRUE, result.constant("HAS_TARGET_GAP"), "非排除版本应存在");
        assertEquals(Boolean.FALSE, result.constant("HAS_TARGET_RENDER__STRING__INT"), "旧版本没有该重载");
    }

    @Test
    void untilBoundaryIsExclusive() {
        var result = ProcessorHarness.compile("Target", SOURCE, "26.3");

        assertTrue(result.success(), "编译应成功，诊断：\n" + result.diagnostics());
        assertEquals(Boolean.FALSE, result.constant("HAS_TARGET_RANGED"), "until 版本起应不再存在（区间左闭右开）");
    }

    @Test
    void rejectsInvertedRange() {
        String bad = """
            package smoke;

            import org.polaris2023.wildwind.imp.annotations.Since;

            public class BadRange {

                @Since(value = "26.3", until = "26.1")
                public static final int BAD = 1;
            }
            """;

        var result = ProcessorHarness.compile("BadRange", bad, "26.2");

        assertFalse(result.success(), "起始版本晚于结束版本必须编译失败");
        assertTrue(result.diagnosticsContain("版本区间非法"),
            "应给出区间非法提示，实际诊断：\n" + result.diagnostics());
    }

    @Test
    void rejectsParamSinceWithoutFallbackOverload() {
        String bad = """
            package smoke;

            import org.polaris2023.wildwind.imp.annotations.ParamSince;

            public class BadFallback {

                public static void render(String graphics, @ParamSince("26.2") int flag) {
                }
            }
            """;

        var result = ProcessorHarness.compile("BadFallback", bad, "26.2");

        assertFalse(result.success(), "缺少回退重载必须编译失败（旧版本没有分派目标）");
        assertTrue(result.diagnosticsContain("缺少回退重载"),
            "应给出回退重载缺失提示，实际诊断：\n" + result.diagnostics());
    }

    @Test
    void rejectsMissingTargetVersion() {
        var result = ProcessorHarness.compile("Target", SOURCE, null);

        assertFalse(result.success(), "未提供目标版本必须编译失败，而不是猜一个默认值");
        assertNull(result.manifestSource(), "失败时不应产出 manifest");
    }
}
