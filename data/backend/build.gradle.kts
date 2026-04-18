plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.backend"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.infrastructure.util)
    implementation(projects.data.datastore)
    // Template
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.network.all)
}
