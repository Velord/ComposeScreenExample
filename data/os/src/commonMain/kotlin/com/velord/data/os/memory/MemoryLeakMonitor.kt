package com.velord.data.os.memory

import org.koin.core.annotation.Single

@Single
expect class MemoryLeakMonitor(logger: MemoryLogger) {
    fun start()
}
