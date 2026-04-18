plugins {
    alias(libs.plugins.convention.feature.ui.koin)
}

android {
    namespace = "com.velord.feature.demo"
}

dependencies {
    // Module Infrastructure
    implementation(projects.infrastructure.util)
    implementation(projects.infrastructure.config)
    // Module Core
    implementation(projects.core.coreResource)
    implementation(projects.core.coreNavigation)
    implementation(projects.core.coreUi)
    // Module Ui
    implementation(projects.ui.sharedviewmodel)
    implementation(projects.ui.featureBottomnavigation)
    // Template
    implementation(libs.bundles.compose.all)
}
