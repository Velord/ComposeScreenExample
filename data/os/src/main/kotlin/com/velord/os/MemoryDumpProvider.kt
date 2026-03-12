package com.velord.os

import android.os.Debug
import android.system.Os
import android.system.OsConstants
import org.koin.core.annotation.Single
import java.io.BufferedReader
import java.io.FileReader
import kotlin.math.roundToLong

@Single
internal class MemoryDumpProvider(private val logger: MemoryLogger) {

    private val memoryInfo = Debug.MemoryInfo()
    private val pageSize by lazy { Os.sysconf(OsConstants._SC_PAGESIZE) }

    fun takeDump(reason: DumpReason, trimLevel: Int? = null): MemoryDump {
        Debug.getMemoryInfo(memoryInfo)

        val (rss, shared) = getRssAndShared()
        val (swap, pageTable) = getSwapAndPageTable()

        val core = CoreOsMetric(
            rss = rss,
            shared = shared,
            swap = swap,
            pageTable = pageTable,
            uss = rss - shared
        )
        val pss = PssBreakdown(
            total = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_TOTAL).toLongCompat().kbToMb(),
            javaHeap = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_JAVA_HEAP).toLongCompat().kbToMb(),
            nativeHeap = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_NATIVE_HEAP).toLongCompat().kbToMb(),
            graphics = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_GRAPHICS).toLongCompat().kbToMb(),
            code = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_CODE).toLongCompat().kbToMb(),
            stack = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_STACK).toLongCompat().kbToMb(),
            system = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_SYSTEM).toLongCompat().kbToMb(),
            other = memoryInfo.getMemoryStat(PssBreakdown.KEY_PSS_OTHER).toLongCompat().kbToMb()
        )
        val snapshot =  MemorySnapshot(core = core, pss = pss)
        val dump = MemoryDump(
            reason = reason,
            trimLevel = trimLevel,
            timestamp = System.currentTimeMillis(),
            snapshot = snapshot
        )
        return dump
    }

    private fun getRssAndShared(): Pair<Long, Long> = try {
        BufferedReader(FileReader("/proc/self/statm")).use { reader ->
            val line = reader.readLine() ?: return 0L to 0L
            val parts = line.split(" ")
            val rss = (parts.getOrNull(1)?.toLongOrNull() ?: 0L) * pageSize
            val shared = (parts.getOrNull(2)?.toLongOrNull() ?: 0L) * pageSize
            rss.bytesToMb() to shared.bytesToMb()
        }
    } catch (e: Exception) {
        logger.log("Error reading /proc/self/statm: ${e.message}")
        0L to 0L
    }

    private fun getSwapAndPageTable(): Pair<Long, Long> {
        var swap = 0L
        var pageTable = 0L
        try {
            BufferedReader(FileReader("/proc/self/status")).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    if (line!!.startsWith("VmSwap:")) {
                        swap = parseValue(line)
                    } else if (line.startsWith("VmPTE:")) {
                        pageTable = parseValue(line)
                    }
                    if (swap > 0 && pageTable > 0) break
                }
            }
        } catch (e: Exception) {
            logger.log("Error reading /proc/self/status: ${e.message}")
        }
        return swap.kbToMb() to pageTable.kbToMb()
    }

    private fun parseValue(line: String): Long =
        line.replace(DIGIT_REGEX, "").toLongOrNull() ?: 0L


    private fun String?.toLongCompat(): Long = this?.toLongOrNull() ?: 0L
    private fun Long.bytesToMb(): Long = (this.toDouble() / BYTES_IN_MB).roundToLong()
    private fun Long.kbToMb(): Long = (this.toDouble() / KB_IN_MB).roundToLong()

    companion object {
        private val DIGIT_REGEX = Regex("[^0-9]")
        private const val KB_IN_MB = 1024.0
        private const val BYTES_IN_MB = 1048576.0 // 1024 * 1024
    }
}
