package com.example.clearcounts.utils

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.clearcounts.R
import com.example.clearcounts.data.local.repository.notificacion.NotificationRepository
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
        return try {
            val titulo = resolverString(inputData.getString("titulo_key"))
                ?: ""
            val mensaje = resolverString(inputData.getString("mensaje_key"))
                ?: ""

            if (titulo.isEmpty() || mensaje.isEmpty()) return Result.failure()

            NotificationHelper.showNotification(applicationContext, titulo, mensaje)
            notificationRepository.insertNotification(titulo, mensaje)
            Result.success()
        } catch (e: Exception) {
            Log.e("WorkManager", "Error: ${e.message}")
            Result.failure()
        }
    }

    private fun resolverString(key: String?): String? {
        val resId = when (key) {
            "no_olvides_registrar_tus_movimientos" -> R.string.no_olvides_registrar_tus_movimientos
            "lleva_un_control_de_tus_gastos_e_ingresos_de_hoy" -> R.string.lleva_un_control_de_tus_gastos_e_ingresos_de_hoy
            "c_mo_van_tus_finanzas_hoy" -> R.string.c_mo_van_tus_finanzas_hoy
            "revisa_tu_resumen_del_d_a_en_clearcounts" -> R.string.revisa_tu_resumen_del_d_a_en_clearcounts
            else -> return null
        }
        return applicationContext.getString(resId)
    }
}