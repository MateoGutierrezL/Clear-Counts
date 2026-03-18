package com.example.clearcounts.data.local.repository.gasto

import com.example.clearcounts.data.local.database.dao.ExpenseDao
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class OfflineExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val firestoreSync: FirestoreSyncRepository,
    private val auth: FirebaseAuth
) : ExpenseRepository {

    private val userId get() = auth.currentUser?.uid ?: ""

    override suspend fun insertExpense(expenseEntity: ExpenseEntity) {
        expenseDao.insert(expenseEntity)
        firestoreSync.subirGasto(userId, expenseEntity)
    }

    override suspend fun insertExpenseLocal(expenseEntity: ExpenseEntity) {
        expenseDao.insert(expenseEntity)
    }

    override fun getAllExpenses(userId: String) = expenseDao.getAllExpenses(userId)
    override suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        expenseDao.delete(expenseEntity)
        firestoreSync.eliminarGasto(userId, expenseEntity.id.toString())
    }
    override fun totalExpense(userId: String) = expenseDao.getTotalExpense(userId)
    override fun getExpensesByCategory(userId: String) = expenseDao.getExpensesByCategory(userId)
    override fun getMonthlyExpense(userId: String) = expenseDao.getMonthlyExpenses(userId)
}