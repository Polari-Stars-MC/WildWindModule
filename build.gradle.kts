import jdk.internal.net.http.common.TimeSource.source
import org.slf4j.event.Level.DEBUG
import java.util.Locale

plugins {
    `java-library`
    `maven-publish`
    id("net.neoforged.moddev") version("2.0.141")
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

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "idea")
    apply(plugin = "net.neoforged.moddev")
    apply(plugin = "base")
    val modGroupId: String by project
    val modId: String by project
    var projectModId = modId + "_" + project.name.lowercase(Locale.ROOT)
    val modVersion: String by project
    val neoVersion: String by project
    if (project == rootProject) return@allprojects
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
            "mod_name" to modName,
            "mod_license" to modLicense,
            "mod_version" to modVersion,
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
                srcDir(rootProject.file("build/generated/${project.name}"))
                srcDir(rootProject.file("src/${project.name}/resources"))

                exclude("**/*.bbmodel")
                exclude("build/generated/**/.cache")
            }
        }
    }

    repositories {

    }

    java.toolchain.languageVersion = JavaLanguageVersion.of(25)

    neoForge {
        version = neoVersion

        // accessTransformers = project.files('src/main/resources/META-INF/accesstransformer.cfg')

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
            register("data") {
                clientData()
                gameDirectory = layout.buildDirectory.dir("runs/${project.name}/datagen").get().asFile
                programArguments.addAll(listOf(
                    "--mod", projectModId,
                    "--all",
                    "--output", rootProject.file("build/generated/${project.name}").absolutePath,
                    "--existing", rootProject.file("src/${project.name}/resources").absolutePath,
                ))
            }
            configureEach {
                systemProperty("forge.logging.markers", "REGISTRIES")
                logLevel.set(DEBUG)
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
}
