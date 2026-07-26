package com.velord.data.os.file

import java.awt.Desktop
import java.io.File

private const val APP_FOLDER_NAME = "ComposeScreenExample"
private const val APP_PRIVATE_DIRECTORY_NAME = ".$APP_FOLDER_NAME"
private const val MOVIES_DIRECTORY_NAME = "Movies"

class DesktopFileDataSource : BaseFileDataSource() {

    override val appFolderName: String = APP_FOLDER_NAME

    override fun createDirectory(path: String): String = File(path).apply { mkdirs() }.absolutePath

    override fun openDirectory(path: String): Boolean = try {
        Desktop.getDesktop().open(File(path))
        true
    } catch (_: Exception) {
        false
    }

    override fun getRootDirectoryPath(
        directory: FileDirectory,
        storageScope: FileStorageScope,
    ): String = when (storageScope) {
        FileStorageScope.Public -> directory.getPublicRootDirectoryPath()
        FileStorageScope.AppPrivate -> directory.getAppPrivateRootDirectoryPath()
    }

    private fun FileDirectory.getPublicRootDirectoryPath(): String = when (this) {
        FileDirectory.Video -> File(userHomePath, MOVIES_DIRECTORY_NAME).absolutePath
    }

    private fun FileDirectory.getAppPrivateRootDirectoryPath(): String = when (this) {
        FileDirectory.Video -> File(userHomePath, APP_PRIVATE_DIRECTORY_NAME).absolutePath
    }

    private val userHomePath: String get() = System.getProperty("user.home")
}
