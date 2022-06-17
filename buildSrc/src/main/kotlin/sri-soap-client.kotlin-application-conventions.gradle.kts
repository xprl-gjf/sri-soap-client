import org.gradle.kotlin.dsl.application
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_jvm_target_version: String by project

plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = kotlin_jvm_target_version
}
