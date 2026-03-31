package com.example.clearcounts.data.local.repository.pago

import android.util.Log
import com.example.clearcounts.data.local.database.UserDatabase
import com.example.clearcounts.data.local.database.dao.PaymentMethodDao
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class OfflinePaymentMethodRepository @Inject constructor(
    private val paymentMethodDao: PaymentMethodDao,
    private val firestoreSync: FirestoreSyncRepository,
    private val auth: FirebaseAuth
) : PaymentMethodRepository {

    private val userId get() = auth.currentUser?.uid ?: ""

    override fun getAllPaymentMethods(userId: String) =
        paymentMethodDao.getAllPaymentMethods(userId)

    override suspend fun insert(method: PaymentMethodEntity) {
        paymentMethodDao.insert(method)
        firestoreSync.subirMetodoPago(userId, method)
    }

    override suspend fun insertLocal(method: PaymentMethodEntity) {
        paymentMethodDao.insert(method)
    }

    override suspend fun delete(method: PaymentMethodEntity) {
        paymentMethodDao.delete(method)
        firestoreSync.eliminarMetodoPago(userId, method.firestoreId)
    }

    override suspend fun insertDefaultMethods(userId: String) {
        UserDatabase.DEFAULT_PAYMENT_METHODS.forEach { metodo ->
            val existe = paymentMethodDao.existsByName(metodo.nombre, userId)
            if (!existe) {
                paymentMethodDao.insert(metodo.copy(userId = userId))
            }
        }
    }

    companion object {
        val DEFAULT_PAYMENT_METHODS = listOf(
            PaymentMethodEntity(nombre = "Efectivo", icono = "efectivo", esDefault = true),
            PaymentMethodEntity(nombre = "Débito", icono = "tarjetaazul", esDefault = true),
            PaymentMethodEntity(nombre = "Crédito", icono = "tarjetaroja", esDefault = true),
        )
    }
}