package com.velord.composescreenexample.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.velord.core.ui.theme.AppThemeContainer
import com.velord.infrastructure.config.GeneratedBuildConfigResolver
import com.velord.infrastructure.di.createCommonAppModuleRoster
import com.velord.infrastructure.navigation.NavigationContent
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(createCommonAppModuleRoster())
    }
    val navigationLib = GeneratedBuildConfigResolver().getNavigationLib()

    Window(
        onCloseRequest = ::exitApplication,
        title = "ComposeScreenExample",
    ) {
        AppThemeContainer {
            NavigationContent(navigationLib = navigationLib)
        }
    }
}
