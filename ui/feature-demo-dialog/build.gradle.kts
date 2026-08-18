plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.demo.dialog"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            implementation(projects.data.localization)
            implementation(projects.ui.sharedviewmodel)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // Template
            // Compose
            implementation(libs.compose.animation)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.window)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
        }
//        desktopMain.dependencies {
//            implementation(libs.compose.material3.window)
//            implementation(libs.compose.desktop.currentOs)
//        }
    }
}
