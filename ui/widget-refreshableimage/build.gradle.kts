plugins {
    alias(libs.plugins.convention.widget.glance)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
}

android {
    namespace = "com.velord.ui.widget.refreshableimage"
}

dependencies {
    // Module Core
    implementation(projects.core.coreResource)
    // Module Domain
    implementation(projects.domain.usecaseSetting)
    // Koin
    implementation(libs.koin.core)
    // Coil
    implementation(libs.bundles.coil)
    // AndroidX
    implementation(libs.androidx.core.ktx)
}
