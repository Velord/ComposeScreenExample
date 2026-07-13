plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.bottomnavigation"

        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.core.coreNavigation)
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            implementation(projects.domain.usecaseEvent)
            implementation(projects.ui.sharedviewmodel)
            // Core
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.transitions)
            // Other
            implementation(libs.kermit)
        }

        androidMain.dependencies {
            implementation(projects.model)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.constraint)
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.androidx.navigation.fragment)
            implementation(libs.androidx.navigation.ui)
            implementation(libs.velord.multiplebackstack)
        }
    }
}
