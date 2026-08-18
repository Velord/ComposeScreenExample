plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.demo.hintphonenumber"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            // Template
            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
        }

        androidMain.dependencies {
            // Module
            implementation(projects.infrastructure.util)
            // Template
            // AndroidX
            implementation(libs.androidx.activity.compose)
        }
    }
}
