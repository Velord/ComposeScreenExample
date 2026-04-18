plugins {
    alias(libs.plugins.convention.feature.ui.koin)
}

android {
    namespace = "com.velord.flowsummator"
}

dependencies {
    // Module
    implementation(projects.infrastructure.util)
    implementation(projects.core.coreResource)
    implementation(projects.core.coreUi)
    implementation(projects.ui.sharedviewmodel)
    // Template
    implementation(libs.bundles.compose.all)
}
