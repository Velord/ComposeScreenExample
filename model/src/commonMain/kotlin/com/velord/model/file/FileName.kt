package com.velord.model.file

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val DEFAULT_EXT = ".mp4"

@JvmInline
value class FileName private constructor(val value: String) {

    companion object {
        operator fun invoke(extension: String = DEFAULT_EXT): FileName = FileName(
            createValue(extension = extension)
        )

        fun createValue(
            extension: String = DEFAULT_EXT,
            timeZone: TimeZone = TimeZone.currentSystemDefault(),
        ): String {
            val now = Instant.fromEpochMilliseconds(currentEpochMilliseconds()).toLocalDateTime(timeZone)
            val date = listOf(now.year, now.monthNumber, now.dayOfMonth)
                .joinToString(separator = "-") { it.toString().padStart(2, '0') }
            val time = listOf(now.hour, now.minute, now.second, now.nanosecond / 1_000_000)
                .joinToString(separator = "-") { it.toString().padStart(2, '0') }
            return "$date-$time$extension"
        }
    }
}
