package com.velord.composescreenexample

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.velord.config.BuildConfigResolver
import com.velord.config.NavigationLib
import com.velord.di.startKoin
import com.velord.navigation.voyager.initVoyager
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.velord.composescreenexample")
class AppModule

@Single
class BuildConfigResolverImpl: BuildConfigResolver {
    override fun getNavigationLib(): NavigationLib = BuildConfig.NAVIGATION_LIB
}

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initVoyager()
        initKoin()
        initStrictMode()
    }

    private fun initKoin() {
        val moduleRoster = AppModule().module
        startKoin(moduleRoster)
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyFlashScreen()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                // .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}