package org.polaris2023.wildwind.imp.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 生成 {@code org.polaris2023.wildwind.imp.runtime.ImpManifest}。
 *
 * <p>每个被 @Since/@Until 注解的成员生成一个布尔常量：
 * {@code HAS_<Type扁平名>_<成员名>}（Since）/ {@code UNTIL_<...>}（Until）。
 * 方法含参数列表摘要以区分重载。同一成员同时打 @Since 与 @Until 时报错。
 * 纯文本生成，无第三方模板依赖，方便以单一 jar 分发处理器。</p>
 */
final class ManifestBuilder {

    private static final String PKG = "org.polaris2023.wildwind.imp.runtime";
    private static final String CLASS = "ImpManifest";

    private final ProcessingEnvironment env;
    /** 常量名 → (注释描述, 值)。LinkedHashMap 保证生成顺序稳定 → 输出确定（可复现构建）。 */
    private final Map<String, Entry> entries = new LinkedHashMap<>();

    record Entry(String javadoc, boolean value) {}

    ManifestBuilder(ProcessingEnvironment env) {
        this.env = env;
    }

    void addEntry(Element annotated, String prefix, String annotatedVersion, boolean exists) {
        String constName = prefix + memberKey(annotated);
        Entry prev = entries.putIfAbsent(constName, new Entry(describe(annotated, annotatedVersion), exists));
        if (prev != null && prev.value() != exists) {
            env.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "成员同时命中 @Since 与 @Until（或重复注册）：" + constName, annotated);
        }
    }

    /** 生成常量类；没有任何条目时仍生成空类，保证下游引用稳定。每轮编译只写一次。 */
    void generate(String targetVersion) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("package ").append(PKG).append(";\n\n");
        sb.append("/** 由 imp-processor 生成（targetMcVersion=").append(targetVersion).append("），请勿手改。 */\n");
        sb.append("public final class ").append(CLASS).append(" {\n\n");
        entries.forEach((name, entry) -> {
            sb.append("    /** ").append(entry.javadoc()).append(" */\n");
            sb.append("    public static final boolean ").append(name)
              .append(" = ").append(entry.value()).append(";\n\n");
        });
        sb.append("    private ").append(CLASS).append("() {\n    }\n}\n");

        try {
            JavaFileObject file = env.getFiler().createSourceFile(PKG + "." + CLASS);
            try (PrintWriter w = new PrintWriter(file.openWriter(), false)) {
                w.print(sb);
            }
        } catch (IOException e) {
            env.getMessager().printMessage(Diagnostic.Kind.ERROR, "ImpManifest 写入失败：" + e.getMessage());
        }
    }

    /** 常量名：HAS_TypeName_member 或 HAS_TypeName_member__I_I（重载摘要）。 */
    private String memberKey(Element e) {
        String owner = flattenOwner(e);
        String member = switch (e.getKind()) {
            case FIELD, ENUM_CONSTANT -> e.getSimpleName().toString();
            case METHOD, CONSTRUCTOR -> {
                ExecutableElement m = (ExecutableElement) e;
                StringBuilder sb = new StringBuilder(m.getSimpleName().toString());
                m.getParameters().forEach(p -> sb.append("__").append(typeDigest(p.asType().toString())));
                yield sb.toString();
            }
            default -> "TYPE"; // 打在类/接口/包上
        };
        return sanitize(owner + "_" + member).toUpperCase();
    }

    private String flattenOwner(Element e) {
        Element cur = e;
        while (!(cur instanceof PackageElement)) {
            if (cur instanceof TypeElement t) {
                return t.getSimpleName().toString();
            }
            cur = cur.getEnclosingElement();
        }
        return "Root";
    }

    /** 参数类型摘要：全限定名压成首字母+类名，避免常量名过长。 */
    private String typeDigest(String type) {
        String simple = type.substring(type.lastIndexOf('.') + 1);
        return simple.replace("[]", "A").replaceAll("[^A-Za-z0-9]", "");
    }

    private String sanitize(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (char c : s.toCharArray()) {
            sb.append(Character.isJavaIdentifierPart(c) ? c : '_');
        }
        return sb.toString();
    }

    private String describe(Element e, String version) {
        if (e instanceof VariableElement v && v.getEnclosingElement() instanceof ExecutableElement m) {
            return m.getEnclosingElement().getSimpleName() + "#" + m.getSimpleName() + " 参数 " + v.getSimpleName() + " (@" + version + ")";
        }
        if (e instanceof ExecutableElement m) {
            return m.getEnclosingElement().getSimpleName() + "#" + m.getSimpleName() + " (@" + version + ")";
        }
        return e.getSimpleName() + " (@" + version + ")";
    }
}
