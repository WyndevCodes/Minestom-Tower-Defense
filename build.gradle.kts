plugins {
    id("java")
}

group = "me.wyndev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.minestom:minestom-snapshots:1f34e60ea6")
}

tasks.test {
    useJUnitPlatform()
}