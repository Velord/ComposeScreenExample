package com.velord.datastore

import android.content.Context
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.context.GlobalContext

internal actual fun appSettingStorePath(): Path {
    val appContext: Context = GlobalContext.get().get()
    return "${appContext.filesDir.absolutePath}/setting.json".toPath()
}
