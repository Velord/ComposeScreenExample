package com.velord.appstate

import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

interface AppStateService {
    val flow: MutableStateFlow<ThemeConfig>
}

@Single
class AppStateServiceImpl : AppStateService {
    override val flow = MutableStateFlow(ThemeConfig.DEFAULT)
}