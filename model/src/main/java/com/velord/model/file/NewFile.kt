package com.velord.model.file

import android.content.Context
import com.velord.util.context.createDirInCacheOrFilesDir
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val DEFAULT_DIR_NAME = "videos"
private const val DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val DEFAULT_EXT = ".mp4"

@JvmInline
value class OutputDirectory(val value: File) {
    constructor(
        context: Context,
        desiredDirName: String = DEFAULT_DIR_NAME
    ) : this(context.createDirInCacheOrFilesDir(desiredDirName))
}

@JvmInline
value class FileName(val value: String) {
    companion object {
        fun invoke(
            simpleDateFormatPattern: String = DEFAULT_DATE_FORMAT_PATTERN,
            extension: String = DEFAULT_EXT,
        ): FileName = FileName(
            SimpleDateFormat(
                simpleDateFormatPattern,
                Locale.US
            ).format(System.currentTimeMillis()) + extension
        )
    }
}

@JvmInline
value class NewFile(val value: File) {

    constructor(
        name: FileName,
        dir: OutputDirectory,
    ) : this(
        File(dir.value, name.value)
    )

    constructor(
        dir: OutputDirectory,
        simpleDateFormatPattern: String = DEFAULT_DATE_FORMAT_PATTERN,
        extension: String = DEFAULT_EXT,
    ) : this(
        File(
            dir.value,
            FileName.invoke(simpleDateFormatPattern, extension).value
        )
    )
}