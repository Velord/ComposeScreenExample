plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt) apply false

    // Add Firebase plugins here
    alias(libs.plugins.google.gms.services) apply false
    alias(libs.plugins.google.firebase.crashlytic) apply false
}

// Fast way to clean project
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}