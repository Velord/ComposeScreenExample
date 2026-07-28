plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.movie"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.model)
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            implementation(projects.ui.sharedviewmodel)
            implementation(projects.domain.usecaseMovie)
            // Template
            // Lib
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.kotlin.datetime)
            // Compose
            implementation(libs.compose.animation)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kermit)
            // Third Party
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
        }

        androidMain.dependencies {
            // Compose Scrollbar
            implementation(libs.compose.scrollbar.nanihadesuka)
        }

        desktopMain.dependencies {
            // Desktop
            implementation(compose.desktop.currentOs)
        }
    }
}
