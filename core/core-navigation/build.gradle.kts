plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.velord.core.navigation"

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
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
    buildFeatures {
        compose = true
        viewBinding = true
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    // Template
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.lifecycle.runtime)
    implementation(libs.bundles.compose.all)
    // Lib
    implementation(libs.bundles.voyager)
    implementation(libs.androidx.navigation.fragment)
}