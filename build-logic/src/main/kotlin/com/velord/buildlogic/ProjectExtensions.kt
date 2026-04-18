package com.velord.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal val Project.libs: VersionCatalog
    get() = extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")

internal fun Project.version(name: String): String =
    libs.findVersion(name).get().requiredVersion

internal fun Project.versionInt(name: String): Int = version(name).toInt()

internal fun Project.plugin(alias: String): PluginDependency = libs.findPlugin(alias).get().get()

internal fun Project.pluginId(alias: String): String = plugin(alias).pluginId

internal fun Project.applyPlugin(alias: String) {
    pluginManager.apply(pluginId(alias))
}

internal fun Project.withPlugin(alias: String, action: () -> Unit) {
    pluginManager.withPlugin(pluginId(alias)) {
        action()
    }
}

internal fun Project.library(libraryName: String) = libs.findLibrary(libraryName).get()

private fun Project.bundle(bundleName: String) = libs.findBundle(bundleName).get().get()

internal fun Project.implementationBundle(bundleName: String) {
    bundle(bundleName).forEach { dependency ->
        dependencies.add(
            "implementation",
            dependency,
        )
    }
}

internal fun Project.implementationLibrary(libraryName: String) {
    dependencies.add(
        "implementation",
        library(libraryName),
    )
}

internal fun Project.implementationPlatformLibrary(libraryName: String) {
    dependencies.add(
        "implementation",
        dependencies.platform(library(libraryName)),
    )
}

internal fun Project.implementationProject(projectPath: String) {
    dependencies.add(
        "implementation",
        projectDependency(projectPath),
    )
}

internal fun Project.kspLibrary(libraryName: String) {
    dependencies.add(
        "ksp",
        library(libraryName),
    )
}

internal fun Project.projectDependency(projectPath: String) =
    project(projectPath)

internal fun KotlinMultiplatformExtension.commonMainImplementationLibrary(
    project: Project,
    libraryName: String,
) {
    sourceSets.commonMain.dependencies {
        implementation(project.library(libraryName))
    }
}

internal fun KotlinMultiplatformExtension.commonMainImplementationProject(
    project: Project,
    projectPath: String,
) {
    sourceSets.commonMain.dependencies {
        implementation(project.projectDependency(projectPath))
    }
}
