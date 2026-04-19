package com.velord.gateway.movie

import com.velord.appstate.AppStateDataSource
import com.velord.model.movie.MovieSortOption
import com.velord.usecase.movie.model.MovieSortOptionFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieSortGateway(private val appState: AppStateDataSource) {

    fun getFlow(): MovieSortOptionFlow = MovieSortOptionFlow(appState.movieSortFlow)

    fun getSelectedFlow(): Flow<MovieSortOption> = appState.movieSortFlow
        .map { roster -> roster.firstOrNull { it.isSelected } }
        .filterNotNull()

    fun update(newOption: MovieSortOption) {
        val updated = newOption.copy(isSelected = true)
        appState.movieSortFlow.update {
            it.map { option ->
                if (updated.type == option.type) {
                    updated
                } else {
                    option.copy(isSelected = false)
                }
            }
        }
    }

    fun getSelected(): MovieSortOption = appState.movieSortFlow.value
        .firstOrNull { it.isSelected }
        ?: MovieSortOption.Default
}
