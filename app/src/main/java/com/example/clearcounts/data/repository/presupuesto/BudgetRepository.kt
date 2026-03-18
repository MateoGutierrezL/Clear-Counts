package com.example.clearcounts.data.repository.presupuesto

import com.example.clearcounts.data.database.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(userId: String): Flow<List<BudgetEntity>>
    suspend fun insertBudget(budgetEntity: BudgetEntity)
    suspend fun deleteBudget(budgetEntity: BudgetEntity)
    suspend fun updateBudget(budgetEntity: BudgetEntity)
}