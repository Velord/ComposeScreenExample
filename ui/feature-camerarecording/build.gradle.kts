plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.camerarecording"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            implementation(projects.infrastructure.util)
            implementation(projects.domain.usecaseCamera)
            implementation(projects.domain.usecaseEvent)
            implementation(projects.ui.sharedviewmodel)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.kamera.core)
            implementation(libs.kermit)
        }

        androidMain.dependencies {
            implementation(projects.core.coreUi)
            implementation(projects.ui.featureBottomnavigation)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.androidx.navigation.fragment)
        }
    }
}
