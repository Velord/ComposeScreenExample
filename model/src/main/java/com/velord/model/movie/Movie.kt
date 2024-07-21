package com.velord.model.movie

import java.text.SimpleDateFormat
import java.util.Calendar

private const val PICSUM_HOST = "https://picsum.photos/200/300"

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val isLiked: Boolean,
    val date: Calendar,
    val rating: Float,
    val imagePath: String? = null,
) {

    val formattedDateForCard: String get() {
        val formatterYear = SimpleDateFormat("yyyy")
        val formatterMonth = SimpleDateFormat("MMM")
        val formatterDay = SimpleDateFormat("dd")

        val year = formatterYear.format(date.time)
        val month = formatterMonth.format(date.time)
        val day = formatterDay.format(date.time)

        return "$year $month $day"
    }

    val formattedDateForDivider: String get() {
        val formatterYear = SimpleDateFormat("yyyy")
        val formatterMonth = SimpleDateFormat("MMM")

        val year = formatterYear.format(date.time)
        val month = formatterMonth.format(date.time)

        return "$month $year"
    }

    fun isAnotherMonthOrYear(calendar: Calendar?): Boolean =
        date.get(Calendar.MONTH) != calendar?.get(Calendar.MONTH) ||
                date.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)

    val imageUrl: String get() = if (imagePath == null) {
        PICSUM_HOST
    } else {
        "https://image.tmdb.org/t/p/original$imagePath"
    }
}

data object MoviePagination {
    const val PRELOAD_BEFORE_END = 10

    fun shouldLoadMore(
        lastVisibleIndex: Int,
        totalItemCount: Int,
    ): Boolean = lastVisibleIndex >= totalItemCount - PRELOAD_BEFORE_END
}

fun List<Movie>.findRecentTimeInMilli(): Long =
    maxByOrNull { it.date.timeInMillis }?.date?.timeInMillis ?: 0