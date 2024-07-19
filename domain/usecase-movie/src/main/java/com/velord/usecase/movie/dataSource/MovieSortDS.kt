package com.velord.usecase.movie.dataSource

import com.velord.model.movie.MovieSortOption
import kotlinx.coroutines.flow.Flow

interface MovieSortDS {
    fun getFlow(): Flow<List<MovieSortOption>>
    fun getSelectedFlow(): Flow<MovieSortOption>
    fun update(newOption: MovieSortOption)
}