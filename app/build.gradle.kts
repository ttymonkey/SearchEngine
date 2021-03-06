/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.7/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.4.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
    kotlin("plugin.serialization") version "1.4.10"
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.4")

    implementation("org.slf4j:slf4j-simple:1.7.30")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    implementation("org.apache.opennlp:opennlp-tools:1.9.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.0.1")

    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClass.set("com.ttymonkey.search.MainKt")
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}
