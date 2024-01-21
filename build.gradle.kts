plugins {
    kotlin("jvm") version "1.9.22"
    id("org.flywaydb.flyway") version ("10.6.0")
}

group = "kds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinVersion by extra(project.property("kotlinVersion"))

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.6.0")
    }
}

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
    jvmToolchain(21)
}

flyway {
    url = "jdbc:postgresql://localhost/first"
    user = "user"
    password = ""
}