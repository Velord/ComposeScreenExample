package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class DomainUsecaseKmpConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("convention-kmp-library")

        extensions.configure<KotlinMultiplatformExtension> {
            commonMainImplementationProject(target, ":model")
            commonMainImplementationLibrary(target, "kotlin-coroutine-core")
        }
    }
}
