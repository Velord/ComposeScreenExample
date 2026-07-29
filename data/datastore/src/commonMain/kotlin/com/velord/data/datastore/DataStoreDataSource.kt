package com.velord.data.datastore

import com.velord.data.datastore.appSetting.AppSettingDataStore
import com.velord.model.movie.MovieFilterOption
import com.velord.model.setting.AppSetting
import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

interface DataStoreDataSource {
    suspend fun checkAppFirstLaunch(): Boolean
    suspend fun setThemeConfig(theme: ThemeConfig)
    fun getAppSettingFlow(): Flow<AppSetting>
    suspend fun setMovieFilters(filters: List<MovieFilterOption>)
}

@Single(binds = [DataStoreDataSource::class])
class DataStoreDataSourceImpl(
    private val appSetting: AppSettingDataStore,
) : DataStoreDataSource {

    private suspend fun setFirstLaunch() {
        appSetting.updateData {
            it.copy(isAppFirstLaunch = true)
        }
    }

    override suspend fun checkAppFirstLaunch(): Boolean = appSetting.flow.map {
        val isFirstLaunch = it.isAppFirstLaunch
        if (isFirstLaunch) setFirstLaunch()

        isFirstLaunch
    }
        .first()

    override suspend fun setThemeConfig(theme: ThemeConfig) {
        appSetting.updateData {
            it.copy(theme = theme)
        }
    }

    override fun getAppSettingFlow(): Flow<AppSetting> = appSetting.flow

    override suspend fun setMovieFilters(filters: List<MovieFilterOption>) {
        appSetting.updateData {
            it.copy(movieFilters = filters)
        }
    }
}
