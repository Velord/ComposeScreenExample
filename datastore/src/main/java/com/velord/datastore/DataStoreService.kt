package com.velord.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface DataStoreService {
    suspend fun checkAppFirstLaunch(): Boolean
}

private object Keys {
    val FIRST_LAUNCH = booleanPreferencesKey("compose_screen_example.first_launch")
}

class DataStoreServiceImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreService {

    private suspend fun setFirstLaunch() {
        dataStore.edit { preferences ->
            preferences[Keys.FIRST_LAUNCH] = false
        }
    }

    override suspend fun checkAppFirstLaunch(): Boolean = dataStore.data.map { preferences ->
        val isFirstLaunch = (preferences[Keys.FIRST_LAUNCH] ?: true)
        if (isFirstLaunch) setFirstLaunch()

        isFirstLaunch
    }.first()
}