package com.velord.os

expect class MemoryLeakMonitor(logger: MemoryLogger) {
    fun start()
}
