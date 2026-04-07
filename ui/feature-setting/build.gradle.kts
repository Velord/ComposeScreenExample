plugins {
    id("velord.android.library")
    id("velord.android.compose")
    id("velord.android.viewbinding")
    id("velord.koin")
}

android {
    namespace = "com.velord.setting"
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-resource"))
    implementation(project(":ui:sharedviewmodel"))
    implementation(project(":ui:feature-bottomnavigation"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
}
