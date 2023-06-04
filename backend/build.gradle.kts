plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("plugin.serialization") version "1.8.21"
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.velord.backend"

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
        targetSdk = libs.versions.targetApi.get().toInt()
        minSdk = libs.versions.minApi.get().toInt()
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
    // Module
    implementation(project(":model"))
    implementation(project(":datastore"))
    implementation(project(":util"))
    // Templates
    implementation(libs.androidx.core)
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.network)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

// https://slack-chats.kotlinlang.org/t/9025044/after-updating-my-project-to-kotlin-1-8-0-i-m-getting-the-fo
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}
