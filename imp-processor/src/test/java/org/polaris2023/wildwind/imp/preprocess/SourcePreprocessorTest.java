package org.polaris2023.wildwind.imp.preprocess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 源码预处理器的回归测试。
 *
 * <p>它承担了"删代码 + 改名"这两件 APT 做不到的事，是单类多版本方案的地基，
 * 语义必须被钉住。</p>
 */
final class SourcePreprocessorTest {

    private static final String SAMPLE = """
        package smoke;

        import org.polaris2023.wildwind.imp.annotations.Name;
        import org.polaris2023.wildwind.imp.annotations.Since;
        import org.polaris2023.wildwind.imp.annotations.Until;

        public class Sample {

            @Since("26.1")
            @Name("value")
            public static String modern() {
                return "modern";
            }

            @Since("1.21")
            @Until("26.1")
            @Name("value")
            public static String ten21() {
                return "1.21";
            }

            @Until("1.21")
            @Name("value")
            public static String ten20() {
                return "1.20";
            }

            @Since("26.1")
            public static final int FUTURE_ONLY = 1;

            @Until("26.1")
            @Since("1.21")
            public static final int GAPPED = 2;

            public static String call() {
                return value();
            }
        }
        """;

    private Path writeSample(Path dir) throws IOException {
        Path src = dir.resolve("src/smoke");
        Files.createDirectories(src);
        Path file = src.resolve("Sample.java");
        Files.writeString(file, SAMPLE, StandardCharsets.UTF_8);
        return dir.resolve("src");
    }

    private String preprocess(Path tmp, String version) throws IOException {
        Path srcRoot = writeSample(tmp);
        Path outRoot = tmp.resolve("out");
        new SourcePreprocessor(version).process(srcRoot, outRoot, null);
        return Files.readString(outRoot.resolve("smoke/Sample.java"), StandardCharsets.UTF_8);
    }

    @Test
    void keepsOnlyModernBranchForNewVersion(@TempDir Path tmp) throws IOException {
        String out = preprocess(tmp, "26.2");

        assertTrue(out.contains("return \"modern\";"), "应保留 26.x 分支");
        assertFalse(out.contains("return \"1.21\";"), "1.21 分支应被裁掉");
        assertFalse(out.contains("return \"1.20\";"), "1.20 分支应被裁掉");
        assertTrue(out.contains("FUTURE_ONLY"), "@Since 26.1 在 26.2 应保留");
        assertFalse(out.contains("GAPPED"), "GAPPED 上限是 26.1，在 26.2 应被裁掉");
    }

    @Test
    void keepsTen21BranchForMiddleVersion(@TempDir Path tmp) throws IOException {
        String out = preprocess(tmp, "1.21.1");

        assertFalse(out.contains("return \"modern\";"), "26.x 分支应被裁掉");
        assertTrue(out.contains("return \"1.21\";"), "[1.21, 26.1) 区间内应保留");
        assertFalse(out.contains("return \"1.20\";"), "1.20 分支应被裁掉");
        assertFalse(out.contains("FUTURE_ONLY"), "@Since 26.1 在 1.21 应被裁掉");
        assertTrue(out.contains("GAPPED"), "[1.21, 26.1) 内应保留");
    }

    @Test
    void keepsOldBranchForTen20(@TempDir Path tmp) throws IOException {
        String out = preprocess(tmp, "1.20.4");

        assertTrue(out.contains("return \"1.20\";"), "@Until 1.21 在 1.20 应保留");
        assertFalse(out.contains("return \"modern\";"));
        assertFalse(out.contains("GAPPED"), "@Since 1.21 在 1.20 应被裁掉");
    }

    @Test
    void renamesDeclarationAndCallSites(@TempDir Path tmp) throws IOException {
        String out = preprocess(tmp, "26.2");

        assertTrue(out.contains("public static String value()"), "@Name 应改写声明名");
        assertFalse(out.contains("modern()"), "旧声明名不应残留");
        assertTrue(out.contains("value();"), "调用点应指向新名字");
    }

    @Test
    void rejectsAmbiguousNameMapping(@TempDir Path tmp) throws IOException {
        Path src = tmp.resolve("src/smoke");
        Files.createDirectories(src);
        Files.writeString(src.resolve("Clash.java"), """
            package smoke;

            import org.polaris2023.wildwind.imp.annotations.Name;

            public class Clash {

                @Name("same")
                public static void a() {
                }

                @Name("same")
                public static void b() {
                }
            }
            """, StandardCharsets.UTF_8);

        // a 与 b 都映射到 same —— 不是错误；真正的歧义是同一旧名映射到不同新名
        new SourcePreprocessor("26.2").process(tmp.resolve("src"), tmp.resolve("out"), null);

        Files.writeString(src.resolve("Clash2.java"), """
            package smoke;

            import org.polaris2023.wildwind.imp.annotations.Name;

            public class Clash2 {

                @Name("first")
                public static void dup() {
                }
            }
            """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Clash3.java"), """
            package smoke;

            import org.polaris2023.wildwind.imp.annotations.Name;

            public class Clash3 {

                @Name("second")
                public static void dup() {
                }
            }
            """, StandardCharsets.UTF_8);

        assertThrows(IllegalStateException.class,
            () -> new SourcePreprocessor("26.2").process(tmp.resolve("src"), tmp.resolve("out2"), null),
            "同一旧名映射到两个新名必须报错，而不是随便挑一个");
    }

    @Test
    void appliesExternalRenameTable(@TempDir Path tmp) throws IOException {
        Path src = tmp.resolve("src/smoke");
        Files.createDirectories(src);
        Files.writeString(src.resolve("Caller.java"), """
            package smoke;

            public class Caller {

                public static int read(Object o) {
                    return oldGetter(o);
                }

                private static int oldGetter(Object o) {
                    return 0;
                }
            }
            """, StandardCharsets.UTF_8);

        Path table = tmp.resolve("renames.txt");
        Files.writeString(table, "26.x => oldGetter => newGetter => 26.x 改名\n", StandardCharsets.UTF_8);

        new SourcePreprocessor("26.2").process(tmp.resolve("src"), tmp.resolve("out"), table);
        String out = Files.readString(tmp.resolve("out/smoke/Caller.java"), StandardCharsets.UTF_8);

        assertTrue(out.contains("newGetter(o)"), "调用点应按外部改名表改写");
        assertTrue(out.contains("newGetter(Object o)"), "声明也应按改改名表改写");
    }

    @Test
    void externalTableRespectsFamilyFilter(@TempDir Path tmp) throws IOException {
        Path src = tmp.resolve("src/smoke");
        Files.createDirectories(src);
        Files.writeString(src.resolve("Caller.java"), """
            package smoke;

            public class Caller {

                public static int read() {
                    return oldGetter();
                }

                private static int oldGetter() {
                    return 0;
                }
            }
            """, StandardCharsets.UTF_8);

        Path table = tmp.resolve("renames.txt");
        Files.writeString(table, "26.x => oldGetter => newGetter => 仅 26.x\n", StandardCharsets.UTF_8);

        new SourcePreprocessor("1.21.1").process(tmp.resolve("src"), tmp.resolve("out"), table);
        String out = Files.readString(tmp.resolve("out/smoke/Caller.java"), StandardCharsets.UTF_8);

        assertTrue(out.contains("oldGetter()"), "不属于当前族的规则不应生效");
    }

    @Test
    void rejectsUnparsableTargetVersion(@TempDir Path tmp) {
        assertThrows(IllegalArgumentException.class, () -> new SourcePreprocessor("26.2-beta"));
    }

    @Test
    void allowsVaryingReturnTypesAcrossVersionBranches(@TempDir Path tmp) throws IOException {
        // 同一方法名在不同版本返回不同类型：只要每次只留一个分支，Java 里完全合法。
        // 这是"imp 可以返回 MC 原生类型"的实现依据。
        Path src = tmp.resolve("src/smoke");
        Files.createDirectories(src);
        Files.writeString(src.resolve("IdFactory.java"), """
            package smoke;

            import org.polaris2023.wildwind.imp.annotations.Name;
            import org.polaris2023.wildwind.imp.annotations.Since;
            import org.polaris2023.wildwind.imp.annotations.Until;

            public class IdFactory {

                @Since("26.1")
                @Name("make")
                public static String makeModern(String path) {
                    return "modern:" + path;
                }

                @Until("26.1")
                @Name("make")
                public static Integer makeLegacy(String path) {
                    return path.length();
                }

                public static String usesStringResult(String path) {
                    return String.valueOf(make(path));
                }
            }
            """, StandardCharsets.UTF_8);

        new SourcePreprocessor("26.2").process(tmp.resolve("src"), tmp.resolve("out"), null);
        String modern = Files.readString(tmp.resolve("out/smoke/IdFactory.java"), StandardCharsets.UTF_8);
        assertTrue(modern.contains("public static String make("), "26.2 应保留 String 返回类型的分支");
        assertFalse(modern.contains("public static Integer make("), "另一分支应被删除");
        assertTrue(modern.contains("String.valueOf(make(path))"), "共用调用点应保留");

        new SourcePreprocessor("1.21.1").process(tmp.resolve("src"), tmp.resolve("out2"), null);
        String legacy = Files.readString(tmp.resolve("out2/smoke/IdFactory.java"), StandardCharsets.UTF_8);
        assertTrue(legacy.contains("public static Integer make("), "1.21 应保留 Integer 返回类型的分支");
        assertFalse(legacy.contains("public static String make("), "另一分支应被删除");
    }
}
