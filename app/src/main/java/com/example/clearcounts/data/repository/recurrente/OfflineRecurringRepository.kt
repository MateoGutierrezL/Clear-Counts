package com.example.clearcounts.data.repository.recurrente

import com.example.clearcounts.data.database.dao.RecurringDao
import com.example.clearcounts.data.database.entities.RecurringEntity
import javax.inject.Inject

class OfflineRecurringRepository @Inject constructor(
    private val recurringDao: RecurringDao
) : RecurringRepository {
    override fun getAllRecurring(userId: String) = recurringDao.getAllRecurring(userId)
    override suspend fun insertRecurring(entity: RecurringEntity) = recurringDao.insert(entity)
    override suspend fun updateRecurring(entity: RecurringEntity) = recurringDao.update(entity)
    override suspend fun deleteRecurring(entity: RecurringEntity) = recurringDao.delete(entity)
}
