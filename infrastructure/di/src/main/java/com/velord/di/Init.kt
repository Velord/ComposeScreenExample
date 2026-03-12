package com.velord.di

import android.app.Application
import com.velord.backend.ktor.httpModule
import com.velord.bottomnavigation.BottomNavigationModule
import com.velord.db.databaseModule
import com.velord.gateway.GatewayModule
import com.velord.os.OsModule
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
        // Load manual DSL modules
        modules(useCaseModule)
        modules(viewModelModule)
        modules(httpModule)
        modules(databaseModule)
        // Load Annotation-based modules
        modules(BottomNavigationModule().module)
        modules(GatewayModule().module)
        modules(OsModule().module)
    }
}