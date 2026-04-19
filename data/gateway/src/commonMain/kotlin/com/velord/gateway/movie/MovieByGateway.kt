package com.velord.gateway.movie

import com.velord.model.movie.SortType
import com.velord.usecase.movie.model.MovieFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Single

@Single
class MovieByGateway(
    private val movieGateway: MovieGateway,
    private val movieSortGateway: MovieSortGateway,
    private val movieFavoriteReader: MovieFavoriteReader,
) {

    fun getBySort(): MovieFlow {
        val all = movieGateway.getFlow()
        val sort = movieSortGateway.getSelectedFlow()
        return MovieFlow(
            all.combine(sort) { movies, sortOption ->
                when (sortOption.type) {
                    SortType.DateDescending -> movies.sortedByDescending { it.date }
                    SortType.DateAscending -> movies.sortedBy { it.date }
                }
            }.catch {},
        )
    }

    fun getByFavorite(): MovieFlow {
        val favorite = movieFavoriteReader.getFlow()
        val sort = movieSortGateway.getSelectedFlow()
        return MovieFlow(
            favorite.combine(sort) { movies, sortOption ->
                when (sortOption.type) {
                    SortType.DateDescending -> movies.sortedByDescending { it.date }
                    SortType.DateAscending -> movies.sortedBy { it.date }
                }
            }.catch {},
        )
    }
}
