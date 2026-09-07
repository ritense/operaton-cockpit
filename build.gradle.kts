import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

val operatonVersion: String by project
val operatonKeycloakVersion: String by project
val kotlinLoggingVersion: String by project
val postgresqlDriverVersion: String by project

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/plugins-release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    val file = File(".env.properties")
    if (!file.exists()) {
        file.createNewFile()
    }
}

plugins {
    // dotenv
    id("co.uzzu.dotenv.gradle")
    id("com.avast.gradle.docker-compose")

    war
    // Idea
    idea
    id("org.jetbrains.gradle.plugin.idea-ext")

    // Spring
    id("org.springframework.boot")
    id("io.spring.dependency-management")

    // Spring boot actuator generator
    id("com.gorylenko.gradle-git-properties")

    // Kotlin
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

group = "com.ritense"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repository.jboss.org/nexus/content/repositories/releases") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.operaton.bpm.springboot:operaton-bpm-spring-boot-starter-webapp:$operatonVersion")

    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.postgresql:postgresql:$postgresqlDriverVersion")


    implementation("org.operaton.bpm.extension:operaton-keycloak:$operatonKeycloakVersion")

    // JAXB runtime required by Jersey for WADL serialization
    implementation("org.glassfish.jaxb:jaxb-runtime")
}

dockerCompose {
    setProjectName("operaton-docker-compose")
    useDockerComposeV2 = true
    stopContainers = false
    removeContainers = false
    removeVolumes = false
    if (DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX) {
        executable = "/usr/local/bin/docker-compose"
        dockerExecutable = "/usr/local/bin/docker"
    }
}

dockerCompose.isRequiredBy(tasks.bootRun)

tasks.bootRun {
    environment.putAll(env.allVariables())
}
