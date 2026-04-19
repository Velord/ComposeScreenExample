plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
}

kotlin {
    android {
        namespace = "com.velord.core.navigation"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.kotlin.serialization.json)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.voyager.navigator)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.androidx.navigation.fragment)
        }
    }
}
