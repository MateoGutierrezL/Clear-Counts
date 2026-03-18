package com.example.clearcounts.data.repository.recurrente

import com.example.clearcounts.data.database.entities.RecurringEntity
import kotlinx.coroutines.flow.Flow

interface RecurringRepository {
    fun getAllRecurring(userId: String): Flow<List<RecurringEntity>>
    suspend fun insertRecurring(entity: RecurringEntity)
    suspend fun updateRecurring(entity: RecurringEntity)
    suspend fun deleteRecurring(entity: RecurringEntity)
}