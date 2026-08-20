plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.splash"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.model)
            implementation(projects.core.coreUi)
            implementation(projects.ui.sharedviewmodel)
            implementation(libs.kotlin.coroutine.core)
            // Template
            // Compose
            implementation(libs.compose.animation)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            // AndroidX
            implementation(libs.androidx.lifecycle.runtime.compose)
        }

        androidMain.dependencies {
            // Lib
            implementation(libs.androidx.core.splashscreen)
        }
    }
}
