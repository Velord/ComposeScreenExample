package com.velord.composescreenexample

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.velord.config.BuildConfigResolver
import com.velord.config.NavigationLib
import com.velord.di.startKoin
import com.velord.navigation.voyager.initVoyager
import com.velord.os.MemoryLeakMonitor
import com.velord.os.MemoryLogger
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.velord.composescreenexample")
internal class AppModule

@Single
internal class BuildConfigResolverImpl: BuildConfigResolver {
    override fun getNavigationLib(): NavigationLib = BuildConfig.NAVIGATION_LIB
}

@Single
internal class MemoryLoggerImpl : MemoryLogger {

    private val tag = "MemoryMonitor"

    override fun log(message: String, isError: Boolean) {
        if (isError) {
            Log.e(tag, message)
            // FirebaseCrashlytics.getInstance().log("E/$tag: $message")
        } else {
            Log.d(tag, message)
            // FirebaseCrashlytics.getInstance().log("D/$tag: $message")
        }
    }

    override fun recordException(throwable: Throwable) {
        Log.e(tag, "Recording non-fatal exception", throwable)
        // FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    override fun setCustomKey(key: String, value: Int) {
        Log.d(tag, "Key [$key] = $value")
        // FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Long) {
        Log.d(tag, "Key [$key] = $value")
        // FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: String) {
        Log.d(tag, "Key [$key] = $value")
        // FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }
}

class App : Application() {

    private val memoryLogger: MemoryLogger by lazy {
        MemoryLoggerImpl()
    }

    private val memoryLeakMonitor: MemoryLeakMonitor by lazy {
        MemoryLeakMonitor(this, memoryLogger)
    }

    override fun onCreate() {
        super.onCreate()

        initVoyager()
        initKoin()
        initStrictMode()
        initMemoryLeakMonitor()
    }

    private fun initKoin() {
        val moduleRoster = AppModule().module
        startKoin(moduleRoster)
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                //.detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyFlashScreen()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }

    private fun initMemoryLeakMonitor() {
        memoryLeakMonitor.start()
    }
}