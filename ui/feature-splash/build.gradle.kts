plugins {
    id("velord.android.library")
    id("velord.android.compose")
    id("velord.android.viewbinding")
}

android {
    namespace = "com.velord.splash"
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
    // Libs
    implementation(libs.androidx.core.splashscreen)
}
