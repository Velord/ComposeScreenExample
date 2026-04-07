plugins {
    alias(libs.plugins.convention.feature.ui.koin)
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
    implementation(libs.bundles.androidx.camera.all)
    implementation(libs.bundles.compose.all)
    // Navigation
    ksp(libs.compose.destinations.ksp)
}
