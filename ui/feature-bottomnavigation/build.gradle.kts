plugins {
    alias(libs.plugins.convention.feature.ui.koin)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.velord.bottomnavigation"
}

dependencies {
    // Module
    implementation(projects.infrastructure.util)
    implementation(projects.core.coreResource)
    implementation(projects.core.coreNavigation)
    implementation(projects.core.coreUi)
    implementation(projects.ui.sharedviewmodel)
    // Template
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.kotlin.serialization)
    implementation(libs.bundles.voyager) // Refactor. Too much effort need to move to nav module
    // Lib. Only Jetpack Navigation based on Fragment depends on it.
    implementation(libs.androidx.navigation.ui)
    // Third Party
    implementation(libs.velord.multiplebackstack)
}
