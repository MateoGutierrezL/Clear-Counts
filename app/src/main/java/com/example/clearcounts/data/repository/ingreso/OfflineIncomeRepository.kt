package com.example.clearcounts.data.repository.ingreso

import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.database.dao.IncomeDao
import com.example.clearcounts.data.database.dao.MonthlySummary
import com.example.clearcounts.data.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineIncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao
) : IncomeRepository {
    override suspend fun insertIncome(incomeEntity: IncomeEntity) = incomeDao.insert(incomeEntity)
    override fun getAllIncomes(userId: String) = incomeDao.getAllIncomes(userId)
    override suspend fun deleteIncome(incomeEntity: IncomeEntity) = incomeDao.delete(incomeEntity)
    override fun totalIncome(userId: String) = incomeDao.getTotalIncome(userId)
    override fun getMonthlyIncomes(userId: String) = incomeDao.getMonthlyIncomes(userId)
}