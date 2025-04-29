plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "com.codeitsolo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.codeitsolo.MainKt")
}