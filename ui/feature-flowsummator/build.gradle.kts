plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
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
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            // AndroidX
            implementation(libs.androidx.fragment.ktx)
        }
    }
}
