package com.velord.gateway.movie

import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieSortGateway(

) : MovieSortDS {

    private val sortFlow = MutableStateFlow(listOf(
        MovieSortOption(SortType.DateDescending, isSelected = true),
        MovieSortOption(SortType.DateAscending, isSelected = false),
    ))

    override fun getFlow(): Flow<List<MovieSortOption>> = sortFlow

    override fun getSelectedFlow(): Flow<MovieSortOption> = sortFlow
        .map { roster -> roster.firstOrNull { it.isSelected } }
        .filterNotNull()

    override fun update(newOption: MovieSortOption) {
        sortFlow.update {
            it.map { option ->
                if (newOption.type == option.type) {
                    newOption
                } else {
                    option.copy(isSelected = false)
                }
            }
        }
    }
}