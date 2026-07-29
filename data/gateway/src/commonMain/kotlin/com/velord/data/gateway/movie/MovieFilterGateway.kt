package com.velord.data.gateway.movie

import com.velord.data.datastore.DataStoreDataSource
import com.velord.model.movie.MovieFilterOption
import com.velord.usecase.movie.model.MovieFilterOptionFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class MovieFilterGateway(private val dataStore: DataStoreDataSource) {

    fun getFlow(): MovieFilterOptionFlow =
        MovieFilterOptionFlow(dataStore.getAppSettingFlow().map { it.movieFilters })

    suspend fun get(): List<MovieFilterOption> =
        dataStore.getAppSettingFlow().map { it.movieFilters }.first()

    suspend fun update(newOption: MovieFilterOption) {
        val currentFilters = get()
        val updatedFilters = currentFilters.map { option ->
            if (option.type::class == newOption.type::class) newOption else option
        }
        dataStore.setMovieFilters(updatedFilters)
    }
}
