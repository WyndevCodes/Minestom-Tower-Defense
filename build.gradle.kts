import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
    id("application")
}

group = "me.wyndev"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
    maven("https://repo.hypera.dev/snapshots")
    maven("https://reposilite.worldseed.online/public")
}

dependencies {
    //Logger
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    //Lombok
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    //LuckPerms
    implementation("me.lucko.luckperms:minestom:5.4-SNAPSHOT")

    //Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    //Apache common
    implementation("org.apache.commons:commons-lang3:3.15.0")
    implementation("commons-io:commons-io:2.16.1")

    //Data serialisation (JSON / YAML)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.2")

    //Minecraft
    implementation("net.minestom:minestom-snapshots:99ca16e263") //Minestom
    implementation("dev.hollowcube:schem:1.2.0") //Schematic loader
    implementation("net.kyori:adventure-text-minimessage:4.17.0") //Minecraft component generation: see https://docs.advntr.dev/minimessage/index.html
    implementation("net.worldseed.multipart:WorldSeedEntityEngine:10.7.0")
    implementation("net.worldseed.particleemitter:ParticleEmitter:1.4.0")
    implementation("ca.atlasengine:atlas-projectiles:1.0.1")

}



application {
    mainClass.set("me.wyndev.towerdefense.Main")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("server.jar")
}

tasks.test {
    useJUnitPlatform()
}