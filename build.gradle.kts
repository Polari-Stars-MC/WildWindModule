import org.slf4j.event.Level.INFO

plugins {
    `java-library`
    id("net.neoforged.moddev") version("2.0.148")
    base
}
val minecraftVersion: String by project
val minecraftVersionRange: String by project
val neoVersion: String by project
val modVersion: String by project
val modLicense: String by project
val modGroupId: String by project
val geocklibVersion: String by project
val releaseModules = listOf("core", "wilderness", "barren")
val smokeModules = providers.gradleProperty("testModules").orElse("core").get().split(",")
require(smokeModules.contains("core") && smokeModules.all { it in releaseModules }) {
    "testModules must include core and may include wilderness and/or barren"
}
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "net.neoforged.moddev")
    group = modGroupId
    version = modVersion
    val module = name
    val modId = if (module == "verification") "wild_wind_tests" else "wild_wind_$module"
    val moduleDependencies = when (module) {
        "core" -> emptyList()
        "wilderness", "barren" -> listOf("core")
        "smoke" -> smokeModules
        else -> releaseModules
    }
    val usesGecko = module in listOf("wilderness", "datagen", "verification") || (module == "smoke" && "wilderness" in smokeModules)
    base { archivesName = "$modId-mc$minecraftVersion" }
    java.toolchain.languageVersion = JavaLanguageVersion.of(25)
    repositories {
        mavenCentral()
        exclusiveContent {
            forRepository { maven { url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") } }
            filter { includeGroupAndSubgroups("com.geckolib") }
        }
    }
    dependencies {
        moduleDependencies.forEach { implementation(project(":$it")) }
        compileOnly("org.projectlombok:lombok:1.18.44")
        annotationProcessor("org.projectlombok:lombok:1.18.44")
        if (usesGecko) implementation("com.geckolib:geckolib-neoforge-$minecraftVersion:$geocklibVersion")
        testImplementation(platform("org.junit:junit-bom:5.13.4"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
    val metadata = tasks.register<ProcessResources>("generatedMetadata") {
        val titles = mapOf("core" to "Wild Wind:Core", "wilderness" to "Wild Wind:Wilderness Update", "barren" to "Wild Wind:Barren Update", "datagen" to "Wild Wind Data Generators", "verification" to "Wild Wind Integration Tests", "smoke" to "Wild Wind Installation Tests")
        val extraDependencies = buildString {
            moduleDependencies.forEach { dependency ->
                append("""
                    [[dependencies.$modId]]
                    modId="wild_wind_$dependency"
                    type="required"
                    versionRange="[$modVersion]"
                    ordering="AFTER"
                    side="BOTH"
                """.trimIndent())
                append('\n')
            }
            if (usesGecko) append("""
                [[dependencies.$modId]]
                modId="geckolib"
                type="required"
                versionRange="[$geocklibVersion,)"
                ordering="NONE"
                side="BOTH"
            """.trimIndent())
        }
        val mixins = when {
            module in releaseModules -> "[[mixins]]\nconfig=\"$modId.mixins.json\""
            module == "verification" -> "[[mixins]]\nconfig=\"wild_wind_tests.mixins.json\""
            module == "smoke" -> ""
            else -> ""
        }
        val properties = mapOf("mod_id" to modId, "mod_name" to titles.getValue(module), "mod_version" to modVersion, "mod_license" to modLicense, "neo_version" to neoVersion, "minecraft_version_range" to minecraftVersionRange, "module_dependencies" to extraDependencies, "module_mixins" to mixins)
        inputs.properties(properties)
        expand(properties)
        from(rootProject.file("src/templates"))
        into(layout.buildDirectory.dir("generated/sources/modMetadata"))
    }
    sourceSets {
        main {
            java.setSrcDirs(listOf(rootProject.file("src/$module/java")))
            resources.setSrcDirs(listOf(rootProject.file("src/$module/resources")))
            resources.srcDir(metadata)
            resources.exclude("**/*.bbmodel", "**/.cache/**")
        }
        test { java.srcDir(rootProject.file("src/$module/test")) }
    }
    tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }
    tasks.test {
        useJUnitPlatform()
        val directory = layout.buildDirectory.dir("test-runtime")
        workingDir(directory)
        doFirst { directory.get().asFile.mkdirs() }
    }
    neoForge {
        version = neoVersion
        if (usesGecko) accessTransformers.from(rootProject.file("src/wilderness/resources/META-INF/accesstransformer.cfg"))
        addModdingDependenciesTo(sourceSets.test.get())
        mods { register(modId) { sourceSet(sourceSets.main.get()) } }
        runs {
            register("client") { client() }
            register("server") { server(); programArgument("--nogui") }
            if (module == "datagen") {
                register("clientData") {
                    clientData()
                    programArguments.addAll("--mod", modId, "--all", "--output", rootProject.file("build/generated/datagen/client").absolutePath)
                }
                register("serverData") {
                    serverData()
                    programArguments.addAll("--mod", modId, "--all", "--output", rootProject.file("build/generated/datagen/server").absolutePath)
                }
            }
            if (module in listOf("verification", "smoke")) {
                register("gameTest") {
                    type = "gameTestServer"
                    programArguments.addAll("--tests", "$modId:*", "--report", "results.xml")
                    if (module == "smoke") systemProperty("wildwind.testModules", smokeModules.joinToString(","))
                }
            }
            configureEach {
                if (module == "smoke" && providers.gradleProperty("clientSmoke").orElse("false").get() == "true") {
                    systemProperty("wildwind.clientSmoke", "true")
                }
                val variant = if (module == "smoke") "/${smokeModules.joinToString("-")}" else ""
                gameDirectory = layout.buildDirectory.dir("runs/$name$variant")
                logLevel.set(INFO)
            }
        }
    }
    if (module == "verification") {
        val prepare by tasks.registering(Sync::class) {
            from(rootProject.file("src/verification/config"))
            from(rootProject.file("src/core/resources/examples/example_ruins.nbt")) { into("ww_structures/templates") }
            into(layout.buildDirectory.dir("runs/gameTest/config"))
        }
        neoForge.runs.named("gameTest") { taskBefore(prepare) }
    }
}
tasks.register<Sync>("collectMods") {
    description = "Collect only the three distributable mod JARs (no development tools)."
    releaseModules.forEach { module -> from(project(":$module").tasks.named("jar")) }
    into(layout.buildDirectory.dir("distributions/mods"))
}
