plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

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
            implementation(libs.bundles.androidx.graphics)
            // Lib
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.coil.compose)
        }

        desktopMain.dependencies {
            // Template
            implementation(libs.bundles.kotlin.core)
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