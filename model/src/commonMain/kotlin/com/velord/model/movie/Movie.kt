package com.velord.model.movie

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

private const val PICSUM_HOST = "https://picsum.photos/200/300"

private val cardFormat = LocalDateTime.Format {
    year()
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    dayOfMonth()
}

private val dividerFormat = LocalDateTime.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    year()
}

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val isLiked: Boolean,
    val date: Instant,
    val rating: Float,
    val voteCount: Int,
    val imagePath: String? = null,
) {

    fun formattedDateForCard(tz: TimeZone): String =
        cardFormat.format(date.toLocalDateTime(tz))

    fun formattedDateForDivider(tz: TimeZone): String =
        dividerFormat.format(date.toLocalDateTime(tz))

    fun isAnotherMonthOrYear(other: Instant?, tz: TimeZone): Boolean {
        if (other == null) return true
        val a = date.toLocalDateTime(tz)
        val b = other.toLocalDateTime(tz)
        return a.monthNumber != b.monthNumber || a.year != b.year
    }

    val imageUrl: String get() = if (imagePath.isNullOrEmpty()) {
        PICSUM_HOST
    } else {
        "https://image.tmdb.org/t/p/original$imagePath"
    }

    companion object {

        fun toInstant(date: String): Instant = try {
            LocalDate.parse(date).atStartOfDayIn(TimeZone.UTC)
        } catch (_: Exception) {
            Clock.System.now()
        }

        fun toRaw(instant: Instant): String =
            instant.toLocalDateTime(TimeZone.UTC).date.toString()
    }
}

fun List<Movie>.findRecentTimeInMilli(): Long =
    maxByOrNull { it.date.toEpochMilliseconds() }?.date?.toEpochMilliseconds() ?: 0L
