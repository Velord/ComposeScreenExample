package com.velord.data.db

import androidx.room.RoomDatabase

internal expect fun appDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
