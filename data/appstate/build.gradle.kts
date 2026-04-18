plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.appstate"
}

dependencies {
    // Module
    implementation(project(":model"))
    // Template
    implementation(libs.bundles.kotlin.module)
}
