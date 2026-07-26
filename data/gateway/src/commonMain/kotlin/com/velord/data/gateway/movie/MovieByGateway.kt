package com.velord.data.gateway.movie

import com.velord.model.movie.SortType
import com.velord.usecase.movie.model.MovieFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Single

@Single
class MovieByGateway(
    private val movieGateway: MovieGateway,
    private val movieSortGateway: MovieSortGateway,
    private val movieFavoriteGateway: MovieFavoriteGateway,
) {

    fun getBySort(): MovieFlow {
        val all = movieGateway.getFlow()
        val sort = movieSortGateway.getSelectedFlow()
        return MovieFlow(
            all.combine(sort) { movieRoster, sortOption ->
                when (sortOption.type) {
                    SortType.DateDescending -> movieRoster.sortedByDescending { it.date }
                    SortType.DateAscending -> movieRoster.sortedBy { it.date }
                }
            }.catch {},
        )
    }

    fun getByFavorite(): MovieFlow {
        val favorite = movieFavoriteGateway.getFlow()
        val sort = movieSortGateway.getSelectedFlow()
        return MovieFlow(
            favorite.combine(sort) { movieRoster, sortOption ->
                when (sortOption.type) {
                    SortType.DateDescending -> movieRoster.sortedByDescending { it.date }
                    SortType.DateAscending -> movieRoster.sortedBy { it.date }
                }
            }.catch {},
        )
    }
}
