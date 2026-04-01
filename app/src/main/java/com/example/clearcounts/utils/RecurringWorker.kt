package com.example.clearcounts.utils

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.local.repository.recurrente.RecurringRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@HiltWorker
class RecurringWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val recurringRepository: RecurringRepository,
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val auth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure()

            val hoy = LocalDate.now()
            val fechaFormateada = hoy.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val horaFormateada = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            val mesAnioHoy = hoy.format(DateTimeFormatter.ofPattern("MM-yyyy"))

            recurringRepository.getAllRecurring(userId)
                .first()
                .filter { recurrente ->
                    if (!recurrente.activo) return@filter false

                    val ultimaEjecucion = recurrente.ultimaEjecucion

                    when (recurrente.frecuencia) {
                        "diario" -> {
                            ultimaEjecucion != fechaFormateada
                        }
                        "semanal" -> {

                            if (hoy.dayOfWeek.value != recurrente.diaReferencia) return@filter false
                            ultimaEjecucion != fechaFormateada
                        }
                        "quincenal" -> {
                            val ultimoDia = hoy.lengthOfMonth()
                            val primerDia = minOf(recurrente.diaReferencia, ultimoDia)
                            val segundoDiaRaw = recurrente.diaReferencia + 15
                            val segundoDia = if (segundoDiaRaw <= 31)
                                minOf(segundoDiaRaw, ultimoDia)
                            else
                                minOf(segundoDiaRaw - 31, ultimoDia)

                            val esHoyDiaValido = hoy.dayOfMonth == primerDia ||
                                    hoy.dayOfMonth == segundoDia
                            if (!esHoyDiaValido) return@filter false
                            ultimaEjecucion != fechaFormateada
                        }
                        "mensual" -> {
                            val ultimoDia = hoy.lengthOfMonth()
                            val diaEfectivo = minOf(recurrente.diaReferencia, ultimoDia)
                            if (hoy.dayOfMonth != diaEfectivo) return@filter false
                            if (ultimaEjecucion != null) {
                                val mesAnioUltima = ultimaEjecucion.substring(3)
                                if (mesAnioUltima == mesAnioHoy) return@filter false
                            }
                            true
                        }
                        else -> false
                    }
                }
                .forEach { recurrente ->
                    if (recurrente.tipo == "ingreso") {
                        incomeRepository.insertIncome(
                            IncomeEntity(
                                userId = userId,
                                categoria = recurrente.categoria,
                                cantidad = recurrente.cantidad,
                                hora = horaFormateada,
                                fecha = fechaFormateada,
                                nota = recurrente.nombre,
                                metodoPago = recurrente.metodoPago
                            )
                        )
                    } else {
                        expenseRepository.insertExpense(
                            ExpenseEntity(
                                userId = userId,
                                categoria = recurrente.categoria,
                                cantidad = recurrente.cantidad,
                                hora = horaFormateada,
                                fecha = fechaFormateada,
                                nota = recurrente.nombre,
                                metodoPago = recurrente.metodoPago
                            )
                        )
                    }

                    recurringRepository.updateRecurring(
                        recurrente.copy(ultimaEjecucion = fechaFormateada)
                    )
                }

            Result.success()
        } catch (e: Exception) {
            Log.e("RecurringWorker", "Error: ${e.message}")
            Result.failure()
        }
    }

    private fun diaEfectivo(diaReferencia: Int, mes: LocalDate): Int {
        val ultimoDiaDelMes = mes.lengthOfMonth()
        return minOf(diaReferencia, ultimoDiaDelMes)
    }
}