package com.example.clearcounts.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM gasto WHERE user_id = :userId")
    fun getAllExpenses(userId: String): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun delete(expenseEntity: ExpenseEntity)

    @Query("SELECT SUM(cantidad) FROM gasto WHERE user_id = :userId")
    fun getTotalExpense(userId: String): Flow<Double?>

    @Query("""
        SELECT categoria, SUM(cantidad) as total
        FROM gasto
        WHERE user_id = :userId
        GROUP BY categoria
        HAVING SUM(cantidad) > 0
    """)
    fun getExpensesByCategory(userId: String): Flow<List<CategoryExpenseSummary>>

    @Query("""
        SELECT SUBSTR(fecha, 4, 7) as mes, SUM(cantidad) as total
        FROM gasto
        WHERE user_id = :userId
        GROUP BY SUBSTR(fecha, 4, 7)
        ORDER BY SUBSTR(fecha, 7, 4) || SUBSTR(fecha, 4, 2)
    """)
    fun getMonthlyExpenses(userId: String): Flow<List<MonthlySummary>>
}

