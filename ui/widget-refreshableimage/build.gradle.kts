plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.compose)
}

android {
    namespace = "com.velord.refreshableimage"

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
        targetCompatibility = JavaVersion.VERSION_24
    }
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
    implementation(libs.bundles.coil)
    // Compose
    implementation(libs.bundles.compose.ui)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    compilerOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}