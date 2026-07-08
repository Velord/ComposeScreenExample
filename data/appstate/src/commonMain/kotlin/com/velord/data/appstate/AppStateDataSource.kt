package com.velord.data.appstate

import com.velord.model.ToastConfig
import com.velord.model.movie.Movie
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

interface AppStateDataSource {
    val themeConfigFlow: MutableStateFlow<ThemeConfig>
    val movieRosterFlow: MutableStateFlow<List<Movie>>
    val movieFavoriteRosterFlow: MutableStateFlow<List<Movie>>
    val movieSortFlow: MutableStateFlow<List<MovieSortOption>>
    val toastConfigFlow: MutableSharedFlow<ToastConfig>
}

@Single(binds = [AppStateDataSource::class])
class AppStateDataSourceImpl : AppStateDataSource {
    override val themeConfigFlow = MutableStateFlow(ThemeConfig.DEFAULT)
    override val movieRosterFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val movieFavoriteRosterFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val movieSortFlow = MutableStateFlow(
        listOf(
            MovieSortOption(SortType.DateDescending, isSelected = true),
            MovieSortOption(SortType.DateAscending, isSelected = false),
        ),
    )
    override val toastConfigFlow = MutableSharedFlow<ToastConfig>()
}
