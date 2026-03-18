package com.example.clearcounts.data.local.repository.recurrente

import com.example.clearcounts.data.local.database.dao.RecurringDao
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class OfflineRecurringRepository @Inject constructor(
    private val recurringDao: RecurringDao,
    private val firestoreSync: FirestoreSyncRepository,
    private val auth: FirebaseAuth
) : RecurringRepository {

    private val userId get() = auth.currentUser?.uid ?: ""

    override fun getAllRecurring(userId: String) = recurringDao.getAllRecurring(userId)

    override suspend fun insertRecurring(entity: RecurringEntity) {
        recurringDao.insert(entity)
        firestoreSync.subirRecurrente(userId, entity)
    }

    override suspend fun insertRecurringLocal(entity: RecurringEntity) {
        recurringDao.insert(entity)
    }

    override suspend fun updateRecurring(entity: RecurringEntity) {
        recurringDao.update(entity)
        firestoreSync.subirRecurrente(userId, entity)
    }

    override suspend fun deleteRecurring(entity: RecurringEntity) {
        recurringDao.delete(entity)
        firestoreSync.eliminarRecurrente(userId, entity.id.toString())
    }
}
