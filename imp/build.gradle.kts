import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.CommandLineArgumentProvider
import java.util.Properties

plugins {
    `java-library`
    idea
    base
    // moddev 版本在根 scripts 的 plugins 块已声明，此处免版本引用
    id("net.neoforged.moddev")
}

// imp：版本中间层模块（ADR-0003）
//
// 设计：所有版本的相关代码写在同一个类里，用注解声明存废与改名；
// 构建时由源码预处理器（imp-processor 的 SourcePreprocessor）按 targetMcVersion
// 裁掉不适用的成员、把 @Name 改成目标名字，再交给 javac。
//
// 为什么不是注解处理器：APT 只能生成新类，删不掉既有成员、也改不了名字。
// 所以主力是编译前的源码改写，APT（ImpManifest）只作为可选的版本常量来源保留。

val modGroupId: String by rootProject
val modVersion: String by rootProject
val minecraftVersion: String by rootProject

/** 目标 MC 版本标量（唯一事实来源；-PtargetMcVersion 覆盖 gradle.properties）。 */
val targetMcVersion: String = (findProperty("targetMcVersion") as String?) ?: minecraftVersion

/**
 * 版本族表：单一事实源在 gradle/version-families.properties（与根脚本、CI 矩阵共用）。
 * 条目格式：族=minecraft版本|neoForge版本|java级别。
 */
val versionFamilies: Map<String, Triple<String, String, Int>> =
    Properties().apply {
        rootProject.file("gradle/version-families.properties").inputStream().use { load(it) }
    }.entries.associate { entry ->
        val parts = entry.value.toString().split('|')
        require(parts.size == 3) {
            "版本族条目格式错误：${entry.key}=${entry.value}（应为 mc|neoForge|java）"
        }
        entry.key.toString() to Triple(parts[0].trim(), parts[1].trim(), parts[2].trim().toInt())
    }

val versionFamily: String = if (targetMcVersion.startsWith("1.")) {
    val parts = targetMcVersion.split(".")
    parts[0] + "." + parts[1]
} else {
    targetMcVersion.substringBefore('.') + ".x"
}

val familyDefault: Triple<String, String, Int> = versionFamilies[versionFamily]
    ?: error(
        "未知的版本族 '$versionFamily'（来自 targetMcVersion=$targetMcVersion）。" +
            "已支持：" + versionFamilies.keys.joinToString(" / ")
    )

/**
 * Java 语言级别由 MC 版本决定（实测约束）：
 * < 1.20.5 → 17 ；1.20.5 ~ 26 之前 → 21 ；>= 26 → 25。
 */
fun javaReleaseFor(mcVersion: String): Int {
    val segs = mcVersion.split(".").map { it.toIntOrNull() ?: 0 }
    val major = segs.getOrElse(0) { 0 }
    val minor = segs.getOrElse(1) { 0 }
    return when {
        major >= 26 -> 25
        major == 1 && minor >= 21 -> 21
        major == 1 && minor == 20 && segs.getOrElse(2) { 0 } >= 5 -> 21
        else -> 17
    }
}

/** 该族对应的 Java 语言级别：按**实际目标版本**推导（不是族锚点），否则 1.20.6 会被错判为 17。 */
val familyJavaRelease: Int = javaReleaseFor(targetMcVersion)
val effectiveNeoVersion: String = (findProperty("impNeoVersion") as String?) ?: familyDefault.second

group = modGroupId
version = modVersion
base.archivesName = "ww-imp-mc$targetMcVersion"

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

// ── 源码预处理：按目标版本裁剪 + 改名 ─────────────────────────────────────
val preprocessorClasspath by configurations.creating

dependencies {
    compileOnly(project(":imp-annotations"))
    annotationProcessor(project(":imp-processor"))
    preprocessorClasspath(project(":imp-processor"))
}

val rawSourceDir = layout.projectDirectory.dir("src/main/java")
val preprocessedDir = layout.buildDirectory.dir("preprocessed/java")
val renamesFile = rootProject.file("gradle/imp-renames.txt")

// 注意：这里刻意在配置期就把路径解析成不可变值。
// 早期版本在 doFirst 闭包里捕获了 Provider 与脚本对象，导致 configuration cache
// 无法序列化该任务（"cannot serialize Gradle script object references"），
// 使 :imp:build 在开启 config cache 时直接失败。
val preprocessOutDir: File = preprocessedDir.get().asFile
val preprocessSrcDir: File = rawSourceDir.asFile
val preprocessRenames: File = renamesFile

val preprocessImpSources = tasks.register<JavaExec>("preprocessImpSources") {
    group = "build"
    description = "按 targetMcVersion 裁剪 imp 源码并应用 @Name 改名"
    classpath = files(preprocessorClasspath)
    mainClass.set("org.polaris2023.wildwind.imp.preprocess.SourcePreprocessor")

    inputs.dir(rawSourceDir).withPathSensitivity(PathSensitivity.RELATIVE)
    inputs.file(renamesFile).optional()
    inputs.property("targetMcVersion", targetMcVersion)
    outputs.dir(preprocessedDir)

    // 输出目录的清理由预处理器自己完成（main 里会先删再写）：
    // 不在 doFirst 闭包里做，避免捕获脚本对象导致 configuration cache 无法序列化。
    //
    // ⚠️ 但必须让任务"永不 up-to-date"：切换 targetMcVersion 时，若 Gradle 认为任务已
    // 是最新而跳过执行，就会沿用上一族的预处理产物（另一族的成员还在里面），
    // 引发一堆莫名其妙的"包不存在 / 找不到符号"假错误。
    // outputs.dir 的清理写在 main() 里，而 main() 只在任务真正执行时才跑。
    outputs.upToDateWhen { false }

    args(
        preprocessSrcDir.absolutePath,
        preprocessOutDir.absolutePath,
        targetMcVersion,
        if (preprocessRenames.exists()) preprocessRenames.absolutePath else "",
    )
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf(preprocessedDir))
        }
    }
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(preprocessImpSources)
}

neoForge {
    version = effectiveNeoVersion
}

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = familyJavaRelease
    options.compilerArgs.addAll(listOf(
        "-AimpTargetMcVersion=$targetMcVersion",
        "-AtargetMcVersion=$targetMcVersion",
    ))
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Implementation-Title" to "Wild Wind imp",
            "Imp-Target-MC-Version" to targetMcVersion,
            "Imp-Version-Family" to versionFamily,
            "Imp-Neo-Version" to effectiveNeoVersion,
        )
    }
}

tasks.register("printImpVariant") {
    group = "help"
    description = "打印当前 imp 构建的族 / MC 版本 / NeoForge 版本 / Java 级别"
    val mc = targetMcVersion
    val fam = versionFamily
    val neo = effectiveNeoVersion
    val rel = familyJavaRelease
    doLast {
        logger.lifecycle("imp: targetMcVersion=$mc family=$fam neo=$neo javaRelease=$rel")
    }
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}
