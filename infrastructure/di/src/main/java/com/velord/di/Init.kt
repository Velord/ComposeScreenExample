package com.velord.di

import android.app.Application
import com.velord.appstate.AppStateModule
import com.velord.backend.ktor.BackendModule
import com.velord.backend.ktor.httpModule
import com.velord.bottomnavigation.BottomNavigationModule
import com.velord.datastore.DataStoreModule
import com.velord.db.DbModule
import com.velord.db.databaseModule
import com.velord.gateway.GatewayModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.ksp.generated.module

fun Application.startKoin(
    vararg moduleToInstall: Module
) {
    val app = this
    startKoin {
        androidLogger()
        androidContext(app)

        modules(*moduleToInstall)
        modules(useCaseModule)
        modules(viewModelModule)
        modules(httpModule)
        modules(databaseModule)
        modules(BottomNavigationModule().module)
        modules(DataStoreModule().module)
        modules(AppStateModule().module)
        modules(BackendModule().module)
        modules(DbModule().module)
        modules(GatewayModule().module)
    }
}