plugins {
    id("velord.android.library")
}

android {
    namespace = "com.velord.core.resource"
}

dependencies {
    // Templates
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.androidx.module)
    implementation(libs.androidx.core.splashscreen)
}
