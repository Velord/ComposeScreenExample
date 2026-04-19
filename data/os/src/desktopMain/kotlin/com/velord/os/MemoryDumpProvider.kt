package com.velord.os

internal actual class MemoryDumpProvider actual constructor(
    private val logger: MemoryLogger,
) {
    actual fun takeDump(reason: DumpReason, trimLevel: Int?): MemoryDump {
        logger.log("Memory dump is not implemented for desktop target.")
        return MemoryDump(
            reason = reason,
            trimLevel = trimLevel,
            timestamp = System.currentTimeMillis(),
            snapshot = MemorySnapshot(
                core = CoreOsMetric(
                    rss = 0L,
                    shared = 0L,
                    swap = 0L,
                    pageTable = 0L,
                    uss = 0L,
                ),
                pss = PssBreakdown(
                    total = 0L,
                    javaHeap = 0L,
                    nativeHeap = 0L,
                    graphics = 0L,
                    code = 0L,
                    stack = 0L,
                    system = 0L,
                    other = 0L,
                ),
            ),
        )
    }
}
