import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.plugin.compose)
}

android {
    namespace = "com.velord.widgetcounter"

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
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }
}

composeCompiler {
    featureFlags = setOf(
        ComposeFeatureFlag.OptimizeNonSkippingGroups
    )
}

dependencies {
    // Modules
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.glance)
    implementation(libs.bundles.androidx.workmanager)
    // Compose
    implementation(libs.bundles.compose.ui)
}