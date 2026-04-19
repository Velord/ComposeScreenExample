@file:Suppress("MatchingDeclarationName")

package com.velord.gateway

import com.velord.backend.ktor.BackendModule
import com.velord.datastore.DataStoreModule
import com.velord.db.DbModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.velord.gateway.movie.android")
class GatewayAndroidMovieModule

@Module
@ComponentScan("com.velord.gateway.setting.android")
class GatewayAndroidSettingModule

@Module(
    includes = [
        GatewayCommonModule::class,
        GatewayAndroidMovieModule::class,
        GatewayAndroidSettingModule::class,
        DbModule::class,
        BackendModule::class,
        DataStoreModule::class,
    ],
)
class GatewayModule
