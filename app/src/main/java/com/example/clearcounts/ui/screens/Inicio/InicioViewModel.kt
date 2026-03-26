package com.example.clearcounts.ui.screens.Inicio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.local.repository.pago.PaymentMethodRepository
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
    private val expenseRepository: ExpenseRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    //  userId como Flow reactivo
    private val userIdFlow: StateFlow<String> = MutableStateFlow(
        auth.currentUser?.uid ?: ""
    ).also { flow ->
        auth.addAuthStateListener { firebaseAuth ->
            (flow as MutableStateFlow).value = firebaseAuth.currentUser?.uid ?: ""
        }
    }

    val movimientosState: StateFlow<List<Any>> = userIdFlow
        .flatMapLatest { uid ->
            combine(
                incomeRepository.getAllIncomes(uid),
                expenseRepository.getAllExpenses(uid)
            ) { ingresos, gastos ->
                (ingresos + gastos).sortedByDescending { movimiento ->
                    when (movimiento) {
                        is IncomeEntity -> combinarFechaHora(movimiento.fecha, movimiento.hora)
                        is ExpenseEntity -> combinarFechaHora(movimiento.fecha, movimiento.hora)
                        else -> LocalDateTime.MIN
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun combinarFechaHora(fecha: String, hora: String): LocalDateTime {
        return try {
            val formatterFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formatterHora = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            LocalDateTime.of(LocalDate.parse(fecha, formatterFecha), LocalTime.parse(hora, formatterHora))
        } catch (e: Exception) { LocalDateTime.MIN }
    }

    fun deleteIncome(incomeEntity: IncomeEntity) {
        viewModelScope.launch {
            try { incomeRepository.deleteIncome(incomeEntity) }
            catch (e: Exception) { Log.e("ingreso", "fallo al eliminar: ${e.message}") }
        }
    }

    fun deleteExpense(expenseEntity: ExpenseEntity) {
        viewModelScope.launch {
            try { expenseRepository.deleteExpense(expenseEntity) }
            catch (e: Exception) { Log.e("gasto", "fallo al eliminar: ${e.message}") }
        }
    }

    val totalIngresoSum: StateFlow<Double> = userIdFlow
        .flatMapLatest { uid -> incomeRepository.totalIncome(uid).map { it ?: 0.0 } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastoSum: StateFlow<Double> = userIdFlow
        .flatMapLatest { uid -> expenseRepository.totalExpense(uid).map { it ?: 0.0 } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val dbFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val axisFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale("es", "ES"))

    val chartDataState = movimientosState.map { movimientos ->
        val hoy = LocalDate.now()
        val points = mutableListOf<Point>()
        val labelsX = mutableListOf<String>()
        val ultimos7Dias = (0..6).map { hoy.minusDays(it.toLong()) }.reversed()
        ultimos7Dias.forEachIndexed { index, fecha ->
            val fechaStringDB = fecha.format(dbFormatter)
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
        val uniqueYValues = points.map { it.y }.toMutableList().apply {
            if (!contains(0f)) add(0f)
        }.distinct().sorted()
        Triple(points, labelsX, uniqueYValues)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        Triple(emptyList<Point>(), emptyList<String>(), emptyList<Float>()))

    sealed class MovimientoItem {
        data class Header(val fecha: String) : MovimientoItem()
        data class Transaccion(val movimiento: Any) : MovimientoItem()
    }

    val movimientosAgrupados: StateFlow<List<MovimientoItem>> = movimientosState.map { lista ->
        lista.take(15)
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val movimientosAgrupadosSinLimite: StateFlow<List<MovimientoItem>> = movimientosState.map { lista ->
        lista.groupBy { movimiento ->
            when (movimiento) {
                is IncomeEntity -> movimiento.fecha
                is ExpenseEntity -> movimiento.fecha
                else -> ""
            }
        }.flatMap { (fecha, items) ->
            listOf(MovimientoItem.Header(fecha)) + items.map { MovimientoItem.Transaccion(it) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mesActual = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-yyyy"))


    val totalIngresoMensual: StateFlow<Double> = userIdFlow
        .flatMapLatest { uid ->
            incomeRepository.getAllIncomes(uid).map { ingresos ->
                ingresos.filter { ingreso ->
                    val partes = ingreso.fecha.split("-")
                    "${partes.getOrElse(1) { "" }}-${partes.getOrElse(2) { "" }}" == mesActual
                }.sumOf { it.cantidad }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastoMensual: StateFlow<Double> = userIdFlow
        .flatMapLatest { uid ->
            expenseRepository.getAllExpenses(uid).map { gastos ->
                gastos.filter { gasto ->
                    val partes = gasto.fecha.split("-")
                    "${partes.getOrElse(1) { "" }}-${partes.getOrElse(2) { "" }}" == mesActual
                }.sumOf { it.cantidad }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val balanceMensual: StateFlow<Double> = combine(totalIngresoMensual, totalGastoMensual) { i, g ->
        i - g
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val balancePorMetodoPago: StateFlow<Map<String, Pair<String, Double>>> = userIdFlow
        .flatMapLatest { uid ->
            combine(
                incomeRepository.getAllIncomes(uid),
                expenseRepository.getAllExpenses(uid),
                paymentMethodRepository.getAllPaymentMethods(uid)
            ) { ingresos, gastos, metodos ->
                metodos.associate { metodo ->
                    val ingresoMetodo = ingresos.filter { ingreso ->
                        val partes = ingreso.fecha.split("-")
                        "${partes.getOrElse(1) { "" }}-${partes.getOrElse(2) { "" }}" == mesActual &&
                                ingreso.metodoPago == metodo.nombre
                    }.sumOf { it.cantidad }
                    val gastoMetodo = gastos.filter { gasto ->
                        val partes = gasto.fecha.split("-")
                        "${partes.getOrElse(1) { "" }}-${partes.getOrElse(2) { "" }}" == mesActual &&
                                gasto.metodoPago == metodo.nombre
                    }.sumOf { it.cantidad }
                    metodo.nombre to Pair(metodo.icono, ingresoMetodo - gastoMetodo)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val metodosPago: StateFlow<List<PaymentMethodEntity>> = userIdFlow
        .flatMapLatest { uid -> paymentMethodRepository.getAllPaymentMethods(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}