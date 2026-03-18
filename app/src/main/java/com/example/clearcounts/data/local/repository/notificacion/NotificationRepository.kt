package com.example.clearcounts.data.local.repository.notificacion

import com.example.clearcounts.data.local.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    suspend fun insertNotification(titulo: String, mensaje: String)
    suspend fun deleteAllNotifications()

    suspend fun marcarTodasLeidas()
}