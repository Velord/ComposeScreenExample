plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.gateway"
}

dependencies {
    // Modules
    implementation(project(":model"))
    // Data
    implementation(project(":data:datastore"))
    implementation(project(":data:appstate"))
    implementation(project(":data:backend"))
    implementation(project(":data:db"))
    // Use case
    implementation(project(":domain:usecase-setting"))
    implementation(project(":domain:usecase-movie"))
}
