package com.example.clearcounts.ui.screens.Inicio

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel(){

    val movimientosState: StateFlow<List<Any>> = combine(
        incomeRepository.getAllIncomes(),
        expenseRepository.getAllExpenses()
    ) { ingresos, gastos ->

        val listaUnida = ingresos + gastos

       //Estas tablas se ordenan segun la fecha y hora, el ultimo ingreso
        //o gasto en ser registrado va a aparecer en la parte de arriba del inicio
        listaUnida.sortedByDescending { movimiento ->
            when (movimiento) {
                is IncomeEntity -> combinarFechaHora(movimiento.fecha, movimiento.hora)
                is ExpenseEntity -> combinarFechaHora(movimiento.fecha, movimiento.hora)
                else -> LocalDateTime.MIN
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun combinarFechaHora(fecha: String, hora: String): LocalDateTime {
        return try {
            val formatterFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")

            val formatterHora = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

            val fechaParsed = LocalDate.parse(fecha, formatterFecha)
            val horaParsed = LocalTime.parse(hora, formatterHora)

            LocalDateTime.of(fechaParsed, horaParsed)
        } catch (e: Exception) {
            // Si hay un error, lo mandamos al final de la lista para no romper la app
            LocalDateTime.MIN
        }
    }

    fun deleteIncome(incomeEntity: IncomeEntity){

        viewModelScope.launch {
            try {
                incomeRepository.deleteIncome(incomeEntity = incomeEntity)
            }catch (e: Exception){
                Log.e("ingreso", "fallo al eliminar el ingreso: ${e.message}")
            }
        }
    }

    fun deleteExpense(expenseEntity: ExpenseEntity){
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expenseEntity = expenseEntity)
            }catch (e: Exception){
                Log.e("gasto", "fallo al eliminar el gasto: ${e.message}")
            }
        }
    }

    val totalIngresoSum: StateFlow<Double> = incomeRepository.totalIncome()
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val totalGastoSum: StateFlow<Double> = expenseRepository.totalExpense()
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
    // En tu InicioViewModel
    private val dbFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    // Formato: "feb. 20" (MMM para mes abreviado, dd para día)
    private val axisFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale("es", "ES"))

    val chartDataState = movimientosState.map { movimientos ->
        val hoy = LocalDate.now()
        val points = mutableListOf<Point>()
        val labelsX = mutableListOf<String>()

        // Generar últimos 7 días de más antiguo a más reciente
        val ultimos7Dias = (0..6).map { hoy.minusDays(it.toLong()) }.reversed()

        ultimos7Dias.forEachIndexed { index, fecha ->
            val fechaStringDB = fecha.format(dbFormatter)

            // Balance Neto: Suma ingresos (+) y resta gastos (-)
            val balanceDia = movimientos.filter { mov ->
                when (mov) {
                    is IncomeEntity -> mov.fecha == fechaStringDB
                    is ExpenseEntity -> mov.fecha == fechaStringDB
                    else -> false
                }
            }.sumOf { mov ->
                when (mov) {
                    is IncomeEntity -> mov.cantidad
                    is ExpenseEntity -> -mov.cantidad
                    else -> 0.0
                }
            }

            points.add(Point(index.toFloat(), balanceDia.toFloat()))
            labelsX.add(fecha.format(axisFormatter).replaceFirstChar { it.lowercase() })
        }

        // Valores únicos para el eje Y (ordenados para que el eje sea coherente)
        val uniqueYValues = points.map { it.y }.toMutableList().apply {
            if (!contains(0f)) add(0f)
        }.distinct().sorted()

        val minY = uniqueYValues.firstOrNull() ?: 0f
        val maxY = uniqueYValues.lastOrNull() ?: 100f

        Triple(points, labelsX, uniqueYValues)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Triple(emptyList<Point>(), emptyList<String>(), emptyList<Float>())
    )

    // Sealed class para diferenciar header de transacción
    sealed class MovimientoItem {
        data class Header(val fecha: String) : MovimientoItem()
        data class Transaccion(val movimiento: Any) : MovimientoItem()
    }

    val movimientosAgrupados: StateFlow<List<MovimientoItem>> = movimientosState.map { lista ->
        lista.take(15) // 👈 Límite de 15
            .groupBy { movimiento ->
                when (movimiento) {
                    is IncomeEntity -> movimiento.fecha
                    is ExpenseEntity -> movimiento.fecha
                    else -> ""
                }
            }
            .flatMap { (fecha, items) ->
                listOf(MovimientoItem.Header(fecha)) + items.map { MovimientoItem.Transaccion(it) }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val movimientosAgrupadosSinLimite: StateFlow<List<MovimientoItem>> = movimientosState.map { lista ->
        lista // 👈 Sin .take(15)
            .groupBy { movimiento ->
                when (movimiento) {
                    is IncomeEntity -> movimiento.fecha
                    is ExpenseEntity -> movimiento.fecha
                    else -> ""
                }
            }
            .flatMap { (fecha, items) ->
                listOf(MovimientoItem.Header(fecha)) + items.map { MovimientoItem.Transaccion(it) }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val mesActual = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-yyyy"))

    val totalIngresoMensual: StateFlow<Double> = incomeRepository.getAllIncomes()
        .map { ingresos ->
            ingresos.filter { ingreso ->
                val partes = ingreso.fecha.split("-") // formato dd-MM-yyyy
                "${partes.getOrElse(1){""}}-${partes.getOrElse(2){""}}" == mesActual
            }.sumOf { it.cantidad }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastoMensual: StateFlow<Double> = expenseRepository.getAllExpenses()
        .map { gastos ->
            gastos.filter { gasto ->
                val partes = gasto.fecha.split("-") // formato dd-MM-yyyy
                "${partes.getOrElse(1){""}}-${partes.getOrElse(2){""}}" == mesActual
            }.sumOf { it.cantidad }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val balanceMensual: StateFlow<Double> = combine(
        totalIngresoMensual,
        totalGastoMensual
    ) { ingresos, gastos ->
        ingresos - gastos
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}