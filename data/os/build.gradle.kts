plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.os"
}

dependencies {
    // Template
    implementation(libs.bundles.kotlin.module)
}

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
}
