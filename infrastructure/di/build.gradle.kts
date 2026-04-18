plugins {
    alias(libs.plugins.convention.android.library)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.di"
}

dependencies {
    implementation(projects.infrastructure.config)
    implementation(projects.model)
    // Module Domain
    implementation(projects.domain.usecaseSetting)
    implementation(projects.domain.usecaseMovie)
    // Module Data Source
    implementation(projects.data.backend)
    implementation(projects.data.datastore)
    implementation(projects.data.appstate)
    implementation(projects.data.gateway)
    implementation(projects.data.db)
    implementation(projects.data.os)
    // Module UI
    implementation(projects.ui.sharedviewmodel)
    // Module UI Feature
    implementation(projects.ui.featureDemo)
    implementation(projects.ui.featureCamerarecording)
    implementation(projects.ui.featureBottomnavigation)
    implementation(projects.ui.featureSetting)
    implementation(projects.ui.featureSplash)
    implementation(projects.ui.featureFlowsummator)
    implementation(projects.ui.featureMovie)
    implementation(projects.ui.featureDemoDialog)
    // Template
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.androidx.module)
}
