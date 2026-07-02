package com.velord.infrastructure.di

import android.app.Application
import com.velord.data.backend.ktor.httpModule
import com.velord.data.db.databaseModule
import com.velord.data.gateway.GatewayModule
import com.velord.ui.feature.bottomnavigation.BottomNavigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.ksp.generated.module

fun Application.startKoin(vararg moduleToInstall: Module) {
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
        //modules(OsModule().module)
    }
}
