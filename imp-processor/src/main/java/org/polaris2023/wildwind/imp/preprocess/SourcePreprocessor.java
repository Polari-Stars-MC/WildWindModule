package org.polaris2023.wildwind.imp.preprocess;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.polaris2023.wildwind.imp.annotations.ImpVersionSpec;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * imp 源码预处理器（ADR-0003 §3）。
 *
 * <p>为什么必须有它：注解处理器只能"生成新类"，无法删除既有成员、也无法改名。
 * 而"所有版本代码写在同一个类里"要求把不适用该版本的成员真的<b>抹掉</b>，
 * 并且能把符号改名为目标版本的名字。因此这一步必须在 javac 之前对源码做改写。</p>
 *
 * <p>输入：一个源码目录 + 目标 MC 版本；输出：改写后的源码目录。</p>
 *
 * <p>处理内容：</p>
 * <ol>
 *   <li><b>版本裁剪</b>：{@code @Since}/{@code @Until} 判定该成员在目标版本是否存在，
 *       不存在则整块删除（支持 {@code until} 区间与 {@code exclude} 空洞）；</li>
 *   <li><b>符号改名</b>：{@code @Name} 指定的新名字会同时改写声明与调用点；</li>
 *   <li><b>外部符号改名</b>：可选的重命名表，用于把 Minecraft 自身的跨版本改名
 *       （如某方法在 26.x 换名）在调用点改写。</li>
 * </ol>
 *
 * <p>约定：版本分叉的类不写 {@code import}，在成员内用全限定名书写——
 * 这样被删掉的成员连带其类型引用一起消失，无需对 import 做条件编译
 * （Java 不允许注解修饰 import）。</p>
 */
public final class SourcePreprocessor {

    /** 一次改写的统计结果。 */
    public record Report(int files, int removedMembers, int renamedSymbols) {}

    private final String targetVersion;
    /** 解析器：语言级别必须放开到现代 Java，否则 instanceof 模式匹配、record 等会解析失败。 */
    private final JavaParser parser;

    public SourcePreprocessor(String targetVersion) {
        // 提前校验版本可解析，坏版本不要等到改写中途才炸
        ImpVersionSpec.parse(targetVersion);
        this.targetVersion = targetVersion;
        this.parser = new JavaParser(new ParserConfiguration()
            .setLanguageLevel(ParserConfiguration.LanguageLevel.BLEEDING_EDGE));
    }

    /**
     * 处理整个源码目录。
     *
     * @param srcRoot        原始源码根目录
     * @param outRoot        改写后的输出根目录
     * @param renamesFile    外部符号改名表（可为 null）
     */
    public Report process(Path srcRoot, Path outRoot, Path renamesFile) throws IOException {
        List<Path> sources;
        try (Stream<Path> walk = Files.walk(srcRoot)) {
            sources = walk.filter(p -> p.toString().endsWith(".java")).sorted().toList();
        }

        // 一次读入全部文件：改名要跨文件生效（调用点可能在别处）
        Map<Path, CompilationUnit> units = new LinkedHashMap<>();
        for (Path src : sources) {
            try {
                ParseResult<CompilationUnit> result = parser.parse(src);
                CompilationUnit cu = result.getResult().orElseThrow(() ->
                    new IllegalStateException("无法解析：" + src + "\n" + result.getProblems()));
                units.put(src, cu);
            } catch (IOException e) {
                throw new UncheckedIOException("解析失败：" + src, e);
            }
        }

        int removed = 0;
        for (CompilationUnit cu : units.values()) {
            removed += removeInapplicableMembers(cu);
        }

        Map<String, String> renames = collectRenames(units.values());
        if (renamesFile != null && Files.exists(renamesFile)) {
            renames.putAll(loadExternalRenames(renamesFile));
        }

        int renamed = 0;
        if (!renames.isEmpty()) {
            for (CompilationUnit cu : units.values()) {
                renamed += applyRenames(cu, renames);
            }
        }

        for (Map.Entry<Path, CompilationUnit> entry : units.entrySet()) {
            Path relative = srcRoot.relativize(entry.getKey());
            Path target = outRoot.resolve(relative);
            Files.createDirectories(target.getParent());
            Files.writeString(target, entry.getValue().toString(), StandardCharsets.UTF_8);
        }
        return new Report(units.size(), removed, renamed);
    }

    // ── 版本裁剪 ─────────────────────────────────────────────────────────

    private int removeInapplicableMembers(CompilationUnit cu) {
        int count = 0;
        for (TypeDeclaration<?> type : cu.findAll(TypeDeclaration.class)) {
            List<BodyDeclaration<?>> members = new ArrayList<>(type.getMembers());
            for (BodyDeclaration<?> member : members) {
                if (!presentIn(member)) {
                    member.remove();
                    count++;
                }
            }
        }
        return count;
    }

    /** 该成员在目标版本是否存在。没有版本注解的一律保留。 */
    private boolean presentIn(Node node) {
        AnnotationExpr since = findAnnotation(node, "Since");
        if (since != null) {
            String value = stringMember(since, "value").orElse(null);
            String until = stringMember(since, "until").orElse("");
            Set<String> exclude = stringArrayMember(since, "exclude");
            if (value != null) {
                boolean ok = ImpVersionSpec.atLeast(targetVersion, value)
                    && (until.isEmpty() || ImpVersionSpec.before(targetVersion, until))
                    && !ImpVersionSpec.matchesAny(targetVersion, exclude.toArray(new String[0]));
                if (!ok) return false;
            }
        }
        AnnotationExpr until = findAnnotation(node, "Until");
        if (until != null) {
            String value = stringMember(until, "value").orElse(null);
            String sinceVersion = stringMember(until, "since").orElse("");
            Set<String> exclude = stringArrayMember(until, "exclude");
            if (value != null) {
                boolean ok = ImpVersionSpec.before(targetVersion, value)
                    && (sinceVersion.isEmpty() || ImpVersionSpec.atLeast(targetVersion, sinceVersion))
                    && !ImpVersionSpec.matchesAny(targetVersion, exclude.toArray(new String[0]));
                if (!ok) return false;
            }
        }
        return true;
    }

    // ── 符号改名 ─────────────────────────────────────────────────────────

    /** 收集 @Name：旧名 → 新名。同一旧名映射到不同新名视为错误（不做猜测）。 */
    private Map<String, String> collectRenames(Iterable<CompilationUnit> units) {
        Map<String, String> renames = new LinkedHashMap<>();
        for (CompilationUnit cu : units) {
            for (BodyDeclaration<?> member : cu.findAll(BodyDeclaration.class)) {
                AnnotationExpr name = findAnnotation(member, "Name");
                if (name == null) continue;
                String newName = stringMember(name, "value").orElse(null);
                if (newName == null) continue;
                String oldName = declaredName(member);
                if (oldName == null) continue;

                String previous = renames.putIfAbsent(oldName, newName);
                if (previous != null && !previous.equals(newName)) {
                    throw new IllegalStateException(
                        "@Name 冲突：'" + oldName + "' 同时被映射为 '" + previous + "' 与 '" + newName +
                            "'。请给其中之一换个源码内名字，处理器不做猜测。");
                }
            }
        }
        return renames;
    }

    private String declaredName(BodyDeclaration<?> member) {
        if (member instanceof MethodDeclaration m) return m.getNameAsString();
        if (member instanceof FieldDeclaration f) {
            return f.getVariables().isEmpty() ? null : f.getVariable(0).getNameAsString();
        }
        if (member instanceof ClassOrInterfaceDeclaration c) return c.getNameAsString();
        if (member instanceof ConstructorDeclaration c) return c.getNameAsString();
        return null;
    }

    /** 应用改名：声明 + 所有引用点。 */
    private int applyRenames(CompilationUnit cu, Map<String, String> renames) {
        int count = 0;
        for (MethodDeclaration m : cu.findAll(MethodDeclaration.class)) {
            String to = renames.get(m.getNameAsString());
            if (to != null) { m.setName(to); count++; }
        }
        for (ConstructorDeclaration c : cu.findAll(ConstructorDeclaration.class)) {
            String to = renames.get(c.getNameAsString());
            if (to != null) { c.setName(to); count++; }
        }
        for (ClassOrInterfaceDeclaration c : cu.findAll(ClassOrInterfaceDeclaration.class)) {
            String to = renames.get(c.getNameAsString());
            if (to != null) {
                c.setName(to);
                cu.findAll(ObjectCreationExpr.class).forEach(o ->
                    o.getType().getName().setIdentifier(
                        o.getType().getNameAsString().equals(to) ? to : o.getType().getNameAsString()));
                count++;
            }
        }
        for (FieldDeclaration f : cu.findAll(FieldDeclaration.class)) {
            for (VariableDeclarator v : f.getVariables()) {
                String to = renames.get(v.getNameAsString());
                if (to != null) { v.setName(to); count++; }
            }
        }
        for (MethodCallExpr call : cu.findAll(MethodCallExpr.class)) {
            String to = renames.get(call.getNameAsString());
            if (to != null) { call.setName(to); count++; }
        }
        for (FieldAccessExpr access : cu.findAll(FieldAccessExpr.class)) {
            String to = renames.get(access.getNameAsString());
            if (to != null) { access.setName(to); count++; }
        }
        for (NameExpr name : cu.findAll(NameExpr.class)) {
            String to = renames.get(name.getNameAsString());
            if (to != null) { name.setName(to); count++; }
        }
        return count;
    }

    // ── 外部符号改名表 ────────────────────────────────────────────────────

    /**
     * 读取外部改名表：{@code 族列表 => 旧名 => 新名 => 说明}。
     * 仅对当前版本族生效；按<b>简单名</b>匹配，不做类型解析（已知限制，见文档）。
     */
    private Map<String, String> loadExternalRenames(Path file) throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        for (String raw : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] parts = line.split("=>");
            if (parts.length < 3) continue;
            String families = parts[0].trim();
            String from = parts[1].trim();
            String to = parts[2].trim();
            if (!families.equals("*") && !List.of(families.split(",")).stream()
                    .map(String::trim).toList().contains(familyOf(targetVersion))) {
                continue;
            }
            map.put(from, to);
        }
        return map;
    }

    private static String familyOf(String version) {
        return version.startsWith("1.") ? version.split("\\.")[0] + "." + version.split("\\.")[1]
            : version.substring(0, version.indexOf('.')) + ".x";
    }

    // ── 注解读取 ─────────────────────────────────────────────────────────

    private static AnnotationExpr findAnnotation(Node node, String simpleName) {
        List<AnnotationExpr> annotations = new ArrayList<>();
        if (node instanceof BodyDeclaration<?> b) annotations.addAll(b.getAnnotations());
        for (AnnotationExpr a : annotations) {
            if (a.getNameAsString().endsWith(simpleName)) return a;
        }
        return null;
    }

    private static java.util.Optional<String> stringMember(AnnotationExpr annotation, String member) {
        if (annotation instanceof SingleMemberAnnotationExpr single) {
            if ("value".equals(member) && single.getMemberValue() instanceof StringLiteralExpr s) {
                return java.util.Optional.of(s.asString());
            }
            return java.util.Optional.empty();
        }
        if (annotation instanceof NormalAnnotationExpr normal) {
            for (var pair : normal.getPairs()) {
                if (pair.getNameAsString().equals(member) && pair.getValue() instanceof StringLiteralExpr s) {
                    return java.util.Optional.of(s.asString());
                }
            }
        }
        return java.util.Optional.empty();
    }

    private static Set<String> stringArrayMember(AnnotationExpr annotation, String member) {
        Set<String> values = new LinkedHashSet<>();
        if (annotation instanceof NormalAnnotationExpr normal) {
            for (var pair : normal.getPairs()) {
                if (!pair.getNameAsString().equals(member)) continue;
                Expression value = pair.getValue();
                if (value instanceof ArrayInitializerExpr array) {
                    for (Expression element : array.getValues()) {
                        if (element instanceof StringLiteralExpr s) values.add(s.asString());
                    }
                } else if (value instanceof StringLiteralExpr s) {
                    values.add(s.asString());
                }
            }
        }
        return values;
    }

    /**
     * 命令行入口：{@code <srcRoot> <outRoot> <targetVersion> [renamesFile]}。
     *
     * <p>会先清空 {@code outRoot}：增量构建时残留的旧文件（例如某版本独有的类）
     * 不会被覆盖，留着会让后续编译看到不该存在的源码。</p>
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("用法: SourcePreprocessor <srcRoot> <outRoot> <targetVersion> [renamesFile]");
            System.exit(2);
            return;
        }
        Path srcRoot = Path.of(args[0]);
        Path outRoot = Path.of(args[1]);
        String target = args[2];
        Path renames = args.length > 3 && !args[3].isBlank() ? Path.of(args[3]) : null;

        deleteTree(outRoot);
        Report report = new SourcePreprocessor(target).process(srcRoot, outRoot, renames);
        System.out.printf("imp 预处理完成：%d 个文件，裁剪成员 %d 处，改名 %d 处（target=%s）%n",
            report.files(), report.removedMembers(), report.renamedSymbols(), target);
    }

    /** 递归删除目录；不存在时静默返回。 */
    private static void deleteTree(Path root) throws IOException {
        if (!Files.exists(root)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(root)) {
            for (Path path : walk.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }
}
