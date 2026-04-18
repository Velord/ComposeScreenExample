plugins {
    alias(libs.plugins.convention.feature.ui.koin)
}

android {
    namespace = "com.velord.setting"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.core.coreNavigation)
    implementation(projects.core.coreUi)
    implementation(projects.core.coreResource)
    implementation(projects.ui.sharedviewmodel)
    implementation(projects.ui.featureBottomnavigation)
    // Template
    implementation(libs.bundles.compose.all)
}
