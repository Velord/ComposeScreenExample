package com.velord.buildlogic.util

import com.velord.buildlogic.model.ProjectModule
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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

internal fun KotlinMultiplatformExtension.commonMainImplementationProject(
    project: Project,
    projectPath: ProjectModule,
) {
    commonMainImplementationProject(project, projectPath.path)
}
