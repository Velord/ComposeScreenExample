plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.8.21"
}

android {
    namespace = "com.velord.util"

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
        targetSdk = libs.versions.targetApi.get().toInt()
        minSdk = libs.versions.minApi.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // Templates
    implementation(libs.androidx.core)
    implementation(libs.bundles.kotlin.base)
    implementation(libs.bundles.androidx.module.app)
    implementation(libs.bundles.network)
}

// https://slack-chats.kotlinlang.org/t/9025044/after-updating-my-project-to-kotlin-1-8-0-i-m-getting-the-fo
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}