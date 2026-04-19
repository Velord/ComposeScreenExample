plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    android {
        namespace = "com.velord.model"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.datetime)
            implementation(libs.kotlin.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}