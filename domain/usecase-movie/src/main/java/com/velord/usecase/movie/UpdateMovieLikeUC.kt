package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieFavoriteDS

fun interface UpdateMovieLikeUC : suspend (Movie) -> Unit

class UpdateMovieLikeUCImpl(
    private val dataSource: MovieFavoriteDS
) : UpdateMovieLikeUC {

    override suspend operator fun invoke(movie: Movie) {
        val updated = movie.copy(isLiked = movie.isLiked.not())
        dataSource.update(updated)
    }
}
