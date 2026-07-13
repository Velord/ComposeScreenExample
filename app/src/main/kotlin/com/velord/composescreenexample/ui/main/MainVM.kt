package com.velord.composescreenexample.ui.main

import android.content.Context
import com.velord.core.ui.compose.glance.GlanceWidgetThemeSustainer
import com.velord.core.ui.compose.glance.updateAll
import com.velord.model.ToastConfig
import com.velord.model.setting.ThemeConfig
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.ui.widget.counter.CounterWidget
import com.velord.ui.widget.refreshableimage.RefreshableImageWidget
import com.velord.usecase.event.GetToastConfigFlowUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

sealed interface MainUiAction {
    data class UpdateTheme(val themeConfig: ThemeConfig?) : MainUiAction
}

// Only one valid place for "Provided" annotation.
// As this ViewModule can not be placed inside manual setup in viewModelModule DI.
// Also, "app" module uses ksp generation.
// Mixing them together derives telling compile time verification that GetToastConfigFlowUC
// will be provided during runtime.
@KoinViewModel
class MainVM(
    private val context: Context,
    @Provided private val getToastConfigFlowUC: GetToastConfigFlowUC,
) : CoroutineScopeVM() {

    val toastConfigFlow = MutableSharedFlow<ToastConfig>()

    private val widgets = listOf<GlanceWidgetThemeSustainer<*>>(
        RefreshableImageWidget(), CounterWidget()
    )

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
                    is MainUiAction.UpdateTheme -> updateTheme(action.themeConfig)
                }
            }
        }
        launch {
            getToastConfigFlowUC().collect { toastConfig ->
                toastConfigFlow.emit(toastConfig)
            }
        }
    }

    private fun updateTheme(themeConfig: ThemeConfig?) {
        if (themeConfig == null) return

        updateAllWidgets(themeConfig)
        updateDataStore()
    }

    private fun updateAllWidgets(themeConfig: ThemeConfig) = launch {
        widgets.updateAll(context, themeConfig)
    }

    private fun updateDataStore() {
        // TODO: save theme to data store
    }
}
