// Structure generation is an internal feature of HFAS and shares its mod entry point.
sourceSets.main {
    java.srcDir(rootProject.file("src/structures/java"))
    resources.srcDir(rootProject.file("src/structures/resources"))
}

// Generated files take precedence over checked-in fallbacks for builds without datagen.
tasks.processResources {
    filesMatching(listOf("assets/**/*.json", "data/**/*.json")) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    // Keep filtering unfinished boats, non-azalea signs and potted saplings.
    // Keep the source assets, but do not load recipes/loot/advancements for absent content.
    listOf("azalea", "blaze", "soul").forEach { wood ->
        val unfinished = if (wood == "azalea") listOf("boat", "chest_boat")
            else listOf("boat", "chest_boat", "sign", "hanging_sign", "wall_sign", "wall_hanging_sign")
        unfinished.forEach { kind ->
            exclude("data/ww_hfas/recipe/${wood}_${kind}.json")
            exclude("data/ww_hfas/loot_table/blocks/${wood}_${kind}.json")
            exclude("data/ww_hfas/advancement/recipes/**/${wood}_${kind}.json")
            exclude("assets/ww_hfas/blockstates/${wood}_${kind}.json")
        }
        exclude("data/ww_hfas/loot_table/blocks/potted_${wood}_sapling.json")
        exclude("assets/ww_hfas/blockstates/potted_${wood}_sapling.json")
    }
}

sourceSets.test {
    java.srcDir(rootProject.file("src/hfas/test"))
    java.srcDir(rootProject.file("src/structures/test"))
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
val gameTest by sourceSets.creating {
    java.srcDir(rootProject.file("src/hfas/gametest/java"))
    java.srcDir(rootProject.file("src/structures/gametest/java"))
    resources.srcDir(rootProject.file("src/hfas/gametest/resources"))
    resources.srcDir(rootProject.file("src/structures/gametest/resources"))
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}
configurations[gameTest.implementationConfigurationName].extendsFrom(configurations.implementation.get())
configurations[gameTest.runtimeOnlyConfigurationName].extendsFrom(configurations.runtimeOnly.get())
val prepareGameTest by tasks.registering(Sync::class) {
    from(rootProject.file("src/hfas/gametest/config"))
    from(rootProject.file("src/structures/gametest/config"))
    from(rootProject.file("src/structures/resources/examples/example_ruins.nbt")) {
        into("ww_structures/templates")
    }
    into(layout.buildDirectory.dir("runs/hfas/gameTest/config"))
}
neoForge {
    addModdingDependenciesTo(sourceSets.test.get())
    addModdingDependenciesTo(gameTest)
    val mainMod = mods.getByName("ww_hfas")
    val testMod = mods.create("ww_hfas_tests") { sourceSet(gameTest) }
    runs.configureEach { loadedMods.set(setOf(mainMod)) }
    runs.register("gameTest") {
        type = "gameTestServer"
        sourceSet = gameTest
        loadedMods.set(setOf(mainMod, testMod))
        gameDirectory = layout.buildDirectory.dir("runs/hfas/gameTest")
        programArguments.addAll("--tests", "ww_hfas_tests:*", "--report", "results.xml")
        taskBefore(prepareGameTest)
    }
}
