plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
}

android {
    namespace = "com.velord.movie"
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    implementation(project(":domain:usecase-movie"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.coil)
    implementation(libs.androidx.constraint.compose)
    // 3-rd party
    implementation(libs.compose.scrollbar.nanihadesuka)
}
