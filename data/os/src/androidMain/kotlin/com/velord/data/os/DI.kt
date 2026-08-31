@file:Suppress("MatchingDeclarationName")

package com.velord.data.os

import com.velord.data.os.camera.AndroidCameraControllerFactory
import com.velord.data.os.camera.CameraControllerFactory
import com.velord.data.os.file.AndroidFileDataSource
import com.velord.data.os.file.FileDataSource
import com.velord.data.os.memory.AndroidMemoryLogger
import com.velord.data.os.memory.MemoryDumpProvider
import com.velord.data.os.memory.MemoryDumpProviderImpl
import com.velord.data.os.memory.MemoryLeakMonitor
import com.velord.data.os.memory.MemoryLeakMonitorImpl
import com.velord.data.os.memory.MemoryLogger
import com.velord.data.os.share.AndroidShareDataSource
import com.velord.data.os.share.ShareDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class CameraPlatformModule {

    @Single
    actual fun provideCameraControllerFactory(
        scope: Scope
    ): CameraControllerFactory = AndroidCameraControllerFactory(scope.get())
}

@Module
actual class FilePlatformModule {

    @Single
    actual fun provideFileDataSource(
        scope: Scope,
    ): FileDataSource = AndroidFileDataSource(scope.get())
}

@Module
actual class MemoryLoggerPlatformModule {

    @Single
    internal actual fun provideMemoryLogger(
        scope: Scope
    ): MemoryLogger = AndroidMemoryLogger(scope.get())

    @Single
    internal actual fun provideMemoryDumpProvider(
        scope: Scope,
        memoryLogger: MemoryLogger
    ): MemoryDumpProvider = MemoryDumpProviderImpl(memoryLogger)

    @Single
    internal actual fun provideMemoryLeakMonitor(
        scope: Scope,
        memoryLogger: MemoryLogger,
        memoryDumpProvider: MemoryDumpProvider
    ): MemoryLeakMonitor = MemoryLeakMonitorImpl(memoryLogger, memoryDumpProvider)
}

@Module
actual class SharePlatformModule {

    @Single
    actual fun provideShareDataSource(scope: Scope): ShareDataSource =
        AndroidShareDataSource(scope.get())
}
