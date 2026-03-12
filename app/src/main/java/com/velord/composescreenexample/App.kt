package com.velord.composescreenexample

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.velord.config.BuildConfigResolver
import com.velord.config.NavigationLib
import com.velord.di.startKoin
import com.velord.navigation.voyager.initVoyager
import com.velord.os.MemoryLeakMonitor
import com.velord.os.MemoryLogger
import org.koin.android.ext.android.inject
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

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(message: String, isError: Boolean) {
        if (isError) {
            Log.e(TAG, message)
            crashlytics.log("E/$TAG: $message")
        } else {
            Log.d(TAG, message)
            crashlytics.log("D/$TAG: $message")
        }
    }

    override fun recordException(throwable: Throwable) {
        Log.e(TAG, "Recording non-fatal exception", throwable)
        crashlytics.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: Int) {
        Log.d(TAG, "Key [$key] = $value")
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Long) {
        Log.d(TAG, "Key [$key] = $value")
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: String) {
        Log.d(TAG, "Key [$key] = $value")
        crashlytics.setCustomKey(key, value)
    }

    companion object {
        private const val TAG = "MemoryLogger"
    }
}

class App : Application() {

    private val memoryLeakMonitor: MemoryLeakMonitor by inject()

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