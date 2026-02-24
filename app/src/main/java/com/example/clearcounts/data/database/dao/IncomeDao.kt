package com.example.clearcounts.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Query("SELECT * FROM ingreso")
    fun getAllIncomes(): Flow<List<IncomeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(incomeEntity: IncomeEntity)

    @Delete
    suspend fun delete(incomeEntity: IncomeEntity)

    @Query("SELECT SUM(cantidad) FROM ingreso")
    fun getTotalIncome(): Flow<Double?>

}