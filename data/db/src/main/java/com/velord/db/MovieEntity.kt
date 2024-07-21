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
    val imagePath: String,
    @ColumnInfo
    val rating: Float
) {
    fun toDomain(): Movie {
        return Movie(
            id = id,
            title = title,
            description = description,
            isLiked = isLiked,
            date = Movie.toCalendar(date),
            imagePath = imagePath,
            rating = rating
        )
    }
}