package com.velord.data.os.file

import com.velord.model.file.FileName

interface FileDataSource {
    fun getDirectoryPath(
        directory: FileDirectory,
        storageScope: FileStorageScope,
    ): String
    fun getFileName(filePath: String): FileName
    fun getParentDirectoryPath(filePath: String): String?
    fun openDirectory(path: String): Boolean
}
