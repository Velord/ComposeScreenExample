package com.velord.data.gateway.movie

import com.velord.model.movie.FilterType
import com.velord.model.movie.Movie
import com.velord.model.movie.MovieFilterOption
import com.velord.model.movie.SortType
import com.velord.usecase.movie.model.MovieFlow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Single

@Single
class MovieByGateway(
    private val movieGateway: MovieGateway,
    private val movieSortGateway: MovieSortGateway,
    private val movieFavoriteGateway: MovieFavoriteGateway,
    private val movieFilterGateway: MovieFilterGateway,
) {

    fun getBySortAndFilter(): MovieFlow = MovieFlow(
        combine(
            movieGateway.getAllFlow().flow,
            movieFilterGateway.getFlow().flow,
            movieSortGateway.getSelectedSortOptionFlow(),
        ) { movies, filterOptions, sortOption ->
            movies.filterBy(filterOptions).sortBy(sortOption.type)
        },
    )

    fun getByFavorite(): MovieFlow = MovieFlow(
        combine(
            movieFavoriteGateway.getAllFlow().flow,
            movieSortGateway.getSelectedSortOptionFlow(),
        ) { movies, sortOption ->
            movies.sortBy(sortOption.type)
        },
    )

    private fun List<Movie>.filterBy(
        options: List<MovieFilterOption>,
    ): List<Movie> = filter { movie ->
        options.all { option ->
            when (val type = option.type) {
                is FilterType.Rating -> movie.rating in type.start..type.end
                is FilterType.VoteCount -> movie.voteCount in type.start..type.end
            }
        }
    }

    private fun List<Movie>.sortBy(type: SortType): List<Movie> = when (type) {
        SortType.DateDescending -> sortedByDescending(Movie::date)
        SortType.DateAscending -> sortedBy(Movie::date)
    }
}
