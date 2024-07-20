package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.usecase.movie.dataSource.MovieSortDS

class SetMovieSortOptionUC(private val movieSortDS: MovieSortDS) {

    operator fun invoke(newOption: MovieSortOption) {
        val updated = newOption.copy(isSelected = true)
        movieSortDS.update(updated)
    }
}