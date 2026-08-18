plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.camerarecording"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Model
            implementation(projects.model)
            // Module Core
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            // Module Data
            implementation(projects.data.localization)
            // Module Infrastructure
            implementation(projects.infrastructure.util)
            // Module Domain
            implementation(projects.domain.usecaseCamera)
            implementation(projects.domain.usecaseEvent)
            // Module UI
            implementation(projects.ui.sharedviewmodel)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            // AndroidX
            implementation(libs.androidx.lifecycle.runtime.compose)
            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            // Kamera
            implementation(libs.kamera.core)
            // Logging
            implementation(libs.kermit)
        }

        androidMain.dependencies {
            // Module Core
            implementation(projects.core.coreUi)
            // Module UI Feature
            implementation(projects.ui.featureBottomnavigation)
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            // AndroidX
            implementation(libs.androidx.fragment.ktx)
            // Navigation
            implementation(libs.androidx.navigation.fragment)
        }
    }
}
