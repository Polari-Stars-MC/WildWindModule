package org.polaris2023.wildwind.imp.processor;

import com.google.auto.service.AutoService;
import org.polaris2023.wildwind.imp.annotations.ImpVersionSpec;
import org.polaris2023.wildwind.imp.annotations.ParamSince;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * imp 中间层注解处理器（ADR-0003 阶段二）。
 *
 * <p>读取目标版本标量（{@code -AimpTargetMcVersion=26.2} 或环境属性 {@code targetMcVersion}），
 * 扫描 {@link Since} / {@link Until} 注解，生成 {@code org.polaris2023.wildwind.imp.runtime.ImpManifest}：
 * 每个被注解的成员对应一个 {@code public static final boolean} 常量，命名
 * {@code HAS_<类扁平名>_<成员名>}（Since 语义）或 {@code UNTIL_<...>}（Until 语义）。</p>
 *
 * <p>常量在编译期被 javac 内联，运行时零反射零开销（ADR-0003"后果"节）。</p>
 */
@AutoService(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes({
    "org.polaris2023.wildwind.imp.annotations.Since",
    "org.polaris2023.wildwind.imp.annotations.Until",
    "org.polaris2023.wildwind.imp.annotations.ParamSince",
})
@SupportedOptions({"impTargetMcVersion", "targetMcVersion"})
public final class ImpProcessor extends AbstractProcessor {

    public static final String OPTION_TARGET = "impTargetMcVersion";
    /** 回退：未传 -A 时从 gradle 同名属性注入（build.gradle.kts 负责）。 */
    public static final String OPTION_TARGET_FALLBACK = "targetMcVersion";

    /** 跟随 javac 的 source 版本（不写死 RELEASE_21，避免上层用更高版本编译时报错）。 */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Messager messager;
    private String targetVersion;
    private boolean targetResolved;
    private boolean manifestGenerated;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.messager = env.getMessager();
    }

    private String resolveTargetVersion() {
        if (targetResolved) return targetVersion;
        targetResolved = true;
        String v = processingEnv.getOptions().get(OPTION_TARGET);
        if (v == null || v.isBlank()) {
            v = processingEnv.getOptions().get(OPTION_TARGET_FALLBACK);
        }
        if (v == null || v.isBlank()) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                "未提供目标版本：请以 -A" + OPTION_TARGET + "=<mc版本> 传入（构建脚本从 gradle 属性 targetMcVersion 注入）");
            targetVersion = null;
        } else {
            // 提前校验可解析性，坏版本号在此处失败而不是生成阶段
            try {
                ImpVersionSpec.parse(v);
                targetVersion = v.trim();
            } catch (IllegalArgumentException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "目标版本非法：" + e.getMessage());
                targetVersion = null;
            }
        }
        return targetVersion;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        String target = resolveTargetVersion();
        if (target == null) {
            return false; // 版本未定：让编译失败于更明确的错误信息
        }

        ManifestBuilder builder = new ManifestBuilder(processingEnv);

        for (Element annotated : roundEnv.getElementsAnnotatedWith(Since.class)) {
            Since since = annotated.getAnnotation(Since.class);
            boolean exists = ImpVersionSpec.atLeast(target, since.value())
                && (since.until().isEmpty() || ImpVersionSpec.before(target, since.until()))
                && !ImpVersionSpec.matchesAny(target, since.exclude());
            validateKind(annotated, Since.class.getSimpleName());
            validateRange(annotated, since.value(), since.until());
            builder.addEntry(annotated, "HAS_", since.value(), exists);
        }

        for (Element annotated : roundEnv.getElementsAnnotatedWith(Until.class)) {
            Until until = annotated.getAnnotation(Until.class);
            boolean exists = ImpVersionSpec.before(target, until.value())
                && (until.since().isEmpty() || ImpVersionSpec.atLeast(target, until.since()))
                && !ImpVersionSpec.matchesAny(target, until.exclude());
            validateKind(annotated, Until.class.getSimpleName());
            validateRange(annotated, until.since(), until.value());
            builder.addEntry(annotated, "UNTIL_", until.value(), exists);
        }

        // 带 @ParamSince 参数的方法：为宿主方法生成 HAS_ 常量（表示"新版本重载存在"），
        // 供回退重载做分派判断（ADR-0003 §3.2）
        Set<ExecutableElement> paramSinceHosts = new java.util.LinkedHashSet<>();
        for (Element param : roundEnv.getElementsAnnotatedWith(ParamSince.class)) {
            if (param.getEnclosingElement() instanceof ExecutableElement m) {
                paramSinceHosts.add(m);
            }
        }
        for (ExecutableElement m : paramSinceHosts) {
            String since = m.getParameters().stream()
                .map(p -> p.getAnnotation(ParamSince.class))
                .filter(java.util.Objects::nonNull)
                .map(ParamSince::value)
                .findFirst().orElseThrow();
            boolean exists = ImpVersionSpec.atLeast(target, since);
            builder.addEntry(m, "HAS_", since, exists);
        }

        ParamSinceValidator.validate(processingEnv, roundEnv);

        if (!roundEnv.processingOver() && !manifestGenerated) {
            builder.generate(target);
            manifestGenerated = true;
        }
        return false; // 不声明注解所有权，允许其他处理器共存
    }

    /** @Since/@Until 只允许打在包、类、字段、方法、构造器上。 */
    private void validateKind(Element e, String annotationName) {
        ElementKind kind = e.getKind();
        if (kind != ElementKind.CLASS && kind != ElementKind.INTERFACE
                && kind != ElementKind.ENUM && kind != ElementKind.RECORD
                && kind != ElementKind.FIELD && kind != ElementKind.METHOD
                && kind != ElementKind.CONSTRUCTOR && kind != ElementKind.PACKAGE) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                "@" + annotationName + " 不支持打在 " + kind + " 上", e);
        }
    }

    /** 区间合法性：起始版本必须早于结束版本（两者都提供时）。 */
    private void validateRange(Element e, String start, String end) {
        if (!start.isEmpty() && !end.isEmpty() && ImpVersionSpec.atLeast(start, end)) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                "版本区间非法：起始 " + start + " 不早于结束 " + end + "（区间应为 [start, end)）", e);
        }
    }
}
