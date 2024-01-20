plugins {
    kotlin("jvm") version "1.9.21"
}

group = "kds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinVersion by extra(project.property("kotlinVersion"))

dependencies {
    implementation(platform("org.jdbi:jdbi3-bom:3.37.1"))
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("com.aallam.ulid:ulid-kotlin:1.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}