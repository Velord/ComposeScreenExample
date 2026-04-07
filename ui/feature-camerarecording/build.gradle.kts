plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.camerarecording"
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-navigation"))
    implementation(project(":ui:sharedviewmodel"))
    implementation(project(":ui:feature-bottomnavigation"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.androidx.camera.all)
    implementation(libs.bundles.compose.all)
    // Navigation
    ksp(libs.compose.destinations.ksp)
}
