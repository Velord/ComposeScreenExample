plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24
}

gradlePlugin {
    plugins {
        register("androidApplicationConvention") {
            id = "velord.android.application"
            implementationClass = "com.velord.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibraryConvention") {
            id = "velord.android.library"
            implementationClass = "com.velord.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidComposeConvention") {
            id = "velord.android.compose"
            implementationClass = "com.velord.buildlogic.AndroidComposeConventionPlugin"
        }
        register("androidViewBindingConvention") {
            id = "velord.android.viewbinding"
            implementationClass = "com.velord.buildlogic.AndroidViewBindingConventionPlugin"
        }
        register("koinConvention") {
            id = "velord.koin"
            implementationClass = "com.velord.buildlogic.KoinConventionPlugin"
        }
        register("featureUiConvention") {
            id = "velord.feature.ui"
            implementationClass = "com.velord.buildlogic.FeatureUiConventionPlugin"
        }
        register("featureUiKoinConvention") {
            id = "velord.feature.ui.koin"
            implementationClass = "com.velord.buildlogic.FeatureUiKoinConventionPlugin"
        }
        register("domainUsecaseConvention") {
            id = "velord.domain.usecase"
            implementationClass = "com.velord.buildlogic.DomainUsecaseConventionPlugin"
        }
        register("widgetGlanceConvention") {
            id = "velord.widget.glance"
            implementationClass = "com.velord.buildlogic.WidgetGlanceConventionPlugin"
        }
        register("kmpLibraryConvention") {
            id = "velord.kmp.library"
            implementationClass = "com.velord.buildlogic.KmpLibraryConventionPlugin"
        }
    }
}

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.compose)
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${libs.versions.googleKsp.get()}")
}
