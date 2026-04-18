plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.gateway"
}

dependencies {
    // Module
    implementation(projects.model)
    // Data
    implementation(projects.data.datastore)
    implementation(projects.data.appstate)
    implementation(projects.data.backend)
    implementation(projects.data.db)
    // Use case
    implementation(projects.domain.usecaseSetting)
    implementation(projects.domain.usecaseMovie)
}
