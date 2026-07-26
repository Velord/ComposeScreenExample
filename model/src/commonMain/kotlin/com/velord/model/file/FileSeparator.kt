package com.velord.model.file

@JvmInline
value class FileSeparator(val value: Char) {

    val stringValue: String get() = value.toString()
}

expect val fileSeparator: FileSeparator
