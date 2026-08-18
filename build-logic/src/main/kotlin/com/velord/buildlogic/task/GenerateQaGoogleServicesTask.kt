package com.velord.buildlogic.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class GenerateQaGoogleServicesTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val sourceApplicationId: Property<String>

    @get:Input
    abstract val targetApplicationId: Property<String>

    @TaskAction
    fun generate() {
        val sourceText = sourceFile.get().asFile.readText()
        val sourcePackageEntry = "\"package_name\": \"${sourceApplicationId.get()}\""
        require(sourceText.contains(sourcePackageEntry)) {
            "Firebase client '${sourceApplicationId.get()}' is missing from google-services.json"
        }

        val destination = outputFile.get().asFile
        destination.parentFile.mkdirs()
        destination.writeText(
            sourceText.replace(
                oldValue = sourcePackageEntry,
                newValue = "\"package_name\": \"${targetApplicationId.get()}\"",
            ),
        )
    }
}
