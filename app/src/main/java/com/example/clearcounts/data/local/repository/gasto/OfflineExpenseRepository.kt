package com.example.clearcounts.data.local.repository.gasto

import com.example.clearcounts.data.local.database.dao.ExpenseDao
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class OfflineExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val firestoreSync: FirestoreSyncRepository,
    private val auth: FirebaseAuth
) : ExpenseRepository {

    private val userId get() = auth.currentUser?.uid ?: ""

    override suspend fun insertExpense(expenseEntity: ExpenseEntity) {
        // 1. Guarda localmente
        expenseDao.insert(expenseEntity)

        // 2. Sincroniza en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestoreSync.subirGasto(userId, expenseEntity)
            } catch (e: Exception) {
                // Manejar error silenciosamente o con un Log
            }
        }
    }

    override suspend fun insertExpenseLocal(expenseEntity: ExpenseEntity) {
        expenseDao.insert(expenseEntity)
    }

    override fun getAllExpenses(userId: String) = expenseDao.getAllExpenses(userId)
    override suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        expenseDao.delete(expenseEntity)
        firestoreSync.eliminarGasto(userId, expenseEntity.firestoreId)
    }
    override fun totalExpense(userId: String) = expenseDao.getTotalExpense(userId)
    override fun getExpensesByCategory(userId: String) = expenseDao.getExpensesByCategory(userId)
    override fun getMonthlyExpense(userId: String) = expenseDao.getMonthlyExpenses(userId)
}