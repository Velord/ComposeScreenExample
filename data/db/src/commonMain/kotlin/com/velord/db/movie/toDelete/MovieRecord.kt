package com.velord.db.movie.toDelete

//data class MovieRecord(
//    val id: Int,
//    val title: String,
//    val description: String,
//    val isLiked: Boolean,
//    val date: String,
//    val rating: Float,
//    val voteCount: Int,
//    val imagePath: String?,
//) {
//
//    fun toDomain(): Movie = Movie(
//        id = id,
//        title = title,
//        description = description,
//        isLiked = isLiked,
//        date = Movie.toInstant(date),
//        rating = rating,
//        voteCount = voteCount,
//        imagePath = imagePath,
//    )
//}
//
//internal fun Movie.toRecord(): MovieRecord = MovieRecord(
//    id = id,
//    title = title,
//    description = description,
//    isLiked = isLiked,
//    date = Movie.toRaw(date),
//    rating = rating,
//    voteCount = voteCount,
//    imagePath = imagePath,
//)
