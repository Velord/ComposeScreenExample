package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow

class GetThemeConfigUC(private val dataSource: GetThemeConfigDS) {
    suspend fun getConfigFlow(): Flow<ThemeConfig> = dataSource.get()
}