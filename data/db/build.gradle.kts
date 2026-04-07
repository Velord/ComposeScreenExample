plugins {
    id("velord.android.library")
    id("velord.koin")
}

android {
    namespace = "com.velord.db"
}

dependencies {
    // Modules
    implementation(project(":model"))
    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.androidx.room.coroutines)
}
