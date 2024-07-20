package com.velord.model.movie

import java.text.SimpleDateFormat
import java.util.Calendar

const val PICSUM_HOST = "https://picsum.photos"

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val isLiked: Boolean,
    val date: Calendar,
    val imagePath: String
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

    val imageUrl: String get() = if (imagePath.contains(PICSUM_HOST)) {
        imagePath
    } else {
        "https://image.tmdb.org/t/p/original$imagePath"
    }
}