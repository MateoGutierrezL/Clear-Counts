package com.example.clearcounts.data.repository.gasto

import com.example.clearcounts.data.database.dao.CategoryExpenseSummary
import com.example.clearcounts.data.database.dao.ExpenseDao
import com.example.clearcounts.data.database.dao.MonthlySummary
import com.example.clearcounts.data.database.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineExpenseRepository @Inject constructor(

    private val expenseDao: ExpenseDao
): ExpenseRepository {

    override suspend fun insertExpense(expenseEntity: ExpenseEntity) = expenseDao.insert(expenseEntity)
    override fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.getAllExpenses()
    }

    override suspend fun deleteExpense(expenseEntity: ExpenseEntity) = expenseDao.delete(expenseEntity)
    override fun totalExpense(): Flow<Double?> {
        return expenseDao.getTotalExpense()
    }

    override fun getExpensesByCategory() = expenseDao.getExpensesByCategory()
    override fun getMonthlyExpense() = expenseDao.getMonthlyExpenses()
}