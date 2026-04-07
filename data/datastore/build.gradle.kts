plugins {
    id("velord.android.library")
    alias(libs.plugins.kotlin.plugin.serialization)
    id("velord.koin")
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
