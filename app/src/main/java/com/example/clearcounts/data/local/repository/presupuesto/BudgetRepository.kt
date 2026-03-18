package com.example.clearcounts.data.local.repository.presupuesto

import com.example.clearcounts.data.local.database.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(userId: String): Flow<List<BudgetEntity>>
    suspend fun insertBudget(budgetEntity: BudgetEntity)

    suspend fun insertBudgetLocal(budgetEntity: BudgetEntity)
    suspend fun deleteBudget(budgetEntity: BudgetEntity)
    suspend fun updateBudget(budgetEntity: BudgetEntity)
}