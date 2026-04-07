plugins {
    id("velord.android.library")
    id("velord.koin")
}

android {
    namespace = "com.velord.os"
}

dependencies {
    // Templates
    implementation(libs.bundles.kotlin.module)
}

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
}
