package com.example.clearcounts.data.local.repository.gasto

import com.example.clearcounts.data.local.database.dao.CategoryExpenseSummary
import com.example.clearcounts.data.local.database.dao.MonthlySummary
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    suspend fun insertExpenseLocal(expenseEntity: ExpenseEntity)
    fun getAllExpenses(userId: String): Flow<List<ExpenseEntity>>
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)
    fun totalExpense(userId: String): Flow<Double?>
    fun getExpensesByCategory(userId: String): Flow<List<CategoryExpenseSummary>>
    fun getMonthlyExpense(userId: String): Flow<List<MonthlySummary>>
}