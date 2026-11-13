import org.gradle.api.file.DuplicatesStrategy
import org.slf4j.event.Level.INFO
import java.util.Locale

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

val geocklibVersion: String by rootProject

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
 * imp 边界守卫（ADR-0003 阶段 A）：
 * 构建某版本族时，禁止内容模块出现该族不存在的版本分叉 API。
 * 规则文件：gradle/imp-boundary.txt（格式 族列表|正则|说明）。
 */
val checkImpBoundary = tasks.register("checkImpBoundary") {
    group = "verification"
    description = "校验内容模块未越过 imp 边界（当前族 $boundaryFamily）"

    val ruleFile = rootProject.file("gradle/imp-boundary.txt")
    val scanDirs = listOf("src/core/java", "src/wilderness/java", "src/barren/java")
        .map { rootProject.file(it) }
    val family = boundaryFamily

    inputs.file(ruleFile)
    inputs.files(scanDirs.filter { it.isDirectory })
    inputs.property("family", family)

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
            "geocklib_version" to geocklibVersion,
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

    repositories {
        exclusiveContent {
            forRepository {
                maven {
                    name = "GeckoLib"
                    url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
                }
            }

            filter {
                includeGroupAndSubgroups("com.geckolib")
            }
        }
    }

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
        implementation("com.geckolib:geckolib-neoforge-26.2:5.5.4")
        // 版本中间层（ADR-0003）：所有 mod 模块经 imp 访问版本敏感能力
        api(project(":imp"))
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
    }

    // imp 边界守卫先于 check 执行：任何内容模块越过 imp 边界都直接失败
    tasks.named("check") {
        dependsOn(checkImpBoundary)
    }

    // imp 不是独立 mod（无 @Mod 主类、无 mods.toml），其类不进任何 jar；
    // 上层模块必须在产物里带上 imp 的字节码，否则发布后运行期 NoClassDefFoundError。
    // 各 mod jar 内容一致（同一 targetMcVersion 构建），重复无副作用。
    tasks.named<Jar>("jar") {
        from(rootProject.project(":imp").sourceSets["main"].output)
    }

    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}
