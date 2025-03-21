plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "2.1.10"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
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
    implementation(platform("org.jdbi:jdbi3-bom:3.48.0"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.10")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-core:1.5.17")
    implementation("com.aallam.ulid:ulid-kotlin:1.3.0")
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-spring")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject")
    implementation("org.jdbi:jdbi3-jackson2")
    implementation("org.flywaydb:flyway-core:11.4.0")
    implementation("org.flywaydb:flyway-database-postgresql:11.4.0")
    runtimeOnly("org.postgresql:postgresql:42.7.5")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jdbi:jdbi3-testing")
    testImplementation("de.softwareforge.testing:pg-embedded:5.2.0")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
