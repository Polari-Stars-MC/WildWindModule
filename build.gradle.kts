import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.SourceSetContainer
import org.slf4j.event.Level.INFO
import java.util.Locale
import java.util.Properties

plugins {
    `java-library`
    `maven-publish`
    id("net.neoforged.moddev") version ("2.0.141")
    idea
    base
}


//tasks.named<Wrapper>("wrapper").configure {
//    distributionType = Wrapper.DistributionType.BIN
//}

val minecraftVersion: String by rootProject
val minecraftVersionRange: String by rootProject
val modName: String by rootProject
val modLicense: String by rootProject

// 非 mod 的纯 Java 项目（ADR-0003）：imp-annotations / imp-processor 是基础设施，imp 是中间层
// 本体——都不参与 moddev / NeoForge 构建配置，依赖自行声明
val impInfraProjects = setOf("imp-annotations", "imp-processor", "imp")

/** 当前构建的目标 MC 版本与版本族（与 imp/build.gradle.kts 的推导保持一致）。 */
val boundaryTargetVersion: String =
    (findProperty("targetMcVersion") as String?) ?: minecraftVersion
val boundaryFamily: String = if (boundaryTargetVersion.startsWith("1.")) {
    boundaryTargetVersion.split(".").take(2).joinToString(".")
} else {
    boundaryTargetVersion.substringBefore('.') + ".x"
}

/**
 * 版本族唯一事实源（gradle/version-families.properties）：族 → (mc, neoForge, java)。
 * 根脚本、imp 脚本与 CI 矩阵都从这里取，避免三处各写一份表。
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

/**
 * Java 语言级别由**实际目标版本**决定（不是族锚点）：
 * < 1.20.5 → 17 ；1.20.5 ~ 26 之前 → 21 ；>= 26 → 25。
 */
fun javaReleaseForVersion(mcVersion: String): Int {
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

/** 当前构建的 Java 级别（供 mod 模块的 options.release 使用）。 */
val targetJavaRelease: Int = javaReleaseForVersion(boundaryTargetVersion)

/**
 * imp 边界守卫（ADR-0003 阶段 A）：
 * 构建某版本族时，禁止内容模块出现该族不存在的版本分叉 API。
 * 规则文件：gradle/imp-boundary.txt（格式 族列表|正则|说明）。
 */
val checkImpBoundary = tasks.register("checkImpBoundary") {
    group = "verification"
    description = "校验内容模块未越过 imp 边界（当前族 $boundaryFamily）"

    val ruleFile = rootProject.file("gradle/imp-boundary.txt")
    val scanDirs = (
        listOf("src/core/java", "src/wilderness/java", "src/barren/java")
            .map { rootProject.file(it) } +
        // datagen 已独立成 source set 并按族分目录：只扫描当前族的 datagen
        listOf("core", "wilderness", "barren")
            .map { rootProject.file("src/$it/datagen/$boundaryFamily/java") }
        ).filter { it.isDirectory }
    val family = boundaryFamily

    inputs.file(ruleFile)
    inputs.files(scanDirs.filter { it.isDirectory })
    inputs.property("family", family)

    // 校验逻辑写在 doLast 闭包里，会捕获 Gradle 脚本对象，configuration cache 无法序列化。
    // 用官方 API 显式声明不兼容：Gradle 会为该次构建禁用 CC，而不是直接构建失败。
    // （改成 buildSrc 里的独立 Task 类才能完全兼容 CC，但那会拖慢每次配置，收益不成比例。）
    notCompatibleWithConfigurationCache("边界校验通过 doLast 闭包实现，捕获了脚本对象")

    doLast {
        if (!ruleFile.exists()) {
            error("缺少边界规则文件：${ruleFile.absolutePath}")
        }
        val violations = mutableListOf<String>()

        ruleFile.readLines().forEachIndexed { index, rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("#")) return@forEachIndexed

            val parts = line.split("=>").map { it.trim() }
            if (parts.size != 3) {
                error("边界规则第 ${index + 1} 行格式错误（应为 族列表 => 正则 => 说明）：$rawLine")
            }
            val (families, pattern, reason) = parts
            val applies = families == "*" ||
                families.split(',').map { it.trim() }.contains(family)
            if (!applies) return@forEachIndexed

            val regex = Regex(pattern)
            scanDirs.filter { it.isDirectory }.forEach { dir ->
                dir.walkTopDown()
                    .filter { it.isFile && it.extension == "java" }
                    .forEach { file ->
                        file.readLines().forEachIndexed { lineNo, text ->
                            if (regex.containsMatchIn(text)) {
                                val rel = file.relativeTo(rootProject.projectDir).path.replace('\\', '/')
                                violations += "  $rel:${lineNo + 1}  [$reason]\n      ${text.trim()}"
                            }
                        }
                    }
            }
        }

        if (violations.isNotEmpty()) {
            throw GradleException(
                "imp 边界被越过（目标族 $family，规则见 gradle/imp-boundary.txt）：\n" +
                    violations.joinToString("\n") +
                    "\n提示：这类版本分叉 API 应通过 imp 的版本无关接口访问。"
            )
        }
        logger.lifecycle("imp 边界检查通过（目标族 $family）")
    }
}

/**
 * 版本配对守卫（阶段 D）：
 * 防止出现"产物标着 1.21、实际按 26.2 编译"的假产物——
 * targetMcVersion / minecraftVersion / neoVersion 三者必须属于同一个版本族。
 * 确有需要用 -PallowVersionMismatch=true 放行（会打印警告）。
 */
val checkVersionPairing = tasks.register("checkVersionPairing") {
    group = "verification"
    description = "校验 targetMcVersion / minecraftVersion / neoVersion 成对一致"

    val family = boundaryFamily
    val target = boundaryTargetVersion
    val effectiveMc = minecraftVersion
    val effectiveNeo = (findProperty("neoVersion") as String?) ?: ""
    val expected = versionFamilies[family]
    val allowMismatch = (findProperty("allowVersionMismatch") as String?) == "true"

    inputs.property("family", family)
    inputs.property("targetMcVersion", target)
    inputs.property("minecraftVersion", effectiveMc)
    inputs.property("neoVersion", effectiveNeo)

    doLast {
        val exp = expected ?: throw GradleException(
            "未知版本族 '$family'（targetMcVersion=$target）。" +
                "已支持：" + versionFamilies.keys.joinToString(" / ")
        )
        val problems = mutableListOf<String>()
        if (effectiveMc != exp.first) {
            problems += "minecraftVersion=$effectiveMc ≠ 族 '$family' 期望 ${exp.first}"
        }
        if (effectiveNeo.isNotEmpty() && effectiveNeo != exp.second) {
            problems += "neoVersion=$effectiveNeo ≠ 族 '$family' 期望 ${exp.second}"
        }
        if (problems.isNotEmpty()) {
            val message = "版本配对不一致（targetMcVersion=$target，族 $family）：\n" +
                problems.joinToString("\n") { "  - $it" } +
                "\n请同时传 -PminecraftVersion 与 -PneoVersion，或去掉 targetMcVersion 覆盖。"
            if (allowMismatch) {
                logger.warn("WARNING: $message")
            } else {
                throw GradleException(message)
            }
        }
        logger.lifecycle(
            "版本配对通过：族=$family target=$target MC=${exp.first} NeoForge=${exp.second} Java=${exp.third}"
        )
    }

    // 同上：doLast 闭包捕获脚本对象，显式声明与 configuration cache 不兼容
    notCompatibleWithConfigurationCache("版本配对校验通过 doLast 闭包实现，捕获了脚本对象")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "idea")
    apply(plugin = "base")
    if (project.name in impInfraProjects) return@allprojects
    apply(plugin = "net.neoforged.moddev")
    val modGroupId: String by project
    val modId: String by project
    var projectModId = rootProject.name + "_" + project.name.lowercase(Locale.ROOT)
    val modVersion: String by project
    val neoVersion: String by project
    if (project == rootProject) return@allprojects
    // 原野之风三份内容的显示名
    val moduleDisplayName: String = mapOf(
        "core" to "Wild Wind: Core",
        "wilderness" to "Wild Wind: Wilderness Update",
        "barren" to "Wild Wind: Barren Update",
    )[project.name] ?: modName
    // 非核心模块必须依赖核心模块
    val coreDependency: String = if (project.name == "core") "" else """
[[dependencies.${projectModId}]]
    modId="${rootProject.name}_core"
    type="required"
    versionRange="[${modVersion},)"
    ordering="AFTER"
    side="BOTH"
""".trimIndent()
    group = modGroupId
    version = modVersion
    // 产物名带目标 MC 版本（ADR-0003 §4）：CI 矩阵各版本产物可直接区分
    val artifactMcVersion: String = (findProperty("targetMcVersion") as String?) ?: minecraftVersion
    configure<BasePluginExtension> {
        archivesName = "${projectModId}-mc${artifactMcVersion}"

    }

    var generateModMetadata = tasks.register<ProcessResources>("generatedMetadata") {
        val replaceProperties = mapOf(
            "minecraft_version" to minecraftVersion,
            "minecraft_version_range" to minecraftVersionRange,
            "neo_version" to neoVersion,
            "mod_id" to projectModId,
            "mod_name" to moduleDisplayName,
            "mod_license" to modLicense,
            "mod_version" to modVersion,
            "core_dependency" to coreDependency
        )
        inputs.properties(replaceProperties)
        expand(replaceProperties)
        from(rootProject.file("src/templates"))
        into("build/generated/sources/modMetadata")
    }


    sourceSets {
        rootProject.file("src/${project.name}/java").resolve(modGroupId.replace('.', '/')).mkdirs()
        main {
            java {
                srcDir(rootProject.file("src/${project.name}/java"))
                // imp 的预处理输出只并入 **core**：若每个模块都编一份，两个模块会各自包含
                // 同一个包 org.polaris2023.wildwind.imp，JPMS 直接拒绝
                // （Module ww_wilderness contains package ..., module ww_core exports package ...）。
                // 其余模块通过 implementation(project(":core")) 在编译期与运行期获得 imp。
                if (project.name == "core") {
                    srcDir(rootProject.file("imp/build/preprocessed/java"))
                    // sample 演示的是 ImpManifest（需要 APT），模块侧不需要它
                    exclude("**/sample/**")
                }
            }
            resources {
                srcDir(generateModMetadata)
                srcDir(rootProject.file("build/generated/${project.name}/server"))
                srcDir(rootProject.file("build/generated/${project.name}/client"))
                srcDir(rootProject.file("src/${project.name}/resources"))

                exclude("**/*.bbmodel")
                // datagen 的 hash 缓存目录，位于 client/server 两个 srcDir 根下，
                // 同名 entry 会在 processResources 里触发 duplicate 错误，必须排除
                exclude("**/.cache/**")
                exclude(".cache/**")
            }
        }
    }

    // ── datagen 独立 source set（阶段 E）──────────────────────────────────
    // datagen 的 Provider 基类/包在版本之间完全不同（实测：26.x 的
    // net.minecraft.client.data.models 在 1.20/1.21 整个不存在，旧版用
    // net.minecraft.data.models），因此 datagen 代码无法经 imp 收敛，只能按版本分目录。
    //
    // 拆成独立 source set 后，datagen 不再参与常规 build：旧版内容模块可以在
    // datagen 尚未移植的情况下先编译通过，datagen 成为"按需移植"的增量工作。
    val datagenDir = rootProject.file("src/${project.name}/datagen/$boundaryFamily/java")
    val datagenSourceSet = extensions.getByType<SourceSetContainer>().create("datagen") {
        java.srcDir(datagenDir)
        resources.srcDir(rootProject.file("src/${project.name}/datagen/$boundaryFamily/resources"))
    }
    configurations.named(datagenSourceSet.implementationConfigurationName) {
        extendsFrom(configurations.getByName("implementation"))
    }
    configurations.named(datagenSourceSet.compileOnlyConfigurationName) {
        extendsFrom(configurations.getByName("compileOnly"))
    }
    configurations.named(datagenSourceSet.annotationProcessorConfigurationName) {
        extendsFrom(configurations.getByName("annotationProcessor"))
    }
    // moddev 提供的 Minecraft/NeoForge 依赖既不在 implementation 也不在 compileOnly 里。
    // 这里以 **compileOnly 依赖**的形式继承 main 的编译类路径：
    // 直接 `compileClasspath +=` 会把 minecraft-patched-*.jar 一并带进运行期，
    // 导致 MC 类同时出现在 app 与 TRANSFORMER 两个加载器
    // （LinkageError: loader constraint violation: net.minecraft.resources.Identifier）。
    dependencies.add(
        datagenSourceSet.compileOnlyConfigurationName,
        files(sourceSets["main"].compileClasspath)
    )
    datagenSourceSet.compileClasspath += sourceSets["main"].output
    datagenSourceSet.runtimeClasspath += sourceSets["main"].output

    // 荒野 / 瘠土的 datagen 复用 core 的 Provider 工具（MissingModels、BlockLoot 等）。
    // 跨项目取 source set 必须等所有项目配置完成（allprojects 迭代顺序不保证 core 在前）。
    if (project.name != "core") {
        gradle.projectsEvaluated {
            val coreDatagen = rootProject.project(":core")
                .extensions.getByType<SourceSetContainer>()["datagen"]
            datagenSourceSet.compileClasspath += coreDatagen.output
            datagenSourceSet.runtimeClasspath += coreDatagen.output
            tasks.named(datagenSourceSet.compileJavaTaskName) {
                dependsOn(":core:${coreDatagen.compileJavaTaskName}")
            }
        }
    }

    // 这里原本声明了 GeckoLib 的 cloudsmith 仓库（exclusiveContent，按族放行
    // com.geckolib / software.bernie.geckolib）。ADR-0006 把实体渲染迁到原版管线后
    // 已无第三方 maven 需求，仓库块整块删除；依赖只剩 moddev 提供的 MC/NeoForge
    // 与 lombok（在下面的 dependencies 块里）。

    java.toolchain.languageVersion = JavaLanguageVersion.of(25)

    // Gradle 9 对资源重复项默认报错；保持既有语义：先生成的 srcDir（build/generated）优先
    tasks.withType<ProcessResources>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    neoForge {
        version = neoVersion

        accessTransformers.from(rootProject.file("src/${project.name}/resources/META-INF/accesstransformer.cfg"))

        runs {
            register("client") {
                client()
                systemProperty("neoforge.enabledGameTestNamespaces", projectModId)
                gameDirectory = layout.buildDirectory.dir("runs/${project.name}/client").get().asFile
            }
            register("server") {
                server()
                programArgument("--nogui")
                systemProperty("neoforge.enabledGameTestNamespaces", projectModId)
                gameDirectory = layout.buildDirectory.dir("runs/${project.name}/server").get().asFile
            }
            register("clientData") {
                clientData()
                gameDirectory = layout.buildDirectory.dir("runs/${project.name}/datagen").get().asFile
                programArguments.addAll(
                    listOf(
                        "--mod", projectModId,
                        "--all",
                        "--output", rootProject.file("build/generated/${project.name}/client").absolutePath,
                        "--existing", rootProject.file("src/${project.name}/resources").absolutePath,
                    )
                )
            }
            register("serverData") {
                serverData()
                gameDirectory = layout.buildDirectory.dir("runs/${project.name}/datagen").get().asFile
                programArguments.addAll(
                    listOf(
                        "--mod", projectModId,
                        "--all",
                        "--output", rootProject.file("build/generated/${project.name}/server").absolutePath,
                        "--existing", rootProject.file("src/${project.name}/resources").absolutePath,
                    )
                )
            }
            configureEach {
                systemProperty("forge.logging.markers", "REGISTRIES")
                logLevel.set(INFO)
            }
        }

        mods {
            register(projectModId) {
                sourceSet(sourceSets["main"])
                // datagen 提供器只在数据运行（runClientData / runServerData）时需要，
                // 但必须挂进 mod 才能被 GatherDataEvent 发现
                sourceSet(datagenSourceSet)
            }
        }


    }

    neoForge.ideSyncTask(generateModMetadata)

    val localRuntime by configurations.registering

    configurations {
        runtimeClasspath {
            extendsFrom(localRuntime.get())
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.44")
        annotationProcessor("org.projectlombok:lombok:1.18.44")
        // 版本中间层（ADR-0003）：只给编译期。imp 的类会作为本模块源码的一部分编译进
        // 自己的输出（见下方 main.java.srcDir），**不能**作为运行期库出现在 app 类路径上：
        // FML 只接管 MC/NeoForge 包，其它包委托父加载器，imp 类会因此被 app 加载，
        // 而它签名字里含 MC 类型 → LinkageError: loader constraint violation
        compileOnly(project(":imp-annotations"))
        // 荒野 / 瘠土更新都建立在核心之上
        if (project.name != "core") {
            implementation(project(":core"))
        }
        // 开发期联调：瘠土更新的 run 额外加载荒野更新；只在本地运行时生效，不进入发布 jar
        if (project.name == "barren") {
            add("localRuntime", project(":wilderness"))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        // 字节码级别跟随目标 MC 版本（1.20.x→17/21、1.21→21、26.x→25），
        // 否则会给 1.20 族产出 Java 25 字节码，运行期直接 UnsupportedClassVersionError
        options.release = targetJavaRelease
    }

    // 边界守卫与版本配对先于 check 执行；datagen 也必须能编译（该版本族有 datagen 时）
    tasks.named("check") {
        dependsOn(checkImpBoundary, checkVersionPairing, datagenSourceSet.compileJavaTaskName)
    }

    // imp 不是独立 mod（无 @Mod 主类、无 mods.toml）。它的类以**源码目录**形式并入 core 的
    // main source set（见上方 srcDir），因此落在 core 的输出里、由 mod 加载器加载；
    // 其余模块通过 project(":core") 获得，避免同一包出现在多个模块里。
    if (project.name == "core") {
        tasks.named<JavaCompile>("compileJava") {
            dependsOn(":imp:preprocessImpSources")
        }
    }

    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}
