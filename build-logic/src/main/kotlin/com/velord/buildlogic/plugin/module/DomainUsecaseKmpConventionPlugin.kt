package com.velord.buildlogic.plugin.module

import com.velord.buildlogic.model.ProjectModule
import com.velord.buildlogic.util.applyPlugin
import com.velord.buildlogic.util.commonMainImplementationLibrary
import com.velord.buildlogic.util.commonMainImplementationProject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class DomainUsecaseKmpConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("convention-kmp-library")

        extensions.configure<KotlinMultiplatformExtension> {
            commonMainImplementationProject(target, ProjectModule.MODEL)
            commonMainImplementationLibrary(target, "kotlin-coroutine-core")
        }
    }
}
