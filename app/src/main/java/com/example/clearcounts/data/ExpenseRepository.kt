package com.example.clearcounts.data

import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    fun getAllExpenses(): Flow<List<ExpenseEntity>>

}