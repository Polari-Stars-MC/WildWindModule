pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
}

val let = listOf("func", "deco", "adv", "vpp", "hfas")
let.forEach {
    include(it)

    var f = file("modules/$it")
    f.mkdirs()
    project(":$it").projectDir = f
}

