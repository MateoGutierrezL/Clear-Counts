package com.example.clearcounts.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringDao {

    @Query("""
    SELECT * FROM recurrente WHERE user_id = :userId 
    ORDER BY CASE frecuencia 
        WHEN 'diario' THEN 1 
        WHEN 'semanal' THEN 2 
        WHEN 'quincenal' THEN 3 
        WHEN 'mensual' THEN 4 
        ELSE 5 
    END ASC
""")
    fun getAllRecurring(userId: String): Flow<List<RecurringEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recurringEntity: RecurringEntity)

    @Update
    suspend fun update(recurringEntity: RecurringEntity)

    @Delete
    suspend fun delete(recurringEntity: RecurringEntity)
}