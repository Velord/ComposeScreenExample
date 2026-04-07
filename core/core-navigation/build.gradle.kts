plugins {
    id("velord.android.library")
    id("velord.android.compose")
    id("velord.android.viewbinding")
}

android {
    namespace = "com.velord.core.navigation"
}

dependencies {
    // Template
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.lifecycle.runtime)
    implementation(libs.bundles.compose.all)
    // Lib
    implementation(libs.bundles.voyager)
    implementation(libs.androidx.navigation.fragment)
}
