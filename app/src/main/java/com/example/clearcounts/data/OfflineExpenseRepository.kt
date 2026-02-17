package com.example.clearcounts.data

import com.example.clearcounts.data.database.dao.ExpenseDao
import javax.inject.Inject

class OfflineExpenseRepository @Inject constructor(

    private val expenseDao: ExpenseDao
): ExpenseRepository {
}