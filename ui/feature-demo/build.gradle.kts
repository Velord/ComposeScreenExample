plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.demo"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Model
            implementation(projects.model)
            // Module Infrastructure
            api(projects.infrastructure.config)
            // Module Domain
            implementation(projects.domain.usecaseEvent)
            // Module Core
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            // Module Ui
            api(projects.ui.sharedviewmodel)
            // Template
            // Lib
            implementation(libs.kotlin.coroutine.core)
            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
        }

        androidMain.dependencies {
            // Module Infrastructure
            implementation(projects.infrastructure.util)
            // Module Core
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            // Module Ui
            implementation(projects.ui.featureBottomnavigation)
            // Template
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            // AndroidX
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.androidx.navigation.fragment)
        }
    }
}
