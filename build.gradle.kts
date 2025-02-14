plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    application
    `java-library`
    `maven-publish`
}

group = "org.drachens"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:620ebe5d6b")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("de.articdive:jnoise-pipeline:4.1.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.4.1")
    implementation("org.javassist:javassist:3.30.2-GA")
    implementation("net.kyori:adventure-text-minimessage:4.18.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    applicationDefaultJvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
}


tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.drachens.Main"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }
}

tasks.withType<JavaExec> {
    jvmArgs(
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=net.minestom.server/net.minestom.server.network.player=ALL-UNNAMED"
    )
}

