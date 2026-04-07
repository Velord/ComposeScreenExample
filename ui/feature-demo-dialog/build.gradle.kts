plugins {
    id("velord.android.library")
    id("velord.android.compose")
    id("velord.android.viewbinding")
}

android {
    namespace = "com.velord.dialogdemo"
}

dependencies {
    // Modules
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
}
