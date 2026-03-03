package com.example.clearcounts.data.repository.notificacion

import com.example.clearcounts.data.database.dao.NotificationDao
import com.example.clearcounts.data.database.entities.NotificationEntity
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class OfflineNotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getAllNotifications() = notificationDao.getAllNotifications()

    override suspend fun insertNotification(titulo: String, mensaje: String) {
        val now = LocalDateTime.now()
        notificationDao.insertNotification(
            NotificationEntity(
                titulo = titulo,
                mensaje = mensaje,
                fecha = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                hora = now.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
            )
        )
    }

    override suspend fun marcarTodasLeidas() {
        notificationDao.marcarTodasLeidas()
    }

    override suspend fun deleteAllNotifications() = notificationDao.deleteAllNotifications()
}