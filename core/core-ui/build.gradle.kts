plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.core.ui"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.model)
            implementation(projects.infrastructure.util)
            implementation(projects.core.coreResource)
            implementation(projects.ui.sharedviewmodel)
            // Template
            implementation(libs.bundles.compose.ui.core)
            implementation(libs.bundles.compose.foundation)
            implementation(libs.bundles.compose.core)
            implementation(libs.bundles.compose.material3.all)
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // Lib
            implementation(libs.androidx.graphics.shapes)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.coil.compose)
        }

        desktopMain.dependencies {
            // Template
            implementation(libs.bundles.kotlin.core)
            // Lib
            implementation(libs.compose.material.icons.core)
        }

        androidMain.dependencies {
            // Module
            implementation(projects.model)
            implementation(projects.infrastructure.util)
            implementation(projects.core.coreResource)
            implementation(projects.ui.sharedviewmodel)
            // Template
            implementation(libs.bundles.kotlin.core)
            implementation(libs.bundles.androidx.module)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.ui)
            // Lib
            implementation(libs.kermit)
            implementation(libs.androidx.navigation.ui) // Material Dialog lib
            implementation(libs.androidx.core.animation)
            // Glance
            implementation(libs.androidx.glance)
            implementation(libs.androidx.glance.appwidget)
        }
    }
}
