package com.example.clearcounts.ui.screens.Notificaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.database.entities.NotificationEntity
import com.example.clearcounts.data.repository.notificacion.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificacionesViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val notificaciones: StateFlow<List<NotificationEntity>> = notificationRepository
        .getAllNotifications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val tieneNoLeidas: StateFlow<Boolean> = notificaciones.map { lista ->
        lista.any { !it.leida }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun marcarTodasLeidas() {
        viewModelScope.launch {
            notificationRepository.marcarTodasLeidas()
        }
    }

    fun insertNotificacion(titulo: String, mensaje: String) {
        viewModelScope.launch {
            notificationRepository.insertNotification(titulo, mensaje)
        }
    }

    fun deleteAllNotificaciones() {
        viewModelScope.launch {
            notificationRepository.deleteAllNotifications()
        }
    }
}