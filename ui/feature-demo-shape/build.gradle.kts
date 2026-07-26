plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.ui.feature.demo.shape"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.coreUi)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
        }

        androidMain.dependencies {
            implementation(projects.core.coreUi)
            implementation(libs.androidx.fragment.ktx)
        }
    }
}
