import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.CommandLineArgumentProvider

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
 * 版本族 → 默认 (minecraftVersion, neoForgeVersion)。可用 -PimpNeoVersion 覆盖。
 *
 * 选定依据（已实测）：
 * - 1.20 族锚定 1.20.4 / NF 20.4.253：20.2.x 未发布 moddev 所需的 neoforge-moddev-bundle；
 * - 1.21 族锚定 1.21.1 / NF 21.1.209：生态最广；
 * - 26.x 族锚定 26.2 / NF 26.2.0.75：当前主力。
 */
val familyDefaults: Map<String, Pair<String, String>> = mapOf(
    "1.20" to ("1.20.4" to "20.4.253"),
    "1.21" to ("1.21.1" to "21.1.209"),
    "26.x" to ("26.2" to "26.2.0.75"),
)

val versionFamily: String = if (targetMcVersion.startsWith("1.")) {
    val parts = targetMcVersion.split(".")
    parts[0] + "." + parts[1]
} else {
    targetMcVersion.substringBefore('.') + ".x"
}

val familyDefault: Pair<String, String> = familyDefaults[versionFamily]
    ?: error(
        "未知的版本族 '$versionFamily'（来自 targetMcVersion=$targetMcVersion）。" +
            "已支持：" + familyDefaults.keys.joinToString(" / ")
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

val familyJavaRelease: Int = javaReleaseFor(familyDefault.first)
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

val preprocessImpSources = tasks.register<JavaExec>("preprocessImpSources") {
    group = "build"
    description = "按 targetMcVersion 裁剪 imp 源码并应用 @Name 改名"
    classpath = preprocessorClasspath
    mainClass = "org.polaris2023.wildwind.imp.preprocess.SourcePreprocessor"

    inputs.dir(rawSourceDir).withPathSensitivity(PathSensitivity.RELATIVE)
    inputs.file(renamesFile).optional()
    inputs.property("targetMcVersion", targetMcVersion)
    outputs.dir(preprocessedDir)

    doFirst { preprocessedDir.get().asFile.deleteRecursively() }

    argumentProviders.add(CommandLineArgumentProvider {
        listOf(
            rawSourceDir.asFile.absolutePath,
            preprocessedDir.get().asFile.absolutePath,
            targetMcVersion,
            if (renamesFile.exists()) renamesFile.absolutePath else "",
        )
    })
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
