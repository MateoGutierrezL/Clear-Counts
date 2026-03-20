package com.example.clearcounts.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class SyncDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val ULTIMA_SYNC = longPreferencesKey("ultima_sync")
        private val Context.dataStore by preferencesDataStore(name = "sync_prefs")
    }

    suspend fun guardarUltimaSync() {
        dataStore.edit { prefs ->
            prefs[ULTIMA_SYNC] = System.currentTimeMillis()
        }
    }

    suspend fun getUltimaSync(): Long {
        return dataStore.data.first()[ULTIMA_SYNC] ?: 0L
    }

    suspend fun resetUltimaSync() {
        context.dataStore.edit { prefs ->
            prefs[ULTIMA_SYNC] = 0L
        }
    }
}