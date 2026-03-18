package com.example.clearcounts.data.local.repository.recurrente

import com.example.clearcounts.data.local.database.entities.RecurringEntity
import kotlinx.coroutines.flow.Flow

interface RecurringRepository {
    fun getAllRecurring(userId: String): Flow<List<RecurringEntity>>
    suspend fun insertRecurring(entity: RecurringEntity)
    suspend fun insertRecurringLocal(entity: RecurringEntity)
    suspend fun updateRecurring(entity: RecurringEntity)
    suspend fun deleteRecurring(entity: RecurringEntity)
}