package com.example.clearcounts.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Query("SELECT * FROM ingreso WHERE user_id = :userId")
    fun getAllIncomes(userId: String): Flow<List<IncomeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incomeEntity: IncomeEntity)

    @Delete
    suspend fun delete(incomeEntity: IncomeEntity)

    @Query("SELECT SUM(cantidad) FROM ingreso WHERE user_id = :userId")
    fun getTotalIncome(userId: String): Flow<Double?>

    @Query("""
        SELECT SUBSTR(fecha, 4, 7) as mes, SUM(cantidad) as total
        FROM ingreso
        WHERE user_id = :userId
        GROUP BY SUBSTR(fecha, 4, 7)
        ORDER BY SUBSTR(fecha, 7, 4) || SUBSTR(fecha, 4, 2)
    """)
    fun getMonthlyIncomes(userId: String): Flow<List<MonthlySummary>>
}
