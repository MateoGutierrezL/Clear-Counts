package com.example.clearcounts.ui.screens.Exportar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.local.repository.notificacion.NotificationRepository
import com.example.clearcounts.ui.screens.userIdFlow
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ExportarGraficosViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val notificationRepository: NotificationRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userIdFlow = auth.userIdFlow()

    private val _mesSeleccionado = MutableStateFlow("02-2026")
    val mesSeleccionado = _mesSeleccionado.asStateFlow()

    private val allIncomes: StateFlow<List<IncomeEntity>> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(emptyList())
            else incomeRepository.getAllIncomes(uid)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val allExpenses: StateFlow<List<ExpenseEntity>> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(emptyList())
            else expenseRepository.getAllExpenses(uid)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val movimientosFiltrados: StateFlow<List<Any>> = combine(
        allIncomes, allExpenses, _mesSeleccionado
    ) { ingresos, gastos, filtro ->
        ingresos.filter { it.fecha.contains(filtro) } +
                gastos.filter { it.fecha.contains(filtro) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIngresosMensuales: StateFlow<Double> = movimientosFiltrados
        .map { lista -> lista.filterIsInstance<IncomeEntity>().sumOf { it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastosMensuales: StateFlow<Double> = movimientosFiltrados
        .map { lista -> lista.filterIsInstance<ExpenseEntity>().sumOf { it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalIngresoVsGastoMes: StateFlow<Pair<Double, Double>> = combine(
        totalIngresosMensuales, totalGastosMensuales
    ) { ingresos, gastos ->
        Pair(ingresos, gastos)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(0.0, 0.0))

    private val anioSeleccionado: StateFlow<String> = _mesSeleccionado
        .map { it.split("-").getOrElse(1) { "2026" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "2026")

    val ingresosPorMes: StateFlow<Map<String, Double>> = combine(
        allIncomes, anioSeleccionado
    ) { ingresos, anio ->
        (1..12).map { it.toString().padStart(2, '0') }.associateWith { mes ->
            ingresos.filter { ingreso ->
                val partes = ingreso.fecha.split("-")
                partes.getOrElse(1) { "" } == mes && partes.getOrElse(2) { "" } == anio
            }.sumOf { it.cantidad }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val gastosPorCategoria: StateFlow<Map<String, Double>> = combine(
        allExpenses, _mesSeleccionado
    ) { gastos, filtro ->
        val partesFiltro = filtro.split("-")
        val mes = partesFiltro.getOrElse(0) { "" }
        val anio = partesFiltro.getOrElse(1) { "" }
        gastos.filter { gasto ->
            val partes = gasto.fecha.split("-")
            partes.getOrElse(1) { "" } == mes && partes.getOrElse(2) { "" } == anio
        }
            .groupBy { it.categoria }
            .mapValues { (_, lista) -> lista.sumOf { it.cantidad } }
            .filter { it.value > 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val comparacionMensual: StateFlow<List<Triple<String, Double, Double>>> = combine(
        allIncomes, allExpenses, _mesSeleccionado
    ) { ingresos, gastos, filtro ->
        val anio = filtro.split("-").getOrElse(1) { "2026" }
        (1..12).map { it.toString().padStart(2, '0') }.map { mes ->
            val totalIngreso = ingresos.filter { ingreso ->
                val partes = ingreso.fecha.split("-")
                partes.getOrElse(1) { "" } == mes && partes.getOrElse(2) { "" } == anio
            }.sumOf { it.cantidad }
            val totalGasto = gastos.filter { gasto ->
                val partes = gasto.fecha.split("-")
                partes.getOrElse(1) { "" } == mes && partes.getOrElse(2) { "" } == anio
            }.sumOf { it.cantidad }
            Triple(mes, totalIngreso, totalGasto)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setMesFiltro(nuevoMesAnio: String) { _mesSeleccionado.value = nuevoMesAnio }

    fun generarCsvString(movimientos: List<Any>): String {
        val csvHeader = "Tipo;Categoria;Cantidad;Fecha;Hora;Nota\n"
        val csvBody = movimientos.joinToString("\n") { mov ->
            when (mov) {
                is IncomeEntity -> "Ingreso;${mov.categoria};${mov.cantidad};${mov.fecha};${mov.hora};${mov.nota ?: ""}"
                is ExpenseEntity -> "Gasto;${mov.categoria};${mov.cantidad};${mov.fecha};${mov.hora};${mov.nota ?: ""}"
                else -> ""
            }
        }
        return csvHeader + csvBody
    }

    fun notificarExportacion(tipo: String) {
        viewModelScope.launch {
            notificationRepository.insertNotification(
                titulo = "Exportación completada",
                mensaje = "Tu reporte de ${mesSeleccionado.value} fue exportado como $tipo exitosamente."
            )
        }
    }
}
