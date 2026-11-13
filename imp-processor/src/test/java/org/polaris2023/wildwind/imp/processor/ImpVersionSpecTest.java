package org.polaris2023.wildwind.imp.processor;

import org.polaris2023.wildwind.imp.annotations.ImpVersionSpec;

/**
 * 版本比较语义的单元测试。
 *
 * <p>这里是整套注解体系的排序基础：一旦比较逻辑错了，所有 {@code @Since}/{@code @Until}
 * 都会静默判错版本，且不会在别处暴露。</p>
 */
final class ImpVersionSpecTest {

    @org.junit.jupiter.api.Test
    void parsesNumericSegments() {
        org.junit.jupiter.api.Assertions.assertArrayEquals(new int[]{26, 2}, ImpVersionSpec.parse("26.2"));
        org.junit.jupiter.api.Assertions.assertArrayEquals(new int[]{1, 20, 4}, ImpVersionSpec.parse("1.20.4"));
    }

    @org.junit.jupiter.api.Test
    void ordersAcrossMajorVersions() {
        // 1.20.4 < 1.21.1 < 26.2 —— 跨大版本的排序必须成立
        org.junit.jupiter.api.Assertions.assertTrue(ImpVersionSpec.atLeast("26.2", "1.21.1"));
        org.junit.jupiter.api.Assertions.assertTrue(ImpVersionSpec.atLeast("1.21.1", "1.20.4"));
        org.junit.jupiter.api.Assertions.assertFalse(ImpVersionSpec.atLeast("1.20.4", "1.21.1"));
    }

    @org.junit.jupiter.api.Test
    void treatsMissingSegmentsAsZero() {
        // 26.2 与 26.2.0 必须是同一版本，否则 exclude 会漏匹配
        org.junit.jupiter.api.Assertions.assertTrue(ImpVersionSpec.atLeast("26.2.0", "26.2"));
        org.junit.jupiter.api.Assertions.assertTrue(ImpVersionSpec.atLeast("26.2", "26.2.0"));
        org.junit.jupiter.api.Assertions.assertTrue(ImpVersionSpec.matchesAny("26.2.0", new String[]{"26.2"}));
    }

    @org.junit.jupiter.api.Test
    void rejectsNonNumericVersions() {
        // 宁可炸掉也不猜
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
            () -> ImpVersionSpec.parse("26.2-snapshot"));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
            () -> ImpVersionSpec.parse(""));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
            () -> ImpVersionSpec.parse(null));
    }

    @org.junit.jupiter.api.Test
    void beforeIsStrict() {
        org.junit.jupiter.api.Assertions.assertTrue(ImpVersionSpec.before("26.1", "26.2"));
        org.junit.jupiter.api.Assertions.assertFalse(ImpVersionSpec.before("26.2", "26.2"));
    }
}
