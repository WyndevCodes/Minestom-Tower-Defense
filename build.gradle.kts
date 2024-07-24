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

    //Minecraft
    implementation("net.minestom:minestom-snapshots:1f34e60ea6")
}

tasks.test {
    useJUnitPlatform()
}