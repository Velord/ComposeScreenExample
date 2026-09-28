package com.velord.data.os.memory

internal interface MemoryDumpProvider {
    fun takeDump(reason: DumpReason, trimLevel: Int? = null): MemoryDump
}
