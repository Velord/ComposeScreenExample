plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.demo"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Infrastructure
            api(projects.infrastructure.config)
            // Module Core
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            // Module Ui
            api(projects.ui.sharedviewmodel)
            // Module DaTA
            api(projects.data.appstate)
            // Template
            implementation(libs.kotlin.coroutine.core)
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
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.androidx.navigation.fragment)
        }
    }
}
