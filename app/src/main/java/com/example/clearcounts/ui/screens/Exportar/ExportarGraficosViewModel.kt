package com.example.clearcounts.ui.screens.Exportar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.database.dao.CategoryExpenseSummary
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.data.repository.notificacion.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ExportarGraficosViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val notificationRepository: NotificationRepository
): ViewModel(){

    private val _mesSeleccionado = MutableStateFlow("02-2026")
    val mesSeleccionado = _mesSeleccionado.asStateFlow()

    // LISTA UNIDA Y FILTRADA: Para la Vista Previa y el CSV
    val movimientosFiltrados: StateFlow<List<Any>> = combine(
        incomeRepository.getAllIncomes(),
        expenseRepository.getAllExpenses(),
        _mesSeleccionado
    ) { ingresos, gastos, filtro ->
        val unida = ingresos.filter { it.fecha.contains(filtro) } +
                gastos.filter { it.fecha.contains(filtro) }
        unida // Aquí podrías agregar un .sortedBy si lo deseas
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalIngresosMensuales: StateFlow<Double> = movimientosFiltrados.map { lista ->
        lista.filterIsInstance<IncomeEntity>().sumOf { it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastosMensuales: StateFlow<Double> = movimientosFiltrados.map { lista ->
        lista.filterIsInstance<ExpenseEntity>().sumOf { it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun setMesFiltro(nuevoMesAnio: String) {
        _mesSeleccionado.value = nuevoMesAnio
    }

    fun generarCsvString(movimientos: List<Any>): String {
        val csvHeader = "Tipo;Categoría;Cantidad;Fecha;Hora;Nota\n"
        val csvBody = movimientos.joinToString("\n") { mov ->
            when (mov) {
                is IncomeEntity -> "Ingreso;${mov.categoria};${mov.cantidad};${mov.fecha};${mov.hora};${mov.nota ?: ""}"
                is ExpenseEntity -> "Gasto;${mov.categoria};${mov.cantidad};${mov.fecha};${mov.hora};${mov.nota ?: ""}"
                else -> ""
            }
        }
        return csvHeader + csvBody
    }
    fun notificarExportacion(tipo: String) { // 👈 Llama esto al exportar
        viewModelScope.launch {
            notificationRepository.insertNotification(
                titulo = "Exportación completada",
                mensaje = "Tu reporte de ${mesSeleccionado.value} fue exportado como $tipo exitosamente."
            )
        }
    }


    // Año extraído del filtro actual
    private val anioSeleccionado: StateFlow<String> = _mesSeleccionado.map {
        it.split("-").getOrElse(1) { "2026" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "2026")

    // Ingresos por mes del año seleccionado
    val ingresosPorMes: StateFlow<Map<String, Double>> = combine(
        incomeRepository.getAllIncomes(),
        anioSeleccionado
    ) { ingresos, anio ->
        val meses = (1..12).map { it.toString().padStart(2, '0') }
        meses.associateWith { mes ->
            ingresos.filter { ingreso ->
                val partes = ingreso.fecha.split("-")
                partes.getOrElse(1) { "" } == mes && partes.getOrElse(2) { "" } == anio
            }.sumOf { it.cantidad }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Gastos por categoría del mes filtrado
    val gastosPorCategoria: StateFlow<Map<String, Double>> = combine(
        expenseRepository.getAllExpenses(),
        _mesSeleccionado
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

    // Comparación mensual
    val comparacionMensual: StateFlow<List<Triple<String, Double, Double>>> = combine(
        incomeRepository.getAllIncomes(),
        expenseRepository.getAllExpenses(),
        _mesSeleccionado
    ) { ingresos, gastos, filtro ->
        val anio = filtro.split("-").getOrElse(1) { "2026" }
        val meses = (1..12).map { it.toString().padStart(2, '0') }
        meses.map { mes ->
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

    // Ingresos vs Gastos totales del mes
    val totalIngresoVsGastoMes: StateFlow<Pair<Double, Double>> = combine(
        totalIngresosMensuales,
        totalGastosMensuales
    ) { ingresos, gastos ->
        Pair(ingresos, gastos)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(0.0, 0.0))
}
