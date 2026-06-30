@file:Suppress("MatchingDeclarationName")

package com.velord.os

import com.velord.os.camera.CameraPlatformModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CameraPlatformModule::class
    ],
)
@ComponentScan("com.velord.os")
class OsModule
