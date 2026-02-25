package com.example.clearcounts.ui.screens.graficas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.ExpenseRepository
import com.example.clearcounts.data.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
}

data class FinanceUiState(
    val dailyAverageIncome: Double = 0.0,
    val dailyAverageExpense: Double = 0.0,
    val totalDays: Int = 0
)