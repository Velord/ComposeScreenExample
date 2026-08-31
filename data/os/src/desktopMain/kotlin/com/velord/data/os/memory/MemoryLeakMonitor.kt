package com.velord.data.os.memory

internal class MemoryLeakMonitorImpl(private val logger: MemoryLogger): MemoryLeakMonitor {

    override fun start() {
        logger.log("Memory leak monitor is not implemented for desktop target.")
    }
}
