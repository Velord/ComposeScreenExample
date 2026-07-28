plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.velord.model"
    }

    sourceSets {
        commonMain.dependencies {
            // Kotlin DateTime
            api(libs.kotlin.datetime)
            // Kamera
            api(libs.kamera.core)
            // Kotlin Serialization
            implementation(libs.kotlin.serialization.json)
        }
        commonTest.dependencies {
            // Testing
            implementation(libs.kotlin.test)
        }
    }
}
