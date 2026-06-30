package com.velord.os.memory

internal expect class MemoryDumpProvider(logger: MemoryLogger) {
    fun takeDump(reason: DumpReason, trimLevel: Int? = null): MemoryDump
}
