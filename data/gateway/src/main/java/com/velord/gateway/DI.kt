@file:Suppress("MatchingDeclarationName")

package com.velord.gateway

import com.velord.appstate.AppStateModule
import com.velord.backend.ktor.BackendModule
import com.velord.datastore.DataStoreModule
import com.velord.db.DbModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        AppStateModule::class,
        DbModule::class,
        BackendModule::class,
        DataStoreModule::class,
    ],
)
@ComponentScan("com.velord.gateway")
class GatewayModule
