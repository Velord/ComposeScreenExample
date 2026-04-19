@file:Suppress("MatchingDeclarationName")

package com.velord.gateway

import com.velord.appstate.AppStateDataSource
import com.velord.appstate.AppStateDataSourceImpl
import com.velord.backend.ktor.BackendModule
import com.velord.datastore.DataStoreModule
import com.velord.db.DbModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        DbModule::class,
        BackendModule::class,
        DataStoreModule::class,
    ],
)
@ComponentScan("com.velord.gateway")
class GatewayModule {

    @Single
    fun appStateDataSource(): AppStateDataSource = AppStateDataSourceImpl()
}
