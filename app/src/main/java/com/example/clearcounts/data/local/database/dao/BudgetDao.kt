package com.example.clearcounts.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clearcounts.data.local.database.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget WHERE user_id = :userId")
    fun getAllBudgets(userId: String): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetEntity: BudgetEntity)

    @Delete
    suspend fun delete(budgetEntity: BudgetEntity)

    @Update
    suspend fun update(budgetEntity: BudgetEntity)
}