import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.plugin.compose)
}

android {
    namespace = "com.velord.core.ui"

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
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Module
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":ui:sharedviewmodel"))
    // Template
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.ui)
    // Lib
    implementation(libs.androidx.navigation.ui) // Material Dialog lib
    // Compose
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}