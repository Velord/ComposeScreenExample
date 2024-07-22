package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import com.velord.usecase.movie.result.UpdateMovieResult

class UpdateMovieLikeUC(private val dataSource: MovieFavoriteDS) {

    suspend operator fun invoke(movie: Movie): UpdateMovieResult {
        val updated = movie.copy(isLiked = movie.isLiked.not())
        return try {
            dataSource.update(updated)
            UpdateMovieResult.Success
        } catch (e: Exception) {
            UpdateMovieResult.DbError(e.message.orEmpty())
        }
    }
}