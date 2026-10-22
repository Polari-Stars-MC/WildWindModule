sourceSets {
    test {
        java.srcDir(rootProject.file("src/structures/test"))
    }
}

repositories { mavenCentral() }

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    val runtimeDirectory = layout.buildDirectory.dir("test-runtime")
    workingDir(runtimeDirectory)
    doFirst { runtimeDirectory.get().asFile.mkdirs() }
}

neoForge {
    addModdingDependenciesTo(sourceSets.test.get())
}

val gameTest by sourceSets.creating {
    java.srcDir(rootProject.file("src/structures/gametest/java"))
    resources.srcDir(rootProject.file("src/structures/gametest/resources"))
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}

val prepareStructureGameTest by tasks.registering(Sync::class) {
    from(rootProject.file("src/structures/gametest/config"))
    from(rootProject.file("src/structures/resources/examples/example_ruins.nbt")) {
        into("ww_structures/templates")
    }
    into(layout.buildDirectory.dir("runs/structures/gameTest/config"))
}

neoForge {
    addModdingDependenciesTo(gameTest)
    val mainMod = mods.getByName("ww_structures")
    val testMod = mods.create("ww_structures_tests") { sourceSet(gameTest) }
    runs.configureEach { loadedMods.set(setOf(mainMod)) }
    runs.register("gameTest") {
        type = "gameTestServer"
        sourceSet = gameTest
        loadedMods.set(setOf(mainMod, testMod))
        gameDirectory = layout.buildDirectory.dir("runs/structures/gameTest")
        programArguments.addAll("--tests", "ww_structures_tests:*", "--report", "results.xml")
        taskBefore(prepareStructureGameTest)
    }
}
