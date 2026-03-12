package com.velord.os

internal data class MemoryDump(
    val reason: DumpReason,
    val trimLevel: Int? = null,
    val timestamp: Long,
    val snapshot: MemorySnapshot
) {
    override fun toString(): String {
        val trimInfo = trimLevel?.let { " (Level: $it)" } ?: ""
        return "[$reason]$trimInfo PSS: ${snapshot.pss.total}MB" +
                " | Java: ${snapshot.pss.javaHeap}MB" +
                " | USS: ${snapshot.core.uss}MB" +
                " | RSS: ${snapshot.core.rss}MB"
    }
}

internal enum class DumpReason {
    INTERVAL, TRIM_MEMORY, OOM
}

internal data class CoreOsMetric(
    val rss: Long,
    val shared: Long,
    val swap: Long,
    val pageTable: Long,
    // Calculated as RSS - Shared
    val uss: Long,
)

internal data class PssBreakdown(
    val total: Long,
    val javaHeap: Long,
    val nativeHeap: Long,
    val graphics: Long,
    val code: Long,
    val stack: Long,
    val system: Long,
    val other: Long
) {
    companion object {
        const val KEY_PSS_TOTAL = "summary.total-pss"
        const val KEY_PSS_JAVA_HEAP = "summary.java-heap"
        const val KEY_PSS_NATIVE_HEAP = "summary.native-heap"
        const val KEY_PSS_GRAPHICS = "summary.graphics"
        const val KEY_PSS_CODE = "summary.code"
        const val KEY_PSS_STACK = "summary.stack"
        const val KEY_PSS_SYSTEM = "summary.system"
        const val KEY_PSS_OTHER = "summary.private-other"
    }
}

internal data class MemorySnapshot(
    val core: CoreOsMetric,
    val pss: PssBreakdown,
)