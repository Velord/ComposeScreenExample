package com.velord.model.camera

import com.velord.model.file.FileName
import com.velord.model.file.fileSeparator

data class CameraVideoAsset(val fullFilePath: String) {

    val fileName: FileName get() = FileName.fromPath(fullFilePath)

    val filePath: String get() = fullFilePath

    val directoryPath: String? get() {
        val separatorIndex = fullFilePath.lastIndexOf(fileSeparator.value)
        if (separatorIndex <= 0) return null

        return fullFilePath.substring(0, separatorIndex)
    }
}
