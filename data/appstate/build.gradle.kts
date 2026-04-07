plugins {
    id("velord.android.library")
    alias(libs.plugins.kotlin.plugin.serialization)
    id("velord.koin")
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
