plugins {
    id("velord.android.library")
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
    id("velord.koin")
}

android {
    namespace = "com.velord.sharedviewmodel"
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":domain:usecase-setting"))
    // Templates
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.androidx.module)
}
