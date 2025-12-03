// Background info https://github.com/gradle/gradle/issues/1697
pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val ideaExt: String by settings
    val dotenvVersion: String by settings

    plugins {
        // Idea
        idea
        id("org.jetbrains.gradle.plugin.idea-ext") version ideaExt

        // Spring
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion

        // Kotlin
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion

        id("co.uzzu.dotenv.gradle") version dotenvVersion
    }
}

val projectName: String by settings
rootProject.name = projectName

buildscript {
    val jsonschema2pojoVersion: String by settings

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jsonschema2pojo:jsonschema2pojo-gradle-plugin:$jsonschema2pojoVersion")
    }
}
