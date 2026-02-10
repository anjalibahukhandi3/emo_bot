plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.10")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.10")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.10")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.10")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.23")
}

kotlin {
    jvmToolchain(17)
}
