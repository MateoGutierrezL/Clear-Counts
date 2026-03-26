package com.example.clearcounts.data.local.repository.presupuesto

import com.example.clearcounts.data.local.database.dao.BudgetDao
import com.example.clearcounts.data.local.database.entities.BudgetEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class OfflineBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao,
    private val firestoreSync: FirestoreSyncRepository,
    private val auth: FirebaseAuth
) : BudgetRepository {

    private val userId get() = auth.currentUser?.uid ?: ""

    override fun getAllBudgets(userId: String) = budgetDao.getAllBudgets(userId)

    override suspend fun insertBudget(budgetEntity: BudgetEntity) {
        budgetDao.insert(budgetEntity)
        firestoreSync.subirPresupuesto(userId, budgetEntity)
    }

    override suspend fun insertBudgetLocal(budgetEntity: BudgetEntity) {
        budgetDao.insert(budgetEntity)
    }

    override suspend fun deleteBudget(budgetEntity: BudgetEntity) {
        budgetDao.delete(budgetEntity)
        firestoreSync.eliminarPresupuesto(userId, budgetEntity.firestoreId)
    }

    override suspend fun updateBudget(budgetEntity: BudgetEntity) {
        budgetDao.update(budgetEntity)
        firestoreSync.subirPresupuesto(userId, budgetEntity)
    }
}