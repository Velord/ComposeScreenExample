@file:Suppress("MatchingDeclarationName")

package com.velord.data.os

import com.velord.data.os.camera.CameraControllerFactory
import com.velord.data.os.file.FileDataSource
import com.velord.data.os.memory.MemoryDumpProvider
import com.velord.data.os.memory.MemoryLeakMonitor
import com.velord.data.os.memory.MemoryLogger
import com.velord.data.os.share.ShareDataSource
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module(
    includes = [
        CameraPlatformModule::class,
        FilePlatformModule::class,
        MemoryLoggerPlatformModule::class,
        SharePlatformModule::class,
    ],
)
@ComponentScan("com.velord.data.os")
class OsModule

@Module
expect class CameraPlatformModule() {

    @Single
    fun provideCameraControllerFactory(scope: Scope): CameraControllerFactory
}

@Module
expect class FilePlatformModule() {

    @Single
    fun provideFileDataSource(scope: Scope): FileDataSource
}

@Module
expect class MemoryLoggerPlatformModule() {

    @Single
    internal fun provideMemoryLogger(scope: Scope): MemoryLogger

    @Single
    internal fun provideMemoryDumpProvider(
        scope: Scope,
        memoryLogger: MemoryLogger
    ): MemoryDumpProvider

    @Single
    internal fun provideMemoryLeakMonitor(
        scope: Scope,
        memoryLogger: MemoryLogger,
        memoryDumpProvider: MemoryDumpProvider,
    ): MemoryLeakMonitor
}

@Module
expect class SharePlatformModule() {

    @Single
    fun provideShareDataSource(scope: Scope): ShareDataSource
}
