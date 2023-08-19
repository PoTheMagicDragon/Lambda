import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import xyz.wagyourtail.unimined.api.unimined

plugins {
    kotlin("jvm") version "1.9.0"
    id("xyz.wagyourtail.unimined") version "1.0.3"
}

group = project.properties["group"] as String
version = project.properties["version"] as String

val mcVersion = project.properties["minecraft_version"] as String
val fabricVersion = project.properties["fabric_loader_version"] as String
val forgeVersion = project.properties["forge_version"] as String

val mainImplementation = "mainImplementation"
val forgeImplementation = "forgeImplementation"
val fabricImplementation = "fabricImplementation"

val mainSourceSet = "main"
val forgeSourceSet = "forge"
val fabricSourceSet = "fabric"

configurations {
    create(mainImplementation)
    create(forgeImplementation ).extendsFrom(configurations.getByName(mainImplementation))
    create(fabricImplementation).extendsFrom(configurations.getByName(mainImplementation))
}

sourceSets {
    main {
        compileClasspath += configurations[mainImplementation]
        runtimeClasspath += configurations[mainImplementation]
    }
    create(forgeSourceSet) {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
    create(fabricSourceSet) {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

tasks {
    val javaVersion = JavaVersion.VERSION_17

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }

    register<Jar>("fabricJar") {
        from(sourceSets.getByName(fabricSourceSet).output, sourceSets.main.get().output)
        archiveClassifier.set("fabric")
    }

    register<Jar>("forgeJar") {
        from(sourceSets.getByName(forgeSourceSet).output, sourceSets.main.get().output)
        archiveClassifier.set("forge")
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json"){ expand(mutableMapOf("version" to project.version))}
        filesMatching("META-INF/mods.toml"){ expand(mutableMapOf("version" to project.version))}
    }

    jar { enabled = false }
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven") { name = "sponge" }
}

dependencies{
    configurations[mainImplementation]("org.spongepowered:mixin:0.8.5-SNAPSHOT")
}

unimined {
    useGlobalCache = false

    minecraft(sourceSets[mainSourceSet]) {
        version(mcVersion)
        side("client")
        mappings {
            intermediary()
            yarn("1")
        }
        defaultRemapJar = false
    }

    minecraft(sourceSets[fabricSourceSet]) {
        version(mcVersion)
        side("client")
        mappings {
            intermediary()
            yarn("1")
        }
        fabric{
            loader(fabricVersion)
        }
    }

    minecraft(sourceSets[forgeSourceSet]) {
        version(mcVersion)
        side("client")
        mappings {
            intermediary()
            yarn("1")
        }
        minecraftForge {
            loader(forgeVersion)
            mixinConfig("lambda.mixins.json")
        }
    }
}

