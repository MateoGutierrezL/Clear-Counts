package com.example.clearcounts.data.local.repository.ingreso

import com.example.clearcounts.data.local.database.dao.IncomeDao
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class OfflineIncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao,
    private val firestoreSync: FirestoreSyncRepository,
    private val auth: FirebaseAuth
) : IncomeRepository {

    private val userId get() = auth.currentUser?.uid ?: ""

    override suspend fun insertIncome(incomeEntity: IncomeEntity) {
        incomeDao.insert(incomeEntity)
        firestoreSync.subirIngreso(userId, incomeEntity)
    }

    override suspend fun insertIncomeLocal(incomeEntity: IncomeEntity) {
        incomeDao.insert(incomeEntity)
    }

    override fun getAllIncomes(userId: String) = incomeDao.getAllIncomes(userId)
    override suspend fun deleteIncome(incomeEntity: IncomeEntity) {
        incomeDao.delete(incomeEntity)
        firestoreSync.eliminarIngreso(userId, incomeEntity.id.toString())
    }
    override fun totalIncome(userId: String) = incomeDao.getTotalIncome(userId)
    override fun getMonthlyIncomes(userId: String) = incomeDao.getMonthlyIncomes(userId)
}