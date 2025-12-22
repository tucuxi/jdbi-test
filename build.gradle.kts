plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
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
    // BOMs
    implementation(platform(libs.jdbi.bom))
    implementation(platform(libs.spring.boot.dependencies))

    // Spring Core
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.jackson.module.kotlin)
    implementation(kotlin("reflect"))

    // JDBI Bundle
    implementation(libs.bundles.jdbi)
    
    // Database
    implementation(libs.bundles.flyway)
    runtimeOnly(libs.postgresql)

    // Utilities
    implementation(libs.kotlin.logging)
    implementation(libs.ulid)

    // Testing
    testImplementation(kotlin("test"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.jdbi.testing)
    testImplementation(libs.pg.embedded)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
