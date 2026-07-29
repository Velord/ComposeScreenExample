import com.velord.buildlogic.model.BuildEnvironment
import com.velord.buildlogic.model.BuildType
import com.velord.buildlogic.util.AppVersion

plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.velord.composescreenexample.desktop"
    }

    sourceSets {
        getByName("desktopMain") {
            dependencies {
                // Module Model
                implementation(projects.model)
                // Module Infrastructure
                implementation(projects.infrastructure.util)
                implementation(projects.infrastructure.navigation)
                implementation(projects.infrastructure.di)
                implementation(projects.infrastructure.config)
                // Module Core
                implementation(projects.core.coreUi)
                implementation(projects.core.coreNavigation)
                implementation(projects.core.coreResource)
                // Module Data
                implementation(projects.data.appstate)
                // Module UI
                implementation(projects.ui.sharedviewmodel)
                // Module UI Feature
                implementation(projects.ui.featureBottomnavigation)
                implementation(projects.ui.featureSplash)
                // Template
                implementation(libs.bundles.kotlin.desktop.module)
                implementation(libs.bundles.compose.ui.core)
                // Desktop
                implementation(compose.desktop.currentOs)
                // Koin
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.velord.composescreenexample.desktop.MainKt"
        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
        }
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "ComposeScreenExample"
            packageVersion = AppVersion.versionName
        }
    }
}

BuildEnvironment.entries.forEach { environment ->
    BuildType.entries.forEach { buildType ->
        val flavor = environment.variantName(buildType)
        val capitalizedFlavor = flavor.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

        tasks.register("run$capitalizedFlavor") {
            group = "compose desktop"
            description = "Run Desktop application with $capitalizedFlavor configuration"
            dependsOn("run")
        }

        tasks.register("package$capitalizedFlavor") {
            group = "compose desktop"
            description = "Package Desktop application with $capitalizedFlavor configuration"
            dependsOn(
                if (buildType == BuildType.Release) "packageReleaseDistributionForCurrentOS"
                else "packageDistributionForCurrentOS"
            )
        }
    }
}
