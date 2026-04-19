package com.velord.db.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    fun toRecord(): MovieRecord {
        return MovieRecord(
            id = id,
            title = title,
            description = description,
            isLiked = isLiked,
            date = date,
            rating = rating,
            voteCount = voteCount,
            imagePath = imagePath,
        )
    }

    companion object {
        fun fromRecord(record: MovieRecord): MovieEntity = MovieEntity(
            id = record.id,
            title = record.title,
            description = record.description,
            isLiked = record.isLiked,
            date = record.date,
            rating = record.rating,
            voteCount = record.voteCount,
            imagePath = record.imagePath,
        )
    }
}
