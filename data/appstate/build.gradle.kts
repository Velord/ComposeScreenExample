plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.appstate"
}

dependencies {
    // Modules
    implementation(project(":model"))
    // Templates
    implementation(libs.bundles.kotlin.module)
}
