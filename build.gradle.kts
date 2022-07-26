import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.7.10"
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "club.eridani"
version = "1.0.0"



repositories {
    mavenCentral()
}

val lwjglVersion = "3.3.1"
val jomlVersion = "1.10.4"
val winNatives = "natives-windows"
val linuxNatives = "natives-linux"


val pack: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(pack)

loom {
    accessWidenerPath.set(file("src/main/resources/vulkanmod.accesswidener"))
}

dependencies {

    fun include(dep: Dependency?) {
        pack(dep!!)
    }

    include(implementation(kotlin("stdlib")))
    include(implementation("org.lwjgl:lwjgl:$lwjglVersion"))
    include(implementation("org.lwjgl:lwjgl-vulkan:$lwjglVersion"))
    include(implementation("org.lwjgl:lwjgl-vma:$lwjglVersion"))
    include(implementation("org.joml:joml:${jomlVersion}"))
    include(runtimeOnly("org.lwjgl:lwjgl-vma:$lwjglVersion:$winNatives"))
    include(runtimeOnly("org.lwjgl:lwjgl-vma:$lwjglVersion:$linuxNatives"))

    include(implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion"))
    include(runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:$winNatives"))
    include(runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:$linuxNatives"))
    include(implementation("org.lwjgl:lwjgl-stb:$lwjglVersion"))
    include(runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglVersion:$winNatives"))
    include(runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglVersion:$linuxNatives"))
    include(implementation("org.lwjgl:lwjgl-openal:$lwjglVersion"))
    include(runtimeOnly("org.lwjgl:lwjgl-openal:$lwjglVersion:$winNatives"))
    include(runtimeOnly("org.lwjgl:lwjgl-openal:$lwjglVersion:$linuxNatives"))
    include(runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$winNatives"))
    include(runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$linuxNatives"))

    include(implementation("org.lwjgl:lwjgl-shaderc:$lwjglVersion"))
    include(runtimeOnly("org.lwjgl:lwjgl-shaderc:$lwjglVersion:$winNatives"))
    include(runtimeOnly("org.lwjgl:lwjgl-shaderc:$lwjglVersion:$linuxNatives"))

    implementation(pack)


    val minecraft_version: String by project
    val yarn_mappings: String by project
    val loader_version: String by project
    val fabric_version: String by project

    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")

}



tasks {

    shadowJar {
        configurations = listOf(pack)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.release.set(17)
    }
}