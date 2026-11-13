import org.gradle.api.file.DuplicatesStrategy

plugins {
    `java-library`
    `maven-publish`
    idea
    base
}

// imp-processor：imp 中间层的注解处理器（ADR-0003）
// 读取 imp-annotations 中的 @Since / @Until / @ParamSince，
// 生成 ImpManifest 常量类。
// 纯 Java + 标准 APT API，无 Minecraft/NeoForge 依赖。
//
// 字节码级别固定 17（与 imp-annotations 对齐，保证 1.20 族也能用）。

group = "org.polaris2023.wildwind.imp"
version = property("modVersion") as String

base.archivesName = "ww-imp-processor"

repositories {
    mavenCentral()
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

dependencies {
    api(project(":imp-annotations"))
    // 源码预处理器需要真正的 Java 语法树（不能靠正则切源码）
    implementation("com.github.javaparser:javaparser-core:3.26.2")
    // 处理器元数据：让 javac 通过 ServiceLoader 发现处理器
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 17
    options.compilerArgs.add("-Xlint:all,-processing")
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
}
