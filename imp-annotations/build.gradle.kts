import org.gradle.api.file.DuplicatesStrategy

plugins {
    `java-library`
    `maven-publish`
    idea
    base
}

// imp-annotations：imp 中间层的版本注解定义（ADR-0003）
// 纯 Java，无任何 Minecraft/NeoForge 依赖；保留策略全部为 SOURCE，
// 注解只在编译期被 imp-processor 读取，不进入任何产物。
//
// 字节码级别固定为 17：它是 1.20 族（Java 17）能引用的最低级别，
// 这样同一份注解 jar 可以被全部版本族使用。

group = "org.polaris2023.wildwind.imp"
version = property("modVersion") as String

base.archivesName = "ww-imp-annotations"

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 17
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}
