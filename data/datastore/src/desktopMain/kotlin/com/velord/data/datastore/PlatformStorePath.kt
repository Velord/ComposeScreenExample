package com.velord.data.datastore

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

internal actual fun appSettingStorePath(): Path {
    val dir = "${System.getProperty("user.home")}/.velord".toPath()
    FileSystem.SYSTEM.createDirectories(dir)
    return "$dir/setting.json".toPath()
}
