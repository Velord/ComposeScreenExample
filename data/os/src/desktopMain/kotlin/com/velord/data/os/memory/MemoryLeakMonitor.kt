package com.velord.data.os.memory

import org.koin.core.annotation.Single

@Single
actual class MemoryLeakMonitor actual constructor(private val logger: MemoryLogger) {

    actual fun start() {
        logger.log("Memory leak monitor is not implemented for desktop target.")
    }
}
