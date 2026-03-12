package com.velord.os

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.minutes

@Single
class MemoryLeakMonitor(
    private val context: Context,
    private val logger: MemoryLogger,
) : ComponentCallbacks2 {

    private val memoryDumpProvider = MemoryDumpProvider(logger)
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        logger.log("MemoryLeakMonitor coroutine error: ${exception.message}")
    }
    private val monitorScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default + exceptionHandler
    )

    fun start() {
        context.registerComponentCallbacks(this)

        setupIntervalLogging()
        setupOomHandler()
    }

    private fun setupIntervalLogging() {
        monitorScope.launch {
            while (isActive) {
                val snapshot = memoryDumpProvider.takeDump(DumpReason.INTERVAL)
                publishMemoryDump(snapshot)
                delay(LOGGING_INTERVAL)
            }
        }
    }

    private fun setupOomHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            if (exception is OutOfMemoryError) {
                try {
                    val snapshot = memoryDumpProvider.takeDump(DumpReason.OOM)
                    publishMemoryDump(snapshot)
                    logger.recordException(exception)
                } catch (e: Exception) {
                    logger.log("Failed to capture OOM snapshot: ${e.message}")
                }
            }
            defaultHandler?.uncaughtException(thread, exception)
        }
    }

    override fun onTrimMemory(level: Int) {
        val snapshot = memoryDumpProvider.takeDump(
            reason = DumpReason.TRIM_MEMORY,
            trimLevel = level
        )
        publishMemoryDump(snapshot)
    }

    override fun onConfigurationChanged(newConfig: Configuration) = Unit

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() = Unit // No-op

    private fun publishMemoryDump(dump: MemoryDump) {
        logger.log("$dump")

        logger.apply {
            with(dump) {
                setCustomKey(KEY_MEM_REASON, reason.name)
                trimLevel?.let { setCustomKey(KEY_MEM_TRIM_LEVEL, it) }
                setCustomKey(KEY_TIMESTAMP, timestamp)

                setCustomKey(KEY_MEM_RSS_MB, dump.snapshot.core.rss)
                setCustomKey(KEY_MEM_SHARED_MB, dump.snapshot.core.shared)
                setCustomKey(KEY_MEM_SWAP_MB, dump.snapshot.core.swap)
                setCustomKey(KEY_MEM_PAGE_TABLE_MB, dump.snapshot.core.pageTable)
                setCustomKey(KEY_MEM_USS_MB, dump.snapshot.core.uss)

                setCustomKey(KEY_MEM_PSS_TOTAL_MB, dump.snapshot.pss.total)
                setCustomKey(KEY_MEM_PSS_JAVA_MB, dump.snapshot.pss.javaHeap)
                setCustomKey(KEY_MEM_PSS_NATIVE_MB, dump.snapshot.pss.nativeHeap)
                setCustomKey(KEY_MEM_PSS_GRAPHICS_MB, dump.snapshot.pss.graphics)
                setCustomKey(KEY_MEM_PSS_CODE_MB, dump.snapshot.pss.code)
                setCustomKey(KEY_MEM_PSS_STACK_MB, dump.snapshot.pss.stack)
                setCustomKey(KEY_MEM_PSS_SYSTEM_MB, dump.snapshot.pss.system)
                setCustomKey(KEY_MEM_PSS_OTHER_MB, dump.snapshot.pss.other)
            }
        }
    }

    companion object {
        private val LOGGING_INTERVAL = 5.minutes

        private const val KEY_MEM_REASON = "mem_reason"
        private const val KEY_MEM_TRIM_LEVEL = "mem_trim_level"
        private const val KEY_TIMESTAMP = "mem_timestamp"
        private const val KEY_MEM_RSS_MB = "mem_rss_mb"
        private const val KEY_MEM_SHARED_MB = "mem_shared_mb"
        private const val KEY_MEM_SWAP_MB = "mem_swap_mb"
        private const val KEY_MEM_PAGE_TABLE_MB = "mem_page_table_mb"
        private const val KEY_MEM_USS_MB = "mem_uss_mb"
        private const val KEY_MEM_PSS_TOTAL_MB = "mem_pss_total_mb"
        private const val KEY_MEM_PSS_JAVA_MB = "mem_pss_java_mb"
        private const val KEY_MEM_PSS_NATIVE_MB = "mem_pss_native_mb"
        private const val KEY_MEM_PSS_GRAPHICS_MB = "mem_pss_graphics_mb"
        private const val KEY_MEM_PSS_CODE_MB = "mem_pss_code_mb"
        private const val KEY_MEM_PSS_STACK_MB = "mem_pss_stack_mb"
        private const val KEY_MEM_PSS_SYSTEM_MB = "mem_pss_system_mb"
        private const val KEY_MEM_PSS_OTHER_MB = "mem_pss_other_mb"
    }
}
