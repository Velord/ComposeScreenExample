package com.velord.backend.model

import com.velord.model.movie.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    fun toDomain() = Movie(
        id = id,
        title = title,
        description = description,
        isLiked = false,
        date = Movie.toCalendar(date),
        imagePath = imageUrl,
        rating = rating
    )
}
