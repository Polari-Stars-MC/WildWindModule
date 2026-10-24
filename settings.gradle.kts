pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
}

val modules = listOf("core", "wilderness", "barren", "datagen", "verification", "smoke")
modules.forEach {
    include(it)

    val f = file("modules/$it")
    f.mkdirs()
    project(":$it").projectDir = f
}


rootProject.name = "wild_wind"
