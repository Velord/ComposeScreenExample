package com.velord.ui.sharedviewmodel

import com.velord.infrastructure.config.BuildConfigResolver
import com.velord.infrastructure.config.NavigationLib
import com.velord.model.AppEvent
import com.velord.model.ToastConfig
import com.velord.usecase.event.GetAppEventFlowUC
import com.velord.usecase.event.GetToastConfigFlowUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

// Only one valid place for "Provided" annotation.
// As this ViewModule can not be placed inside manual setup in viewModelModule DI.
// Also, "app" module uses ksp generation.
// Mixing them together derives telling compile time verification that GetToastConfigFlowUC
// will be provided during runtime.
class MainVM(
    private val getAppEventFlowUC: GetAppEventFlowUC,
    private val getToastConfigFlowUC: GetToastConfigFlowUC,
    private val buildConfigResolver: BuildConfigResolver,
) : CoroutineScopeVM() {

    val appEventFlow = MutableSharedFlow<AppEvent>()
    val toastConfigFlow = MutableSharedFlow<ToastConfig>()
    val navigationLib: NavigationLib get() = buildConfigResolver.getNavigationLib()

    private val actionFlow = MutableSharedFlow<MainUiAction>()

    init {
        observe()
    }

    fun onAction(action: MainUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    // TODO: Define branches when they appear
                    else -> {}
                }
            }
        }
        launch {
            getToastConfigFlowUC().collect { toastConfig ->
                toastConfigFlow.emit(toastConfig)
            }
        }
        launch {
            getAppEventFlowUC().collect { appEvent ->
                appEventFlow.emit(appEvent)
            }
        }
    }
}
