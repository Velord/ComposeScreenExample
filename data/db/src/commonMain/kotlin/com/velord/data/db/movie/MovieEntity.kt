package com.velord.data.db.movie

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

    fun toDomain(): Movie = Movie(
        id = id,
        title = title,
        description = description,
        isLiked = isLiked,
        date = Movie.toInstant(date),
        rating = rating,
        voteCount = voteCount,
        imagePath = imagePath,
    )
}

internal fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    description = description,
    isLiked = isLiked,
    date = Movie.toRaw(date),
    rating = rating,
    voteCount = voteCount,
    imagePath = imagePath,
)
