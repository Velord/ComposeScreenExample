package com.velord.composescreenexample.desktop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.velord.core.ui.compose.component.DesktopBackDispatcher
import com.velord.core.ui.compose.component.LocalDesktopBackDispatcher
import com.velord.core.ui.compose.component.ToastHost
import com.velord.core.ui.theme.AppThemeHost
import com.velord.core.ui.theme.LocalizationHost
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.infrastructure.di.createCommonAppModuleRoster
import com.velord.infrastructure.navigation.NavigationHost
import com.velord.model.AppEvent
import com.velord.ui.feature.splash.SplashScreen
import com.velord.ui.feature.splash.SplashVM
import com.velord.ui.sharedviewmodel.MainVM
import com.velord.usecase.setting.InitializeLocalizationUC
import kotlinx.coroutines.runBlocking
import org.koin.compose.koinInject
import org.koin.core.context.startKoin

fun main() = application {
    val koin = startKoin {
        modules(createCommonAppModuleRoster())
    }.koin

    runBlocking {
        koin.get<InitializeLocalizationUC>()()
    }

    val splashVM: SplashVM = koinInject()
    val mainVM: MainVM = koinInject()
    val dispatcher = remember { DesktopBackDispatcher() }

    Window(
        onCloseRequest = ::exitApplication,
        title = "ComposeScreenExample",
        onPreviewKeyEvent = { event ->
            if (event.type == KeyEventType.KeyDown && event.key == Key.Escape) {
                dispatcher.onBackPressed()
            } else {
                false
            }
        },
    ) {
        ObserveSharedFlow(mainVM.appEventFlow) {
            when (it) {
                is AppEvent.Exit -> exitApplication()
                else -> Unit
            }
        }

        CompositionLocalProvider(LocalDesktopBackDispatcher provides dispatcher) {
            LocalizationHost {
                AppThemeHost {
                    SplashScreen(viewModel = splashVM) {
                        ToastHost(
                            toastEventFlow = mainVM.toastConfigFlow,
                            modifier = Modifier.fillMaxSize(),
                            content = {
                                NavigationHost(navigationLib = mainVM.navigationLib)
                            },
                        )
                    }
                }
            }
        }
    }
}
