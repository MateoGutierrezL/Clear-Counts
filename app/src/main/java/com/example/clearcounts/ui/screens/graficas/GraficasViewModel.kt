package com.example.clearcounts.ui.screens.graficas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.database.dao.CategoryExpenseSummary
import com.example.clearcounts.data.local.database.dao.MonthlySummary
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.google.firebase.auth.FirebaseAuth
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
    private val expenseRepository: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userId get() = auth.currentUser?.uid ?: ""

    val statsUiState: StateFlow<FinanceUiState> = combine(
        incomeRepository.getAllIncomes(userId),
        expenseRepository.getAllExpenses(userId)
    ) { incomes, expenses ->
        val incomeDailyTotals = incomes.groupBy { it.fecha }
            .map { it.value.sumOf { income -> income.cantidad } }
        val avgIncome = if (incomeDailyTotals.isNotEmpty()) incomeDailyTotals.average() else 0.0
        val expenseDailyTotals = expenses.groupBy { it.fecha }
            .map { it.value.sumOf { expense -> expense.cantidad } }
        val avgExpense = if (expenseDailyTotals.isNotEmpty()) expenseDailyTotals.average() else 0.0
        FinanceUiState(
            dailyAverageIncome = avgIncome,
            dailyAverageExpense = avgExpense,
            totalDays = (incomes.map { it.fecha } + expenses.map { it.fecha }).distinct().size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinanceUiState())

    val totalIngresos: StateFlow<Double> = incomeRepository.totalIncome(userId)
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGastos: StateFlow<Double> = expenseRepository.totalExpense(userId)
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val hoy = LocalDate.now()
    val lunesSemana: LocalDate = hoy.with(DayOfWeek.MONDAY)
    val domingoSemana: LocalDate = hoy.with(DayOfWeek.SUNDAY)
    val diasSemana: List<LocalDate> = (0..6).map { lunesSemana.plusDays(it.toLong()) }

    val ingresosPorDia: StateFlow<Map<LocalDate, Double>> =
        incomeRepository.getAllIncomes(userId)
            .map { lista ->
                lista.mapNotNull { entity ->
                    runCatching { LocalDate.parse(entity.fecha, formatter) }
                        .getOrNull()?.let { fecha -> fecha to entity.cantidad }
                }
                    .filter { (fecha, _) -> !fecha.isBefore(lunesSemana) && !fecha.isAfter(domingoSemana) }
                    .groupBy { (fecha, _) -> fecha }
                    .mapValues { (_, items) -> items.sumOf { it.second } }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val gastosPorDia: StateFlow<Map<LocalDate, Double>> =
        expenseRepository.getAllExpenses(userId)
            .map { lista ->
                lista.mapNotNull { entity ->
                    runCatching { LocalDate.parse(entity.fecha, formatter) }
                        .getOrNull()?.let { fecha -> fecha to entity.cantidad }
                }
                    .filter { (fecha, _) -> !fecha.isBefore(lunesSemana) && !fecha.isAfter(domingoSemana) }
                    .groupBy { (fecha, _) -> fecha }
                    .mapValues { (_, items) -> items.sumOf { it.second } }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val expensesByCategory: StateFlow<List<CategoryExpenseSummary>> =
        expenseRepository.getExpensesByCategory(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val monthlyExpenses: StateFlow<List<MonthlySummary>> =
        expenseRepository.getMonthlyExpense(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val monthlyIncomes: StateFlow<List<MonthlySummary>> =
        incomeRepository.getMonthlyIncomes(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val monthlyComparison: StateFlow<List<MonthlyComparision>> =
        combine(monthlyExpenses, monthlyIncomes) { expenses, incomes ->
            val allMonths = (expenses.map { it.mes } + incomes.map { it.mes }).distinct().sorted()
            allMonths.map { mes ->
                MonthlyComparision(
                    mes = mes,
                    totalGasto = expenses.find { it.mes == mes }?.total ?: 0.0,
                    totalIngreso = incomes.find { it.mes == mes }?.total ?: 0.0
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())



}

