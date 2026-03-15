package com.example.clearcounts.data.repository.pago

import com.example.clearcounts.data.database.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

interface PaymentMethodRepository {
    fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>>
    suspend fun insert(method: PaymentMethodEntity)
    suspend fun delete(method: PaymentMethodEntity)
    suspend fun insertDefaultMethods()
}