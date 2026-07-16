package com.velord.data.os.file

import com.velord.model.file.FileName
import com.velord.model.file.fileSeparator

abstract class BaseFileDataSource : FileDataSource {

    protected abstract val appFolderName: String

    protected abstract fun getRootDirectoryPath(
        directory: FileDirectory,
        storageScope: FileStorageScope,
    ): String

    protected abstract fun createDirectory(path: String): String

    override fun getDirectoryPath(
        directory: FileDirectory,
        storageScope: FileStorageScope,
    ): String {
        val rootDirectoryPath = getRootDirectoryPath(directory, storageScope)
        val path = when (storageScope) {
            FileStorageScope.Public -> joinPath(
                rootDirectoryPath,
                appFolderName,
                directory.childDirectoryName,
            )
            FileStorageScope.AppPrivate -> joinPath(
                rootDirectoryPath,
                directory.childDirectoryName,
            )
        }
        return createDirectory(path)
    }

    override fun getFileName(filePath: String): FileName = FileName.fromPath(filePath)

    override fun getParentDirectoryPath(filePath: String): String? {
        val separatorIndex = filePath.lastIndexOf(fileSeparator.value)
        if (separatorIndex <= 0) return null

        return filePath.substring(0, separatorIndex)
    }

    private fun joinPath(vararg pathRoster: String): String = pathRoster
        .filter { path -> path.isNotBlank() }
        .joinToString(fileSeparator.stringValue) { path -> path.trimPathSeparators() }

    private fun String.trimPathSeparators(): String = trim(fileSeparator.value)
}
