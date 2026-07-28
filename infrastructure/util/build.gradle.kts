plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.infrastructure.util"
    }

    sourceSets {
        commonMain.dependencies {
            // Kotlin Serialization
            implementation(libs.kotlin.serialization.json)
            // Logging
            implementation(libs.kermit)
            // Compose
            implementation(libs.compose.runtime)
        }

        androidMain.dependencies {
            // AndroidX
            implementation(libs.bundles.androidx.module)
            implementation(libs.bundles.androidx.credentials)
            // Google Services
            implementation(libs.bundles.google.gms)
            // Network
            implementation(libs.bundles.network.retrofit)
            // UI
            implementation(libs.bundles.ui)
        }

        desktopMain.dependencies {
            // Network
            implementation(libs.bundles.network.retrofit)
        }
    }
}
