plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "kds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform("org.jdbi:jdbi3-bom:3.45.4"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.20")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-core:1.5.8")
    implementation("com.aallam.ulid:ulid-kotlin:1.3.0")
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-spring5")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject")
    implementation("org.jdbi:jdbi3-jackson2")
    implementation("org.flywaydb:flyway-core:10.17.3")
    implementation("org.flywaydb:flyway-database-postgresql:10.18.0")
    runtimeOnly("org.postgresql:postgresql:42.7.3")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jdbi:jdbi3-testing:3.45.4")
    testImplementation("de.softwareforge.testing:pg-embedded:5.1.0")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
