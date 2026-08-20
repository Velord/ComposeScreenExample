plugins {
    alias(libs.plugins.convention.multiplatform.library)
}

kotlin {
    android {
        namespace = "com.velord.ui.sharedviewmodel"
    }

    sourceSets {
        commonMain.dependencies {
            // Module
            api(projects.model)
            api(projects.core.coreResource)
            // Module Infrastructure
            api(projects.infrastructure.config)
            api(projects.infrastructure.util)
            // Module Domain
            api(projects.domain.usecaseEvent)
            api(projects.domain.usecaseSetting)
            // Template
            implementation(libs.bundles.kotlin.core)
            // Lib
            api(libs.kotlin.coroutine.core)
            api(libs.androidx.lifecycle.viewmodel.core)
            implementation(libs.kermit)
            implementation(libs.koin.core)
        }
    }
}
