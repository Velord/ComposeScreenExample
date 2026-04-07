plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.datastore"
}

dependencies {
    // Modules
    implementation(project(":model"))
    // Templates
    implementation(libs.androidx.datastore)
    implementation(libs.kotlin.serialization.json)
}
