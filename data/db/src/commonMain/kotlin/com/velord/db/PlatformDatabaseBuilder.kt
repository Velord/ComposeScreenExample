package com.velord.db

import androidx.room.RoomDatabase

internal expect fun appDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
