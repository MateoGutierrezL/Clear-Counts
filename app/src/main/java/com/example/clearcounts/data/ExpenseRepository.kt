package com.example.clearcounts.data

import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity

interface ExpenseRepository {

    suspend fun insertExpense(expenseEntity: ExpenseEntity)

}