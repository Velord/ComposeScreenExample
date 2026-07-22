package com.velord.composescreenexample

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.velord.data.os.memory.MemoryLeakMonitor
import org.koin.android.ext.android.inject

class App : Application() {

    private val memoryLeakMonitor: MemoryLeakMonitor by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initStrictMode()
        initMemoryLeakMonitor()
    }

    private fun initKoin() {
        startKoin()
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
