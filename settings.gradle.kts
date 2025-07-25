pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://maven.neoforged.net/releases")
//        maven("https://maven.creeperhost.net/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.9.0")
}
val projectNames = listOf(
    "Deco",
    "Adventure",
    "Agricultural",
    "Vanilla Plus Plus",
    "All In All"
    )
projectNames.forEach {
    include(it)
    project(":$it").projectDir = file("modules/$it")
}
includeBuild("ap")
rootProject.name = "ww"
