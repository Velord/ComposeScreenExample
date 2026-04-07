plugins {
    alias(libs.plugins.convention.android.library)
}

android {
    namespace = "com.velord.usecase.setting"
}

dependencies {
    // Modules
    implementation(project(":model"))
    // Templates
    implementation(libs.bundles.kotlin.module)
}
