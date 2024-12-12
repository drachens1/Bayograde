plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "org.drachens"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:c148954a47")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("de.articdive:jnoise-pipeline:4.1.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.4.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}