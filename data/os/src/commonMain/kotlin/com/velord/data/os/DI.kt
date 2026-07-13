@file:Suppress("MatchingDeclarationName")

package com.velord.data.os

import com.velord.data.os.camera.CameraPlatformModule
import com.velord.data.os.share.SharePlatformModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [CameraPlatformModule::class, SharePlatformModule::class])
@ComponentScan("com.velord.data.os")
class OsModule
