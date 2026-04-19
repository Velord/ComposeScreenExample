package com.velord.gateway.setting

import com.velord.model.setting.ThemeConfig

interface ThemeConfigSaver {
    suspend fun save(config: ThemeConfig)
}
