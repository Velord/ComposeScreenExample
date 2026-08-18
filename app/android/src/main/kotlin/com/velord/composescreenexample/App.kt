package com.velord.composescreenexample

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.velord.data.os.memory.MemoryLeakMonitor
import com.velord.usecase.setting.FetchLocalizationUC
import com.velord.usecase.setting.InitializeLocalizationUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class App : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val memoryLeakMonitor: MemoryLeakMonitor by inject()
    private val initializeLocalizationUC: InitializeLocalizationUC by inject()
    private val fetchLocalizationUC: FetchLocalizationUC by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initLocalization()
        initStrictMode()
        initMemoryLeakMonitor()
    }

    private fun initKoin() {
        startKoin()
    }

    private fun initLocalization() {
        runBlocking {
            initializeLocalizationUC()
        }
        appScope.launch {
            fetchLocalizationUC()
        }
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
