package com.example.clearcounts.data.local.repository.pago

import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

interface PaymentMethodRepository {
    fun getAllPaymentMethods(userId: String): Flow<List<PaymentMethodEntity>>
    suspend fun insert(method: PaymentMethodEntity)

    suspend fun insertLocal(method: PaymentMethodEntity)
    suspend fun delete(method: PaymentMethodEntity)
    suspend fun insertDefaultMethods(userId: String)
}