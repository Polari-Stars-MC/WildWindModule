pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
}

// 0 = core（原野之风：核心）
// 1 = wilderness（原野之风：荒野更新）
// 2 = barren（原野之风：瘠土更新）
// imp-annotations / imp-processor = imp 中间层基础设施（ADR-0003）：
//   源码在根目录同名文件夹（标准布局），不重定向到 modules/，纯 Java，不参与 NeoForge 配置
val modProjects = listOf("core", "wilderness", "barren")
val impInfraProjects = listOf("imp-annotations", "imp-processor")
// imp 中间层本体：走 NeoForge 依赖但不是 mod（无 @Mod 主类），不重定向 projectDir
val impProjects = listOf("imp")

modProjects.forEach {
    include(it)
    val f = file("modules/$it")
    f.mkdirs()
    project(":$it").projectDir = f
}

impInfraProjects.forEach { include(it) }
impProjects.forEach { include(it) }


rootProject.name = "ww"
