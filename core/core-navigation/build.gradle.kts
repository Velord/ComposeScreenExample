plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.core.navigation"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Core
            implementation(projects.core.coreResource)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.kotlin.serialization.json)
            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            // Navigation Voyager
            implementation(libs.voyager.navigator)
        }

        androidMain.dependencies {
            // AndroidX
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.fragment.ktx)
            // Navigation
            implementation(libs.androidx.navigation.fragment)
        }
    }
}
