package com.velord.composescreenexample.desktop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.velord.core.ui.compose.component.ToastHost
import com.velord.core.ui.theme.AppThemeContainer
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.infrastructure.di.createCommonAppModuleRoster
import com.velord.infrastructure.navigation.NavigationContent
import com.velord.model.AppEvent
import com.velord.ui.feature.splash.SplashVM
import com.velord.ui.sharedviewmodel.MainVM
import org.koin.compose.koinInject
import org.koin.core.context.startKoin

// TODO: splash API
fun main() = application {
    startKoin {
        modules(createCommonAppModuleRoster())
    }

    val splashVM: SplashVM = koinInject()
    val mainVM: MainVM = koinInject()

    Window(
        onCloseRequest = ::exitApplication,
        title = "ComposeScreenExample",
    ) {
        val appEventFlow = mainVM.appEventFlow
        ObserveSharedFlow(appEventFlow) {
            when (it) {
                is AppEvent.Exit -> exitApplication()
                else -> Unit
            }
        }

        AppThemeContainer {
            ToastHost(
                toastEventFlow = mainVM.toastConfigFlow,
                modifier = Modifier.fillMaxSize(),
                content = {
                    NavigationContent(navigationLib = mainVM.navigationLib)
                }
            )
        }
    }
}
