package com.example.clearcounts.data.repository.ingreso

import com.example.clearcounts.data.database.dao.MonthlySummary
import com.example.clearcounts.data.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

interface IncomeRepository {

    suspend fun insertIncome(incomeEntity: IncomeEntity)

    fun getAllIncomes(): Flow<List<IncomeEntity>>

    suspend fun deleteIncome(incomeEntity: IncomeEntity)

    fun totalIncome(): Flow<Double?>

    fun getMonthlyIncomes(): Flow<List<MonthlySummary>>

}