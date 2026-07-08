package com.velord.data.gateway.toast

import com.velord.data.appstate.AppStateDataSource
import com.velord.model.ToastConfig
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class ToastGateway(private val appState: AppStateDataSource) {

    fun getFlow(): Flow<ToastConfig> = appState.toastConfigFlow

    suspend fun show(config: ToastConfig) {
        appState.toastConfigFlow.emit(config)
    }
}
