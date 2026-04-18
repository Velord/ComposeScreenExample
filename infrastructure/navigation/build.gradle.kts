plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.navigation"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.infrastructure.util)
    implementation(projects.infrastructure.config)
    // Module Core
    implementation(projects.core.coreUi)
    implementation(projects.core.coreNavigation)
    implementation(projects.core.coreResource)
    // Module UI
    implementation(projects.ui.sharedviewmodel)
    // Module UI Feature
    implementation(projects.ui.featureDemo)
    implementation(projects.ui.featureCamerarecording)
    implementation(projects.ui.featureBottomnavigation)
    implementation(projects.ui.featureSetting)
    implementation(projects.ui.featureSplash)
    implementation(projects.ui.featureDemoShape)
    implementation(projects.ui.featureDemoModifier)
    implementation(projects.ui.featureDemoMorph)
    implementation(projects.ui.featureDemoHintphonenumber)
    implementation(projects.ui.featureDemoDialog)
    implementation(projects.ui.featureFlowsummator)
    implementation(projects.ui.featureMovie)
    // Template
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.androidx.navigation.all)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.kotlin.serialization)
    // Navigation 3-rd Party
    // Navigation Voyager
    implementation(libs.bundles.voyager)
    // Navigation Compose Destinations
    implementation(libs.bundles.compose.destinations)
    ksp(libs.compose.destinations.ksp)
}

ksp {
    arg("compose-destinations.moduleName", "navigation")
}
