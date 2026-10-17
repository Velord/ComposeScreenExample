plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.demo.horizon"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Core
            implementation(projects.core.coreUi)
            // Module UI
            implementation(projects.ui.sharedviewmodel)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            // Lifecycle
            implementation(libs.androidx.lifecycle.runtime.compose)
            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            // Horizon Bloom
            implementation(libs.horizon.bloom)
        }
    }
}
