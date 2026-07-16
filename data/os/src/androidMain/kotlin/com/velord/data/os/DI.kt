@file:Suppress("MatchingDeclarationName")

package com.velord.data.os

import com.velord.data.os.camera.AndroidCameraControllerFactory
import com.velord.data.os.camera.CameraControllerFactory
import com.velord.data.os.file.AndroidFileDataSource
import com.velord.data.os.file.FileDataSource
import com.velord.data.os.share.AndroidShareDataSource
import com.velord.data.os.share.ShareDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class CameraPlatformModule {
    @Single
    actual fun provideCameraControllerFactory(scope: Scope): CameraControllerFactory =
        AndroidCameraControllerFactory(scope.get())
}

@Module
actual class FilePlatformModule {
    @Single
    actual fun provideFileDataSource(
        scope: Scope,
    ): FileDataSource = AndroidFileDataSource(scope.get())
}

@Module
actual class SharePlatformModule {
    @Single
    actual fun provideShareDataSource(scope: Scope): ShareDataSource =
        AndroidShareDataSource(scope.get())
}
