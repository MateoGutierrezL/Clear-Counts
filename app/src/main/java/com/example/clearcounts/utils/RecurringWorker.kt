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

            recurringRepository.getAllRecurring(userId)  // <- pasa userId
                .first()
                .filter { recurrente ->
                    if (!recurrente.activo) return@filter false

                    val ultimaEjecucion = recurrente.ultimaEjecucion
                    if (ultimaEjecucion != null) {
                        val mesAnioUltima = ultimaEjecucion.substring(3)
                        if (mesAnioUltima == mesAnioHoy) return@filter false
                    }

                    hoy.dayOfMonth >= recurrente.diaDelMes
                }
                .forEach { recurrente ->
                    if (recurrente.tipo == "ingreso") {
                        incomeRepository.insertIncome(
                            IncomeEntity(
                                userId = userId,  // <- agrega userId
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
                                userId = userId,  // <- agrega userId
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
}