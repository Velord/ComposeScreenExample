plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.db"
}

dependencies {
    // Module
    implementation(project(":model"))
    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.androidx.room.coroutines)
}
