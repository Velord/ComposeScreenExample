plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.velord.infrastructure.util"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.serialization.json)
            implementation(libs.kermit)
            implementation(libs.compose.runtime)
        }

        androidMain.dependencies {
            implementation(libs.bundles.androidx.module)
            implementation(libs.bundles.androidx.credentials)
            implementation(libs.bundles.google.gms)
            implementation(libs.bundles.network.retrofit)
            implementation(libs.bundles.ui)
        }

        desktopMain.dependencies {
            implementation(libs.bundles.network.retrofit)
        }
    }
}
