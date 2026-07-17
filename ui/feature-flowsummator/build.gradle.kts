plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.flowsummator"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            implementation(projects.infrastructure.util)
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            implementation(projects.ui.sharedviewmodel)
            implementation(libs.kotlin.bignum)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // Template
            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.koin.android)
        }
    }
}
