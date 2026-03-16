package com.example.clearcounts.utils

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.repository.recurrente.RecurringRepository
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
    private val expenseRepository: ExpenseRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val hoy = LocalDate.now()
            val fechaFormateada = hoy.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val horaFormateada = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            val mesAnioHoy = hoy.format(DateTimeFormatter.ofPattern("MM-yyyy"))

            recurringRepository.getAllRecurring()
                .first()
                .filter { recurrente ->
                    if (!recurrente.activo) return@filter false

                    val ultimaEjecucion = recurrente.ultimaEjecucion
                    if (ultimaEjecucion != null) {
                        val mesAnioUltima = ultimaEjecucion.substring(3) // extrae "MM-yyyy"
                        if (mesAnioUltima == mesAnioHoy) return@filter false // ya se ejecutó este mes
                    }

                    hoy.dayOfMonth >= recurrente.diaDelMes
                }
                .forEach { recurrente ->
                    if (recurrente.tipo == "ingreso") {
                        incomeRepository.insertIncome(
                            IncomeEntity(
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