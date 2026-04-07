plugins {
    id("velord.android.library")
    alias(libs.plugins.kotlin.plugin.serialization)
    id("velord.koin")
}

android {
    namespace = "com.velord.backend"
}

dependencies {
    // Module
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":data:datastore"))
    // Templates
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.network.all)
}
