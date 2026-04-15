package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow

fun interface GetThemeConfigUC : suspend () -> Flow<ThemeConfig>

class GetThemeConfigUCImpl(
    private val dataSource: GetThemeConfigDS
) : GetThemeConfigUC {

    override suspend operator fun invoke(): Flow<ThemeConfig> = dataSource.getFlow()
}
