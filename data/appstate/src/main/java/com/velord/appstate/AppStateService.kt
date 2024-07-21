package com.velord.appstate

import com.velord.model.movie.Movie
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.model.settings.ThemeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

interface AppStateService {
    val themeConfigFlow: MutableStateFlow<ThemeConfig>
    val movieRosterFlow: MutableStateFlow<List<Movie>>
    val movieSortFlow: MutableStateFlow<List<MovieSortOption>>
}

@Single
class AppStateServiceImpl : AppStateService {
    override val themeConfigFlow = MutableStateFlow(ThemeConfig.DEFAULT)
    override val movieRosterFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val movieSortFlow = MutableStateFlow(listOf(
        MovieSortOption(SortType.DateDescending, isSelected = true),
        MovieSortOption(SortType.DateAscending, isSelected = false),
    ))
}