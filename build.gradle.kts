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

// `gradlew runClient` 的默认模块：Gradle 只按「任务名」匹配，根目录执行时会同时命中
// core / wilderness / barren 三个 runClient。这里让非默认模块的同名任务在没人用
// `:模块:runClient` 显式点名时跳过，于是 `gradlew runClient` == `gradlew :barren:runClient`
// （barren 的 run 会自动额外加载 wilderness 做联调）。
val defaultClientModule: String = "barren"

// 调用方显式写出的任务（去掉前导冒号），用于判断是否点名了某个模块
val requestedTaskPaths: Set<String> = gradle.startParameter.taskNames
    .map { it.trim().removePrefix(":") }
    .toSet()

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "idea")
    apply(plugin = "net.neoforged.moddev")
    apply(plugin = "base")
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
    configure<BasePluginExtension> {
        archivesName = projectModId

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
            // debug_all.bat 使用：起一个无界面服务端，导出全部方块 / 物品 / 生物属性后自动退出
            register("debugExport") {
                server()
                programArgument("--nogui")
                gameDirectory = layout.buildDirectory.dir("runs/${project.name}/debug").get().asFile
                systemProperty(
                    "wildwind.debug.export.dir",
                    rootProject.file(providers.gradleProperty("wildwindDebugOutput").getOrElse("build/debug")).absolutePath
                )
                systemProperty("wildwind.debug.export.exit", "true")
                systemProperty("wildwind.debug.export.namespaces", "ww_core,ww_wilderness,ww_barren")
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

    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }

    // ---- 默认运行入口：`gradlew runClient` 只启动 barren ----
    if (project.name != defaultClientModule && requestedTaskPaths.none { it.startsWith("${project.name}:") }) {
        tasks.matching { it.name == "runClient" }.configureEach {
            onlyIf("未用项目路径点名模块，默认只启动 :$defaultClientModule:runClient") { false }
        }
    }

    // ---- build 前自动跑 datagen ----
    // datagen 产物写在 build/generated/<module>/{client,server}，不在版本库里；
    // 直接 `gradlew build` 会打出缺模型 / 语言 / 战利品表的 jar，所以让每个模块的 build 先跑自己的 datagen。
    // 不能把 datagen 挂到 processResources 或 jar 的 dependsOn 上：
    // data run 依赖 classes（进而 processResources），而 `:barren:runClient` 又依赖 `:core:jar`，会形成依赖环。
    // 因此 jar 只用 mustRunAfter 排序，并直接从 generated 目录补文件（同一轮构建里 processResources
    // 的输入快照早于 datagen 产出，光靠排顺序仍然会漏资源）。
    val generatedResourceDirs = listOf(
        rootProject.file("build/generated/${project.name}/client"),
        rootProject.file("build/generated/${project.name}/server"),
    )
    val datagenRuns = tasks.matching { it.name == "runClientData" || it.name == "runServerData" }

    tasks.named("build") {
        dependsOn(datagenRuns)
    }

    tasks.named<Jar>("jar") {
        mustRunAfter(datagenRuns)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        generatedResourceDirs.forEach { dir ->
            from(dir) {
                exclude("**/*.bbmodel")
                exclude("**/.cache/**")
                exclude(".cache/**")
            }
        }
    }
}

// datagen 产物写在 build/generated/<module>/{client,server}，由上面 main.resources 的 srcDir 打进 jar。
// 这些目录不在版本库里，所以每个模块的 `build` 都已 dependsOn 自己的 datagen（见上），`gradlew build` 一条命令即可；
// 这里的 `genData` 保留为聚合任务，一次跑完全部模块的 client + server datagen。
// 注意不能把 datagen 挂到 processResources / jar 的 dependsOn 上（runClientData 依赖 classes，而
// `:barren:runClient` 又依赖 `:core:jar`，会形成循环依赖）。
tasks.register("genData") {
    group = "build"
    description = "为每个模块运行 datagen，生成 build/generated/<module> 下的资源"
    subprojects.forEach { sub ->
        dependsOn(sub.tasks.matching { it.name == "runClientData" || it.name == "runServerData" })
    }
}
