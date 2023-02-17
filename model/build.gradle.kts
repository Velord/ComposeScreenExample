plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.7.0"
}

android {
    namespace = "com.velord.model"

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minApi.get().toInt()
        targetSdk = libs.versions.targetApi.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // Templates
    implementation(libs.bundles.androidx.core)
    implementation(libs.bundles.kotlin.base)
    // Json
    implementation(libs.kotlin.serialization.json)
}

// https://slack-chats.kotlinlang.org/t/9025044/after-updating-my-project-to-kotlin-1-8-0-i-m-getting-the-fo
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

