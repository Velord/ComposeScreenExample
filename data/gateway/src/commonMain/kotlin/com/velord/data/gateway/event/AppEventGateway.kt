package com.velord.data.gateway.event

import com.velord.data.appstate.AppStateDataSource
import com.velord.model.AppEvent
import com.velord.model.ToastConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AppEventGateway(private val appState: AppStateDataSource) {

    fun getFlow(): Flow<AppEvent> = appState.appEventFlow

    fun getToastFlow(): Flow<ToastConfig> = getFlow()
        .filterIsInstance<AppEvent.Toast>()
        .map { event -> event.config }

    suspend fun showToast(config: ToastConfig) {
        emit(AppEvent.Toast(config))
    }

    suspend fun requestExit() {
        emit(AppEvent.Exit)
    }

    private suspend fun emit(event: AppEvent) {
        appState.appEventFlow.emit(event)
    }
}
