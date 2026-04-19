plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
}

android {
    namespace = "com.velord.core.navigation"
}

dependencies {
    // Template
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.lifecycle.runtime)
    implementation(libs.bundles.compose.all)
    implementation(libs.compose.resources)
    // Lib
    implementation(libs.bundles.voyager)
    implementation(libs.androidx.navigation.fragment)
}
