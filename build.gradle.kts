plugins {
    id("java")
}

group = "me.wyndev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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

    //Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    //Apache common
    implementation("org.apache.commons:commons-lang3:3.15.0")
    implementation("commons-io:commons-io:2.16.1")

    //Minecraft
    implementation("net.minestom:minestom-snapshots:99ca16e263") //Minestom
    implementation("dev.hollowcube:schem:1.2.0") //Schematic loader
    implementation("net.kyori:adventure-text-minimessage:4.17.0") //Minecraft component generation: see https://docs.advntr.dev/minimessage/index.html

}

tasks.test {
    useJUnitPlatform()
}