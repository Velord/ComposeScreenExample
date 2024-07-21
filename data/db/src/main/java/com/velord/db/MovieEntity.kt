package com.velord.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.velord.model.movie.Movie

@Entity(tableName = "MovieEntity")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val isLiked: Boolean,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val rating: Float,
    @ColumnInfo
    val voteCount: Int,
    @ColumnInfo
    val imagePath: String?,
) {

    fun toDomain(): Movie {
        return Movie(
            id = id,
            title = title,
            description = description,
            isLiked = isLiked,
            date = Movie.toCalendar(date),
            rating = rating,
            voteCount = voteCount,
            imagePath = imagePath,
        )
    }

    companion object {
        operator fun invoke(movie: Movie): MovieEntity = MovieEntity(
            id = movie.id,
            title = movie.title,
            description = movie.description,
            isLiked = movie.isLiked,
            date = Movie.toRaw(movie.date),
            rating = movie.rating,
            voteCount = movie.voteCount,
            imagePath = movie.imagePath,
        )
    }
}