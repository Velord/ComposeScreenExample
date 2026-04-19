package com.velord.os

internal expect class MemoryDumpProvider(logger: MemoryLogger) {
    fun takeDump(reason: DumpReason, trimLevel: Int? = null): MemoryDump
}
