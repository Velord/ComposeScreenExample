package com.velord.data.gateway.file

import com.velord.data.os.file.FileDataSource
import org.koin.core.annotation.Single

@Single
class FileGateway(private val fileDataSource: FileDataSource) {

    fun openDirectory(path: String): Boolean = fileDataSource.openDirectory(path)
}