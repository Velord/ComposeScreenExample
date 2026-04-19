package com.velord.db

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

internal actual fun appDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("user.home"), ".velord/velord.db")
    dbFile.parentFile?.mkdirs()
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
}
