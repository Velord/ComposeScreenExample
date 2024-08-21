package com.velord.usecase.setting

import com.velord.model.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow

class GetThemeConfigUC(private val dataSource: GetThemeConfigDS) {
    suspend operator fun invoke(): Flow<ThemeConfig> = dataSource.getFlow()
}