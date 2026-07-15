plugins {
    alias(libs.plugins.convention.feature.ui.koin)
}

android {
    namespace = "com.velord.ui.feature.camerarecording"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.core.coreResource)
    implementation(projects.core.coreUi)
    implementation(projects.core.coreNavigation)
    implementation(projects.infrastructure.util)
    implementation(projects.domain.usecaseCamera)
    implementation(projects.ui.sharedviewmodel)
    implementation(projects.ui.featureBottomnavigation)
    // Template
    implementation(libs.bundles.androidx.camera.all)
    implementation(libs.bundles.compose.all)
    // Navigation
    ksp(libs.compose.destinations.ksp)
    // 3-rd Party
    implementation(libs.bundles.kamera.all)
}
