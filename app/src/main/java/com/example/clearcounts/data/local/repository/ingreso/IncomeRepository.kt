package com.example.clearcounts.data.local.repository.ingreso

import com.example.clearcounts.data.local.database.dao.MonthlySummary
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

interface IncomeRepository {
    suspend fun insertIncome(incomeEntity: IncomeEntity)

    suspend fun insertIncomeLocal(incomeEntity: IncomeEntity)
    fun getAllIncomes(userId: String): Flow<List<IncomeEntity>>
    suspend fun deleteIncome(incomeEntity: IncomeEntity)
    fun totalIncome(userId: String): Flow<Double?>
    fun getMonthlyIncomes(userId: String): Flow<List<MonthlySummary>>
}