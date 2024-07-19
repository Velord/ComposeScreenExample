package com.velord.usecase.movie

import com.velord.model.profile.movie.Movie
import com.velord.usecase.movie.dataSource.MovieDS

class UpdateMovieLikeUC(private val dataSource: MovieDS) {
    operator fun invoke(movie: Movie) {
        val updated = movie.copy(isLiked = !movie.isLiked)
        dataSource.update(updated)
    }
}