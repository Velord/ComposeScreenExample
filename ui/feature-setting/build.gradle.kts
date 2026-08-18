plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.multiplatform.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.setting"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.model)
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            implementation(projects.ui.sharedviewmodel)
            // Template
            // Compose
            implementation(libs.compose.animation)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
        }

        androidMain.dependencies {
            // Module
            implementation(projects.core.coreNavigation)
            implementation(projects.core.coreUi)
            implementation(projects.ui.featureBottomnavigation)
            // Template
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            // AndroidX
            implementation(libs.androidx.fragment.ktx)
        }
    }
}
