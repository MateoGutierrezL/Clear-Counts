package com.example.clearcounts.data

import com.example.clearcounts.data.database.dao.ExpenseDao
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import javax.inject.Inject

class OfflineExpenseRepository @Inject constructor(

    private val expenseDao: ExpenseDao
): ExpenseRepository {

    override suspend fun insertExpense(expenseEntity: ExpenseEntity) = expenseDao.insert(expenseEntity)

}