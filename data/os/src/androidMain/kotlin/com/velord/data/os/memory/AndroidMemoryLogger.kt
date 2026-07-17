package com.velord.data.os.memory

import co.touchlab.kermit.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics

class AndroidMemoryLogger : MemoryLogger {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(message: String, isError: Boolean) {
        if (isError) {
            log.e { message }
            crashlytics.log("E/$TAG: $message")
        } else {
            log.d { message }
            crashlytics.log("D/$TAG: $message")
        }
    }

    override fun recordException(throwable: Throwable) {
        log.e(throwable) { "Recording non-fatal exception" }
        crashlytics.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: Int) {
        log.d { "Key [$key] = $value" }
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Long) {
        log.d { "Key [$key] = $value" }
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: String) {
        log.d { "Key [$key] = $value" }
        crashlytics.setCustomKey(key, value)
    }

    companion object {
        private const val TAG = "MemoryLogger"
        private val log = Logger.withTag(TAG)
    }
}
