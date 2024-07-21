package com.velord.backend.model

import com.velord.model.movie.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Serializable
data class MovieResponse(
    val id: Int,
    @SerialName("original_title")
    val title: String,
    @SerialName("release_date")
    val date: String,
    @SerialName("poster_path")
    val imageUrl: String,
    @SerialName("overview")
    val description: String,
    @SerialName("vote_average")
    val rating: Float
) {
    private fun parseDate(): Calendar {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date: Date? = sdf.parse(date)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }

        return calendar
    }

    fun toDomain() = Movie(
        id = id,
        title = title,
        description = description,
        isLiked = false,
        date = parseDate(),
        imagePath = imageUrl,
        rating = rating
    )
}
