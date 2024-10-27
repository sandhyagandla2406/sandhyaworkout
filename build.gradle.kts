// build.gradle.kts (Project-level)
buildscript {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.1") // Update as necessary
        classpath("com.google.gms:google-services:4.4.2") // Google Services
    }
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
