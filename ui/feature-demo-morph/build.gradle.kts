plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.demo.morph"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Core
            implementation(projects.core.coreUi)
            // AndroidX
            implementation(libs.androidx.graphics.shapes)
            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
        }

        androidMain.dependencies {
            // Module Core
            implementation(projects.core.coreUi)
            // AndroidX
            implementation(libs.androidx.fragment.ktx)
        }
    }
}
