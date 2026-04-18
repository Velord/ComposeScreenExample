plugins {
    alias(libs.plugins.convention.android.library)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.sharedviewmodel"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.infrastructure.util)
    implementation(projects.domain.usecaseSetting)
    // Template
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.androidx.module)
}
