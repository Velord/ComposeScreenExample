package com.velord.gateway.movie

import com.velord.appstate.AppStateService
import com.velord.model.movie.MovieSortOption
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieSortGateway(
    private val appState: AppStateService
) : MovieSortDS {

    override fun getFlow(): Flow<List<MovieSortOption>> = appState.movieSortFlow

    override fun getSelectedFlow(): Flow<MovieSortOption> = appState.movieSortFlow
        .map { roster -> roster.firstOrNull { it.isSelected } }
        .filterNotNull()

    override fun update(newOption: MovieSortOption) {
        appState.movieSortFlow.update {
            it.map { option ->
                if (newOption.type == option.type) {
                    newOption
                } else {
                    option.copy(isSelected = false)
                }
            }
        }
    }

    override fun getSelected(): MovieSortOption = appState.movieSortFlow.value
        .firstOrNull { it.isSelected }
        ?: MovieSortOption.Default
}