@file:Suppress("MatchingDeclarationName")

package com.velord.data.os

import com.velord.data.os.camera.CameraControllerFactory
import com.velord.data.os.camera.DesktopCameraControllerFactory
import com.velord.data.os.file.DesktopFileDataSource
import com.velord.data.os.file.FileDataSource
import com.velord.data.os.memory.DesktopMemoryLogger
import com.velord.data.os.memory.MemoryLogger
import com.velord.data.os.share.DesktopShareDataSource
import com.velord.data.os.share.ShareDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class CameraPlatformModule {
    @Single
    actual fun provideCameraControllerFactory(
        scope: Scope
    ): CameraControllerFactory = DesktopCameraControllerFactory()
}

@Module
actual class FilePlatformModule {
    @Single
    actual fun provideFileDataSource(scope: Scope): FileDataSource = DesktopFileDataSource()
}

@Module
actual class MemoryLoggerPlatformModule {
    @Single
    actual fun provideMemoryLogger(scope: Scope): MemoryLogger = DesktopMemoryLogger(scope.get())
}

@Module
actual class SharePlatformModule {
    @Single
    actual fun provideShareDataSource(scope: Scope): ShareDataSource = DesktopShareDataSource()
}
