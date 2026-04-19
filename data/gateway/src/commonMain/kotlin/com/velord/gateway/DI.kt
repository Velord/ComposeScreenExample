@file:Suppress("MatchingDeclarationName")

package com.velord.gateway

import com.velord.appstate.AppStateModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        AppStateModule::class,
    ],
)
@ComponentScan("com.velord.gateway")
class GatewayCommonModule
