package com.velord.data.appstate

import com.velord.model.AppEvent
import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraState
import com.velord.model.camera.CameraVideoAsset
import com.velord.model.localization.LocalizationState
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
    val appEventFlow: MutableSharedFlow<AppEvent>
    val cameraSessionFlow: MutableStateFlow<CameraSessionWrapper?>
    val cameraStateFlow: MutableStateFlow<CameraState>
    val lastCameraVideoAssetFlow: MutableStateFlow<CameraVideoAsset?>
    val localizationStateFlow: MutableStateFlow<LocalizationState>
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

    override val appEventFlow = MutableSharedFlow<AppEvent>()

    override val cameraSessionFlow = MutableStateFlow<CameraSessionWrapper?>(null)
    override val cameraStateFlow = MutableStateFlow(CameraState.DEFAULT)
    override val lastCameraVideoAssetFlow = MutableStateFlow<CameraVideoAsset?>(null)

    override val localizationStateFlow = MutableStateFlow(LocalizationState.DEFAULT)
}
