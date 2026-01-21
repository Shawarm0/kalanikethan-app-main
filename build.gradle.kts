// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    kotlin("plugin.serialization") version "2.2.10"
}

buildscript {
    dependencies {
        // Optional if you're manually managing the Kotlin version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.10")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
