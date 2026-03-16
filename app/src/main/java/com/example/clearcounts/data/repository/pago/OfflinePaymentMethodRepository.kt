package com.example.clearcounts.data.repository.pago

import com.example.clearcounts.data.database.dao.PaymentMethodDao
import com.example.clearcounts.data.database.entities.PaymentMethodEntity
import javax.inject.Inject

class OfflinePaymentMethodRepository @Inject constructor(
    private val paymentMethodDao: PaymentMethodDao
) : PaymentMethodRepository {

    override fun getAllPaymentMethods() = paymentMethodDao.getAllPaymentMethods()

    override suspend fun insert(method: PaymentMethodEntity) = paymentMethodDao.insert(method)

    override suspend fun delete(method: PaymentMethodEntity) = paymentMethodDao.delete(method)

    override suspend fun insertDefaultMethods() {
        if (paymentMethodDao.getCount() == 0) {
            paymentMethodDao.insertAll(DEFAULT_PAYMENT_METHODS)
        }
    }

    companion object {
        val DEFAULT_PAYMENT_METHODS = listOf(
            PaymentMethodEntity(nombre = "Efectivo", icono = "efectivo", esDefault = true),
            PaymentMethodEntity(nombre = "Débito", icono = "debito", esDefault = true),
            PaymentMethodEntity(nombre = "Crédito", icono = "credito", esDefault = true),
        )
    }
}