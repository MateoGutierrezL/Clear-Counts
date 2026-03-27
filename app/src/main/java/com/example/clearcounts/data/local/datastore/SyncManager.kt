package com.example.clearcounts.data.local.datastore

import android.util.Log
import com.example.clearcounts.data.local.database.dao.BudgetDao
import com.example.clearcounts.data.local.database.dao.CategoryDao
import com.example.clearcounts.data.local.database.dao.ExpenseDao
import com.example.clearcounts.data.local.database.dao.IncomeDao
import com.example.clearcounts.data.local.database.dao.PaymentMethodDao
import com.example.clearcounts.data.local.database.dao.RecurringDao
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val firestoreSync: FirestoreSyncRepository,
    private val incomeDao: IncomeDao,
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao,
    private val recurringDao: RecurringDao,
    private val paymentMethodDao: PaymentMethodDao,
    private val categoryDao: CategoryDao,
    private val syncDataStore: SyncDataStore,
    private val auth: FirebaseAuth
) {
    suspend fun sincronizarDesdeFirestore() {
        val userId = auth.currentUser?.uid ?: return
        try {
            coroutineScope {
                val ingresos = async { firestoreSync.descargarIngresos(userId) }
                val gastos = async { firestoreSync.descargarGastos(userId) }
                val presupuestos = async { firestoreSync.descargarPresupuesto(userId) }
                val recurrentes = async { firestoreSync.descargarRecurrentes(userId) }
                val metodos = async { firestoreSync.descargarMetodosPago(userId) }
                val categorias = async { firestoreSync.descargarCategorias(userId) }

                ingresos.await().forEach { incomeDao.insert(it) }
                gastos.await().forEach { expenseDao.insert(it) }
                presupuestos.await().forEach { budgetDao.insert(it) }
                recurrentes.await().forEach { recurringDao.insert(it) }
                metodos.await().forEach { paymentMethodDao.insert(it) }
                categorias.await().forEach { categoryDao.insertCategoria(it) }
            }
            syncDataStore.guardarUltimaSync()
            Log.d("SYNC", "Sincronización completa para $userId")
        } catch (e: Exception) {
            Log.e("SYNC", "Error en sincronización: ${e.message}")
        }
    }
}