package com.example.clearcounts.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session_prefs")

@Singleton
class UserSessionDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    // Clave para guardar el ID del usuario. -1 indica que nadie está logueado.
    private val USER_ID_KEY = intPreferencesKey("logged_in_user_id")

    // Expone el ID del usuario actualmente logueado como un Flow
    val loggedInUserIdFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            // Si la clave no existe, devuelve -1 (no logueado)
            preferences[USER_ID_KEY] ?: -1
        }

    // Función para guardar el ID al iniciar sesión
    suspend fun setLoggedInUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Función para limpiar el ID al cerrar sesión
    suspend fun clearLoggedInUserId() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }
}