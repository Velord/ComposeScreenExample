package com.velord.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.context.GlobalContext

internal actual fun appDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext: Context = GlobalContext.get().get()
    val dbFile = appContext.getDatabasePath("velord.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
