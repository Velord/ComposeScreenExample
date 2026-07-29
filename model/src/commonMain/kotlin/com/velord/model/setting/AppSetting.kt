package com.velord.model.setting

import com.velord.model.movie.MovieFilterOption
import kotlinx.serialization.Serializable

@Serializable
data class AppSetting(
    val isAppFirstLaunch: Boolean,
    val theme: ThemeConfig,
    val movieFilters: List<MovieFilterOption> = MovieFilterOption.ALL
) {
    companion object {
        val DEFAULT = AppSetting(
            isAppFirstLaunch = false,
            theme = ThemeConfig.DEFAULT,
            movieFilters = MovieFilterOption.ALL
        )
    }
}
