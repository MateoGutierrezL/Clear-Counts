package com.example.clearcounts.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clearcounts.data.database.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budgetEntity: BudgetEntity)

    @Delete
    suspend fun delete(budgetEntity: BudgetEntity)

    @Update
    suspend fun update(budgetEntity: BudgetEntity)
}