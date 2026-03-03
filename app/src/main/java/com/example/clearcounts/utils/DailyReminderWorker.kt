package com.example.clearcounts.utils

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.clearcounts.data.repository.notificacion.NotificationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationRepository: NotificationRepository
) : CoroutineWorker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        Log.d("WorkManager", "Worker iniciado")
        return try {
            val titulo = inputData.getString("titulo") ?: "¡No olvides registrar tus movimientos!"
            val mensaje = inputData.getString("mensaje") ?: "Lleva un control de tus gastos e ingresos de hoy."

            Log.d("WorkManager", "Titulo: $titulo")
            NotificationHelper.showNotification(applicationContext, titulo, mensaje)
            notificationRepository.insertNotification(titulo, mensaje)
            Log.d("WorkManager", "Worker completado")
            Result.success()
        } catch (e: Exception) {
            Log.e("WorkManager", "Error: ${e.message}")
            Log.e("WorkManager", "StackTrace: ${e.stackTraceToString()}")
            Result.failure()
        }
    }
}