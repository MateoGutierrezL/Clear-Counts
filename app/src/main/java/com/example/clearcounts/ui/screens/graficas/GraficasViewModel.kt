package com.example.clearcounts.ui.screens.graficas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.ExpenseRepository
import com.example.clearcounts.data.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GraficasViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    val statsUiState: StateFlow<FinanceUiState> = combine(
        incomeRepository.getAllIncomes(),
        expenseRepository.getAllExpenses()
    ) { incomes, expenses ->

        // --- LÓGICA DE INGRESOS ---
        val incomeDailyTotals = incomes.groupBy { it.fecha }
            .map { it.value.sumOf { income -> income.cantidad } }

        val avgIncome = if (incomeDailyTotals.isNotEmpty()) incomeDailyTotals.average() else 0.0

        // --- LÓGICA DE GASTOS ---
        val expenseDailyTotals = expenses.groupBy { it.fecha }
            .map { it.value.sumOf { expense -> expense.cantidad } }

        val avgExpense = if (expenseDailyTotals.isNotEmpty()) expenseDailyTotals.average() else 0.0

        // 3. Retornamos el estado con ambos datos
        FinanceUiState(
            dailyAverageIncome = avgIncome,
            dailyAverageExpense = avgExpense,
            totalDays = (incomes.map { it.fecha } + expenses.map { it.fecha }).distinct().size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FinanceUiState()
    )

    val totalIngresos: StateFlow<Double> = incomeRepository.totalIncome()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastos: StateFlow<Double> = expenseRepository.totalExpense()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    private val hoy = LocalDate.now()
    val lunesSemana: LocalDate = hoy.with(DayOfWeek.MONDAY)
    val domingoSemana: LocalDate = hoy.with(DayOfWeek.SUNDAY)

    // Los 7 días de la semana actual (Lun → Dom)
    val diasSemana: List<LocalDate> = (0..6).map { lunesSemana.plusDays(it.toLong()) }

    val ingresosPorDia: StateFlow<Map<LocalDate, Double>> =
        incomeRepository.getAllIncomes()
            .map { lista ->
                lista
                    .mapNotNull { entity ->
                        runCatching {
                            LocalDate.parse(entity.fecha, formatter)
                        }.getOrNull()?.let { fecha -> fecha to entity.cantidad }
                    }
                    .filter { (fecha, _) ->
                        !fecha.isBefore(lunesSemana) && !fecha.isAfter(domingoSemana)
                    }
                    .groupBy { (fecha, _) -> fecha }
                    .mapValues { (_, items) -> items.sumOf { it.second } }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )

    val gastosPorDia: StateFlow<Map<LocalDate, Double>> =
        expenseRepository.getAllExpenses()
            .map { lista ->
                lista
                    .mapNotNull { entity ->
                        runCatching {
                            LocalDate.parse(entity.fecha, formatter)
                        }.getOrNull()?.let { fecha -> fecha to entity.cantidad }
                    }
                    .filter { (fecha, _) ->
                        !fecha.isBefore(lunesSemana) && !fecha.isAfter(domingoSemana)
                    }
                    .groupBy { (fecha, _) -> fecha }
                    .mapValues { (_, items) -> items.sumOf { it.second } }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )
}

data class FinanceUiState(
    val dailyAverageIncome: Double = 0.0,
    val dailyAverageExpense: Double = 0.0,
    val totalDays: Int = 0
)