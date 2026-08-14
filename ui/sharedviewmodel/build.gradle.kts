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
            api(projects.infrastructure.config)
            api(projects.infrastructure.util)
            api(projects.core.coreResource)
            api(projects.domain.usecaseEvent)
            api(projects.domain.usecaseLocalization)
            api(projects.domain.usecaseSetting)

            // Template
            implementation(libs.bundles.kotlin.core)
            // Lib
            api(libs.kotlin.coroutine.core)
            api(libs.androidx.lifecycle.viewmodel.core)
            implementation(libs.kermit)
        }
    }
}
