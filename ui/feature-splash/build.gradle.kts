plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.ui.feature.splash"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.core.coreResource)
    implementation(projects.core.coreUi)
    // Template
    implementation(libs.bundles.compose.all)
    // Lib
    implementation(libs.androidx.core.splashscreen)
}
