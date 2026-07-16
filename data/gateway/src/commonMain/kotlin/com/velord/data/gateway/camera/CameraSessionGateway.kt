package com.velord.data.gateway.camera

import com.velord.data.appstate.AppStateDataSource
import com.velord.data.os.camera.CameraDataSource
import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Single

@Single
class CameraSessionGateway(
    private val cameraDataSource: CameraDataSource,
    private val appState: AppStateDataSource,
) {

    private val sessionMutex = Mutex()

    fun getSession(): StateFlow<CameraSessionWrapper?> = appState.cameraSessionFlow

    suspend fun createSession() = sessionMutex.withLock {
        if (appState.cameraSessionFlow.value != null) return@withLock

        val session = cameraDataSource.createSession()
        appState.cameraSessionFlow.value = session
    }

    suspend fun releaseSession() = sessionMutex.withLock {
        val session = appState.cameraSessionFlow.value ?: return@withLock
        cameraDataSource.releaseSession(session)
        appState.cameraSessionFlow.value = null
        appState.cameraStateFlow.value = CameraState.DEFAULT
    }
}
