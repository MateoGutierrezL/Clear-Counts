package com.example.clearcounts.data.repository.presupuesto

import com.example.clearcounts.data.database.dao.BudgetDao
import com.example.clearcounts.data.database.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
): BudgetRepository{
    override fun getAllBudgets() = budgetDao.getAllBudgets()

    override suspend fun insertBudget(budgetEntity: BudgetEntity) = budgetDao.insert(budgetEntity)

    override suspend fun deleteBudget(budgetEntity: BudgetEntity) = budgetDao.delete(budgetEntity)
    override suspend fun updateBudget(budgetEntity: BudgetEntity) = budgetDao.update(budgetEntity)
}