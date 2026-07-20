package com.velord.composescreenexample

import android.app.Application
import com.velord.infrastructure.di.createCommonAppModuleRoster
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

internal fun Application.startKoin() {
    val app = this
    val moduleRoster = createCommonAppModuleRoster()

    startKoin {
        androidLogger()
        androidContext(app)
        modules(moduleRoster)
    }
}
