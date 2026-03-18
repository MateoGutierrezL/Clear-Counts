package com.example.clearcounts.ui.screens.Exportar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.database.dao.CategoryExpenseSummary
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.data.repository.notificacion.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
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
    private val notificationRepository: NotificationRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userId get() = auth.currentUser?.uid ?: ""

    private val _mesSeleccionado = MutableStateFlow("02-2026")
    val mesSeleccionado = _mesSeleccionado.asStateFlow()

    val movimientosFiltrados: StateFlow<List<Any>> = combine(
        incomeRepository.getAllIncomes(userId),
        expenseRepository.getAllExpenses(userId),
        _mesSeleccionado
    ) { ingresos, gastos, filtro ->
        val unida = ingresos.filter { it.fecha.contains(filtro) } +
                gastos.filter { it.fecha.contains(filtro) }
        unida
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIngresosMensuales: StateFlow<Double> = movimientosFiltrados.map { lista ->
        lista.filterIsInstance<IncomeEntity>().sumOf { it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastosMensuales: StateFlow<Double> = movimientosFiltrados.map { lista ->
        lista.filterIsInstance<ExpenseEntity>().sumOf { it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun setMesFiltro(nuevoMesAnio: String) { _mesSeleccionado.value = nuevoMesAnio }

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

    fun notificarExportacion(tipo: String) {
        viewModelScope.launch {
            notificationRepository.insertNotification(
                titulo = "Exportación completada",
                mensaje = "Tu reporte de ${mesSeleccionado.value} fue exportado como $tipo exitosamente."
            )
        }
    }
}
