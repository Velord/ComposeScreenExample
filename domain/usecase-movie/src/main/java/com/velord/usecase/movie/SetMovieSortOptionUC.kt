package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.usecase.movie.dataSource.MovieSortDS

fun interface SetMovieSortOptionUC : (MovieSortOption) -> Unit

class SetMovieSortOptionUCImpl(
    private val movieSortDS: MovieSortDS
) : SetMovieSortOptionUC {

    override operator fun invoke(newOption: MovieSortOption) {
        val updated = newOption.copy(isSelected = true)
        movieSortDS.update(updated)
    }
}
