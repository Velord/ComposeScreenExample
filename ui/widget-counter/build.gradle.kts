plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
}

android {
    namespace = "com.velord.widgetcounter"
}

dependencies {
    // Modules
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.glance)
    implementation(libs.bundles.androidx.workmanager)
    // Compose
    implementation(libs.bundles.compose.ui)
}
