package com.velord.data.gateway.movie

import com.velord.data.appstate.AppStateDataSource
import com.velord.model.movie.MovieSortOption
import com.velord.usecase.movie.model.MovieSortOptionFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieSortGateway(private val appState: AppStateDataSource) {

    fun getFlow(): MovieSortOptionFlow = MovieSortOptionFlow(appState.movieSortFlow)

    fun getSelectedSortOptionFlow(): Flow<MovieSortOption> = appState.movieSortFlow
        .mapNotNull { roster -> roster.firstOrNull { it.isSelected } }

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
        ?: MovieSortOption.DEFAULT
}
