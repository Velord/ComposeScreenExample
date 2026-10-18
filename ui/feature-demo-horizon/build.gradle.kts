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
            // Compose
            implementation(libs.compose.runtime)
            // Horizon Bloom
            implementation(libs.horizon.bloom.demo)
        }
    }
}
