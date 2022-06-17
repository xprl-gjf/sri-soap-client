
val java_language_version: String by project
val java_target_jdk_version: String by project

plugins {
    `java-library`
}

apply<ec.com.xprl.gradle.MavenArtifactPlugin>()

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.valueOf(java_language_version)
java.targetCompatibility = JavaVersion.valueOf(java_target_jdk_version)

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
