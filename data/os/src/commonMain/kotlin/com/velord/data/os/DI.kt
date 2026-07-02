@file:Suppress("MatchingDeclarationName")

package com.velord.data.os

import com.velord.data.os.camera.CameraPlatformModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CameraPlatformModule::class
    ],
)
@ComponentScan("com.velord.data.os")
class OsModule
