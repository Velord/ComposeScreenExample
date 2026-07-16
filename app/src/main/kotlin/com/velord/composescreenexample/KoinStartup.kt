package com.velord.composescreenexample

import android.app.Application
import com.velord.infrastructure.di.createCommonAppModuleRoster
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

internal fun Application.startKoin() {
    val app = this
    val moduleRoster = createCommonAppModuleRoster() + AppModule().module

    startKoin {
        androidLogger()
        androidContext(app)
        modules(moduleRoster)
    }
}