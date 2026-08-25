@file:Suppress("MatchingDeclarationName")

package com.velord.data.gateway

import com.velord.data.appstate.AppStateModule
import com.velord.data.backend.ktor.BackendModule
import com.velord.data.datastore.DataStoreModule
import com.velord.data.db.DbModule
import com.velord.data.localization.LocalizationModule
import com.velord.data.os.OsModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        AppStateModule::class,
        DbModule::class,
        BackendModule::class,
        DataStoreModule::class,
        LocalizationModule::class,
        OsModule::class,
    ],
)
@ComponentScan("com.velord.data.gateway")
class GatewayModule
