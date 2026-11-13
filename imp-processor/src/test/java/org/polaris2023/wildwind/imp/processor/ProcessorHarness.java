package org.polaris2023.wildwind.imp.processor;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试辅助：用 JDK 自带 {@link JavaCompiler} 真实跑一遍处理器。
 *
 * <p>不引入 compile-testing 之类的重框架：处理器本身就在本模块 main 里，
 * 直接以 {@code -processor} 指定全限定名调用，和真实构建路径一致。</p>
 */
final class ProcessorHarness {

    record Result(boolean success, String diagnostics, String manifestSource) {

        /** 是否包含给定文本（大小写敏感）。 */
        boolean diagnosticsContain(String needle) {
            return diagnostics.contains(needle);
        }

        /** 读取 manifest 中某个布尔常量的值；不存在返回 null。 */
        Boolean constant(String name) {
            if (manifestSource == null) {
                return null;
            }
            Matcher m = Pattern
                .compile("public static final boolean " + Pattern.quote(name) + " = (true|false);")
                .matcher(manifestSource);
            return m.find() ? Boolean.parseBoolean(m.group(1)) : null;
        }
    }

    private static final String PROCESSOR = ImpProcessor.class.getName();

    private ProcessorHarness() {}

    /**
     * 编译一段源码。
     *
     * @param className    源码中的公有类名
     * @param source       Java 源码正文
     * @param targetMcVersion 传给 -AimpTargetMcVersion 的值；null 表示不传（用于验证缺参报错）
     */
    static Result compile(String className, String source, String targetMcVersion) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("当前 JVM 没有 javac（需要 JDK 而非 JRE）");
        }

        Path work;
        try {
            work = Files.createTempDirectory("imp-proc-test");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Path srcDir = work.resolve("src");
        Path outDir = work.resolve("out");
        Path genDir = work.resolve("gen");
        try {
            Files.createDirectories(srcDir);
            Files.createDirectories(outDir);
            Files.createDirectories(genDir);
            Path srcFile = srcDir.resolve(className + ".java");
            Files.writeString(srcFile, source, StandardCharsets.UTF_8);

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            try (StandardJavaFileManager fm = compiler.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8)) {
                fm.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(outDir));
                fm.setLocationFromPaths(StandardLocation.SOURCE_OUTPUT, List.of(genDir));

                List<String> options = new ArrayList<>(List.of(
                    "-encoding", "UTF-8",
                    "-classpath", System.getProperty("java.class.path"),
                    "-processor", PROCESSOR,
                    "-proc:full"
                ));
                if (targetMcVersion != null) {
                    options.add("-AimpTargetMcVersion=" + targetMcVersion);
                }

                Iterable<? extends JavaFileObject> units =
                    fm.getJavaFileObjectsFromPaths(List.of(srcFile));
                boolean ok = compiler.getTask(null, fm, diagnostics, options, null, units).call();

                String diagText = render(diagnostics);
                String manifest = readManifest(genDir);
                return new Result(ok, diagText, manifest);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            deleteRecursively(work);
        }
    }

    private static String render(DiagnosticCollector<JavaFileObject> diagnostics) {
        StringBuilder sb = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
            sb.append(d.getKind()).append(": ").append(d.getMessage(Locale.ROOT)).append('\n');
        }
        return sb.toString();
    }

    private static String readManifest(Path genDir) throws IOException {
        Path manifest = genDir.resolve("org/polaris2023/wildwind/imp/runtime/ImpManifest.java");
        return Files.exists(manifest) ? Files.readString(manifest, StandardCharsets.UTF_8) : null;
    }

    private static void deleteRecursively(Path path) {
        try (var walk = Files.walk(path)) {
            walk.sorted((a, b) -> b.getNameCount() - a.getNameCount()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException ignored) {
                    // 临时目录清理失败不影响断言结果
                }
            });
        } catch (IOException ignored) {
            // 同上
        }
    }
}
