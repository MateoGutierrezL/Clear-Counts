package com.example.clearcounts.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clearcounts.data.database.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentMethodDao {

    @Query("SELECT * FROM payment_method WHERE user_id = :userId")
    fun getAllPaymentMethods(userId: String): Flow<List<PaymentMethodEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(methods: List<PaymentMethodEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(method: PaymentMethodEntity)

    @Delete
    suspend fun delete(method: PaymentMethodEntity)

    @Query("SELECT COUNT(*) FROM payment_method WHERE user_id = :userId")
    suspend fun getCountByUser(userId: String): Int
}