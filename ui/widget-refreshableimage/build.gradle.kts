plugins {
    alias(libs.plugins.convention.widget.glance)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
}

android {
    namespace = "com.velord.ui.widget.refreshableimage"
}

dependencies {
    // Module Model
    implementation(projects.model)
    // Module Domain
    implementation(projects.domain.usecaseLocalization)
    // Coil
    implementation(libs.bundles.coil)
    // AndroidX
    implementation(libs.androidx.core.ktx)
    // Koin
    implementation(libs.koin.core)
}
