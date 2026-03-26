package com.example.clearcounts.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentMethodDao {

    @Query("SELECT * FROM payment_method WHERE user_id = :userId")
    fun getAllPaymentMethods(userId: String): Flow<List<PaymentMethodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(methods: List<PaymentMethodEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(method: PaymentMethodEntity)

    @Delete
    suspend fun delete(method: PaymentMethodEntity)

    @Query("SELECT COUNT(*) FROM payment_method WHERE user_id = :userId")
    suspend fun getCountByUser(userId: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM payment_method WHERE nombre = :nombre AND user_id = :userId)")
    suspend fun existsByName(nombre: String, userId: String): Boolean
}