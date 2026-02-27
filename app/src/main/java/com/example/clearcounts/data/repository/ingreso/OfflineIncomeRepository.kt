package com.example.clearcounts.data.repository.ingreso

import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.database.dao.IncomeDao
import com.example.clearcounts.data.database.dao.MonthlySummary
import com.example.clearcounts.data.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineIncomeRepository @Inject constructor(

    private val incomeDao: IncomeDao
): IncomeRepository {

    override suspend fun insertIncome(incomeEntity: IncomeEntity) = incomeDao.insert(incomeEntity)
    override fun getAllIncomes(): Flow<List<IncomeEntity>> {
        return incomeDao.getAllIncomes()
    }

    override suspend fun deleteIncome(incomeEntity: IncomeEntity) = incomeDao.delete(incomeEntity)

    override fun totalIncome(): Flow<Double?> {
        return incomeDao.getTotalIncome()
    }

    override fun getMonthlyIncomes() = incomeDao.getMonthlyIncomes()

}