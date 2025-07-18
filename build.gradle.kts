import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.polaris2023.mcmeta.extension.McMetaSettings
import org.polaris2023.mcmeta.extension.forge.ForgeLikeDependency
import org.polaris2023.mcmeta.extension.forge.ForgeLikeToml
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeDependency
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeMods
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeModsToml
import org.slf4j.event.Level

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("io.github.polari-stars-mc:mcmeta-plugin:0.0.4-fix.1")
    }
}


plugins {
    `java-library`
    `maven-publish`
    idea
    base
    alias(libs.plugins.mod.dev.gradle)
    id("io.github.jeadyx.sonatype-uploader").version("2.8")
    // id("dev.vfyjxf.modaccessor") version "1.1.1"
}
val mcVersion: String by rootProject
val mcVersionRange: String by rootProject
val modGroupId: String by rootProject
val modAuthors: String by rootProject
val modLicense: String by rootProject
val neoVersion: String by rootProject
val neoVersionRange: String by rootProject
val loaderVersionRange: String by rootProject
val parchmentMappingsVersion: String by rootProject
val parchmentMinecraftVersion: String by rootProject

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")
    apply(plugin = "idea")
    apply(plugin = "io.github.Polari-Stars-MC.mcmeta-plugin")
    apply(plugin = "io.github.jeadyx.sonatype-uploader")
    configure<McMetaSettings> {
        loaderType = McMetaSettings.Type.DEFAULT
    }
    repositories {
        mavenLocal()
        mavenCentral()
        flatDir {
            dirs(rootProject.file("libs"))
        }
        maven("https://maven.neoforged.net/releases")
        maven("https://mvn.devos.one/snapshots")
        maven("https://maven.ithundxr.dev/snapshots")
//        maven("https://maven.creeperhost.net/")
    }
    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")
    }
    configure<ForgeLikeToml> {
        loaderVersion = loaderVersionRange
        license = modLicense
        issueTrackerURL = uri("https://github.com/Polari-Stars-MC/WildWind/issues")
    }
    java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    idea {
        module {
            isDownloadSources = true
            isDownloadJavadoc = true
        }
    }


    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:-unchecked")
    }


}



val lib = libs

subprojects {
    apply(plugin = lib.plugins.mod.dev.gradle.get().pluginId)
    //apply(plugin = "dev.vfyjxf.modaccessor")
	
    val modId: String by project
    val modName: String by project
    val modVersion: String by project
    val modClassPrefix: String by project
    val generatedDir = file("build/generated/data/")
    val resourcesDir = rootProject.file("src/${modId}/resources")
    val javaDir = rootProject.file("src/${modId}/java/")
    resourcesDir.mkdirs()
    javaDir.mkdirs()
    val mixinsFile = resourcesDir.resolve("META-INF/$modId.mixins.json")
    val atFile = resourcesDir.resolve("META-INF/accesstransformer.cfg")
    val enumFile = resourcesDir.resolve("META-INF/enumextensions.json")
    val interfaceFile = file("interfaces.json")



    sourceSets {
        main {
            java {
                srcDir(javaDir)
            }
            resources {
                srcDir(generatedDir)
                srcDir(resourcesDir)
                srcDir(layout.buildDirectory.dir("generated/modMetaData"))
            }
        }
    }

    neoForge {
        version = neoVersion
        parchment {
            minecraftVersion = parchmentMinecraftVersion
            mappingsVersion = parchmentMappingsVersion
        }
        runs {
            register("client") {
                client()
                systemProperty("neoforge.enabledGameTestNamespaces", modId)
                gameDirectory.set(rootProject.file("run/${modId}/client"))
            }
            register("server") {
                server()
                programArgument("--nogui")
                systemProperty("neoforge.enabledGameTestNamespaces", modId)
                gameDirectory.set(rootProject.file("run/${modId}/server"))
            }
            register("gameTestServer") {
                type = "gameTestServer"
                systemProperty("neoforge.enabledGameTestNamespaces", modId)
                gameDirectory.set(rootProject.file("run/${modId}/test-server"))
            }
            register("data") {
                data()
                environment("wild-wind-datagen", "true")
                programArguments.addAll(listOf(
                    "--mod", modId, "--all",
                    "--output", generatedDir.absolutePath,
                    "--existing", resourcesDir.absolutePath,
                ))
                gameDirectory.set(rootProject.file("run/${modId}/data"))
            }
            configureEach {
                systemProperty("forge.logging.markers", "REGISTRIES")
                logLevel = Level.DEBUG
            }

        }
        mods {
            register(modId) {
                sourceSet(sourceSets["main"])
            }
        }
    }

    if (atFile.exists().not()) {
        atFile.parentFile.mkdirs()
        atFile.createNewFile()
    }
    if(enumFile.exists().not()) {
        enumFile.parentFile.mkdirs()
        enumFile.createNewFile()
    }
    if (interfaceFile.exists().not()) {
        interfaceFile.parentFile.mkdirs()
        interfaceFile.createNewFile()
    }

    configure<McMetaSettings> {
        this.loaderType = McMetaSettings.Type.NEOFORGE
    }
    configure<NeoForgeModsToml> {

        mods.add(NeoForgeMods(project).apply {
            this.modId = modId
            namespace = modId
            version = modVersion
            displayName = modName + ": " + project.name
            authors = modAuthors
            logoFile = "$modId.png"
            description = "This is ${displayName.get()}"
            if (enumFile.readBytes().isNotEmpty()) {
                enumExtensions = "META-INF/enumextensions.json"
            }
        })
        dependencies().put(modId, arrayOf(
            NeoForgeDependency
                .builder()
                .type(NeoForgeDependency.Type.required)
                .like(ForgeLikeDependency
                    .builder()
                    .modId("neoforge")
                    .versionRange(neoVersionRange)
                    .ordering(ForgeLikeDependency.Order.NONE)
                    .side(ForgeLikeDependency.Side.BOTH)
                    .build())
                .build(),
            NeoForgeDependency
                .builder()
                .type(NeoForgeDependency.Type.required)
                .like(ForgeLikeDependency
                    .builder()
                    .modId("minecraft")
                    .versionRange(mcVersionRange)
                    .ordering(ForgeLikeDependency.Order.NONE)
                    .side(ForgeLikeDependency.Side.BOTH)
                    .build())
                .build()
        ))
        mixins().add("META-INF/$modId.mixins.json")
        if (atFile.readBytes().isNotEmpty()) {
            accessTransformers().add("META-INF/accesstransformer.cfg")
        }
    }

    val gson = GsonBuilder().setPrettyPrinting().create()


    if (mixinsFile.exists().not()) {
        mixinsFile.parentFile.mkdirs()
        val mixinJson = JsonObject().apply {
            addProperty("required", true)
            addProperty("package", "org.polaris2023.$modId.mixin")
            addProperty("compatibilityLevel", "JAVA_21")
            addProperty("refmap", "$modId.remap.json")
            add("mixins", JsonArray())
            add("client", JsonArray())
            add("server", JsonArray())
            addProperty("minVersion", "0.8")

        }
        mixinsFile.bufferedWriter(Charsets.UTF_8).use {
            gson.toJson(mixinJson, it)
        }
    }

    if (atFile.readBytes().isNotEmpty()) {
        neoForge.setAccessTransformers(atFile)
    }

//    modAccessor {
//        createTransformConfiguration(configurations.getAt("compileOnly"))
//        if (atFile.readBytes().isNotEmpty()) {
//            accessTransformerFiles = rootProject.files("src/${modId}/resources/META-INF/accesstransformer.cfg")
//        }
//    }

    val projectNames = listOf(
        "Deco",
        "Adventure",
        "Vanilla Plus Plus",
        "All In All"
    )

    dependencies {
        implementation(
            group = "com.tterrag.registrate",
            name = "Registrate",
            version = "[MC1.21-1.3.0,)"
        )
        implementation(
            group="dev.xkmc",
            name= "l2serial",
            version="[3.1.1,)")
        implementation(
            group="dev.xkmc",
            name= "l2core",
            version="[3.0.8,)"
        )

        val ap = rootProject.files("ap/build/libs/ap.jar")

        if (ap.singleFile.exists()) {
            compileOnly(ap)
            annotationProcessor(ap)
        }

    }
    tasks.jar {
        exclude(".cache")
        if (atFile.readBytes().isEmpty()) {
            exclude("META-INF/accesstransformer.cfg")
        }
        if (enumFile.readBytes().isEmpty()) {
            exclude("META-INF/enumextensions.json")
        }
    }

    neoForge {
        if (interfaceFile.readBytes().isNotEmpty()) {
            interfaceInjectionData.from(interfaceFile)
            interfaceInjectionData.publish(interfaceFile)
        }
    }

    base {
        archivesName = modId
    }
    description = modName
    version = "$mcVersion-$modVersion"
    group = modGroupId

    tasks.jarJar {
        enabled = true
    }
    tasks.javadoc {
        enabled = false
    }

    val dataBuild:TaskProvider<Task> by tasks.registering {
            group = "build"
            val runData: Task by tasks.getting
            dependsOn(runData)

            finalizedBy(tasks.build)
            tasks.build.get().mustRunAfter(runData)

        }


    sonatypeUploader {
        tokenName = properties["central.sonatype.token.name"].toString()
        tokenPasswd = properties["central.sonatype.token.passwd"].toString()
        signing = Action {
            this.keyId = properties["signing.key.id"].toString()
            this.keyPasswd = properties["signing.key.passwd"].toString()
            this.secretKeyPath = properties["signing.secret.key"].toString()
        }
        pom = Action {
            name.set(modName + ": " + project.name)
            description.set("This is ${modName + ": " + project.name}")
            url.set("https://github.com/Polari-Stars-MC/WildWind")
            licenses {
                license {
                    name.set("LGPL-2.1")
                    url = "https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html"
                }
            }
            developers {
                developer {
                    id.set("baka4n")
                    name.set("baka4n")
                    email.set("474899581@qq.com")
                }
            }
            scm {
                connection = "scm:git:git://github.com/Polaris-Stars-MC/WildWind.git"
                developerConnection = "scm:git:ssh://github.com/Polaris-Stars-MC/WildWind.git"
                url = "https://github.com/Polaris-Stars-MC/WildWind"
            }
        }
    }
}

val dataBuild by tasks.registering {
    group = "build"
    subprojects.forEach {
        val dataBuild by it.tasks.getting
        dependsOn(dataBuild)
    }
}