package org.polaris2023.wildwind.imp.processor;

import org.polaris2023.wildwind.imp.annotations.ParamSince;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link ParamSince} 校验器（ADR-0003 §3.2）。
 *
 * <p>参数差异用"重载 + 适配器分派"表达，因此带 {@code @ParamSince} 参数的方法
 * <b>必须</b>存在一个去掉全部此类参数的同签名重载（返回类型一致、其余参数按序一致），
 * 否则旧版本将没有可分派的目标。编译期立即报错。</p>
 */
final class ParamSinceValidator {

    private ParamSinceValidator() {}

    static void validate(ProcessingEnvironment env, RoundEnvironment roundEnv) {
        Messager messager = env.getMessager();

        // 收集每个宿主类型中"带 ParamSince 参数的方法"
        Map<TypeElement, List<ExecutableElement>> hosts = new LinkedHashMap<>();
        for (var param : roundEnv.getElementsAnnotatedWith(ParamSince.class)) {
            if (param.getEnclosingElement() instanceof ExecutableElement m
                    && m.getEnclosingElement() instanceof TypeElement host) {
                hosts.computeIfAbsent(host, k -> new ArrayList<>()).add(m);
            }
        }

        hosts.forEach((host, methods) -> {
            List<ExecutableElement> allMethods = new ArrayList<>();
            host.getEnclosedElements().forEach(e -> {
                if (e instanceof ExecutableElement m) allMethods.add(m);
            });

            for (ExecutableElement m : methods) {
                boolean hasFallback = allMethods.stream().anyMatch(other ->
                    other != m
                        && other.getSimpleName().equals(m.getSimpleName())
                        && sameReturnType(m, other)
                        && fallbackMatches(m, other));
                if (!hasFallback) {
                    messager.printMessage(Diagnostic.Kind.ERROR,
                        "@" + ParamSince.class.getSimpleName() + " 参数的方法缺少回退重载：需要同名、同返回类型、"
                            + "且参数列表为「其余参数」的方法（旧版本分派目标）。见 ADR-0003 §3.2。",
                        m);
                }
            }
        });
    }

    private static boolean sameReturnType(ExecutableElement a, ExecutableElement b) {
        return a.getReturnType().toString().equals(b.getReturnType().toString());
    }

    /** other 的参数序列是否等于 m 去掉全部 @ParamSince 参数后的序列。 */
    static boolean fallbackMatches(ExecutableElement m, ExecutableElement other) {
        List<String> kept = new ArrayList<>();
        m.getParameters().forEach(p -> {
            if (p.getAnnotation(ParamSince.class) == null) kept.add(p.asType().toString());
        });
        List<String> otherTypes = other.getParameters().stream()
            .map(p -> p.asType().toString()).toList();
        return kept.equals(otherTypes);
    }
}
