plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.3.5"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "team.unnamed"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    mavenCentral()
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    // resource-pack server
    // https://github.com/unnamed/creative
    implementation("team.unnamed:creative-server:0.1.13-SNAPSHOT")

    val ver = "0.2.0-SNAPSHOT" // the hephaestus-engine version
    implementation("team.unnamed:hephaestus-runtime-bukkit-api:$ver")
    implementation("team.unnamed:hephaestus-runtime-bukkit-adapt-v1_18_R2:$ver")
    implementation("team.unnamed:hephaestus-reader-blockbench:$ver") {
        // the server already includes GSON
        exclude(group = "com.google.code.gson", module = "gson")
    }
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}