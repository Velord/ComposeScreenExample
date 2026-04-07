plugins {
    id("velord.android.library")
    id("velord.android.compose")
    id("velord.android.viewbinding")
}

android {
    namespace = "com.velord.modifierdemo"
}

dependencies {
    // Modules
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
}
