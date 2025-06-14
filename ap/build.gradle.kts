
plugins {
    java
    idea
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


repositories {
    mavenCentral()
}
dependencies {
    implementation("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    implementation("org.tomlj:tomlj:1.1.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.shadowJar {
    archiveClassifier.set(null as String?)
}

tasks.jar {
    archiveClassifier.set("o")
}