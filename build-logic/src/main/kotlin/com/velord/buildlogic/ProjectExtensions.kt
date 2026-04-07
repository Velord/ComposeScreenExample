package com.velord.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

internal val Project.libs: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal fun Project.version(name: String): String =
    libs.findVersion(name).get().requiredVersion

internal fun Project.addBundle(configurationName: String, bundleName: String) {
    val bundle = libs.findBundle(bundleName).get().get()
    bundle.forEach { dependency ->
        dependencies.add(configurationName, dependency)
    }
}

internal fun Project.addLibraryDependency(configurationName: String, libraryName: String) {
    dependencies.add(configurationName, libs.findLibrary(libraryName).get().get())
}

internal fun Project.addProjectDependency(configurationName: String, projectPath: String) {
    dependencies.add(configurationName, project(projectPath))
}
