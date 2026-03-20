package com.example.clearcounts.ui.screens.InicioSesion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.datastore.SyncDataStore
import com.example.clearcounts.data.local.repository.categoria.CategoryRepository
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.local.repository.pago.PaymentMethodRepository
import com.example.clearcounts.data.local.repository.presupuesto.BudgetRepository
import com.example.clearcounts.data.local.repository.recurrente.RecurringRepository
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val firestoreSync: FirestoreSyncRepository,
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: BudgetRepository,
    private val recurringRepository: RecurringRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val categoryRepository: CategoryRepository,
    private val auth: FirebaseAuth,
    private val syncDataStore: SyncDataStore
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState

    companion object {
        private const val UN_DIA = 86400000L // 1 hora en milisegundos
    }

    fun sincronizarDesdFirestore() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val ultimaSync = syncDataStore.getUltimaSync()
                val ahora = System.currentTimeMillis()
                val tiempoTranscurrido = ahora - ultimaSync

                // Solo sincroniza si pasó más de 1 hora
                if (tiempoTranscurrido < UN_DIA) {
                    Log.d("SyncViewModel", "Sync omitida, última hace ${tiempoTranscurrido/60000} minutos")
                    _syncState.value = SyncState.Success
                    return@launch
                }

                _syncState.value = SyncState.Loading

                val ingresos = async { firestoreSync.descargarIngresos(userId) }
                val gastos = async { firestoreSync.descargarGastos(userId) }
                val presupuesto = async { firestoreSync.descargarPresupuesto(userId) }
                val recurrentes = async { firestoreSync.descargarRecurrentes(userId) }
                val metodosPago = async { firestoreSync.descargarMetodosPago(userId) }
                val categorias = async { firestoreSync.descargarCategorias(userId) }

                ingresos.await().forEach { incomeRepository.insertIncomeLocal(it) }
                gastos.await().forEach { expenseRepository.insertExpenseLocal(it) }
                presupuesto.await().forEach { budgetRepository.insertBudgetLocal(it) }
                recurrentes.await().forEach { recurringRepository.insertRecurringLocal(it) }
                metodosPago.await().forEach { paymentMethodRepository.insertLocal(it) }
                categorias.await().forEach { categoryRepository.insertCategoriaLocal(it) }

                // Guarda el timestamp de esta sync
                syncDataStore.guardarUltimaSync()

                _syncState.value = SyncState.Success

            } catch (e: Exception) {
                Log.e("SyncViewModel", "Error: ${e.message}")
                _syncState.value = SyncState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Fuerza sincronización ignorando el tiempo
    fun forzarSincronizacion() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _syncState.value = SyncState.Loading
            try {
                val ingresos = async { firestoreSync.descargarIngresos(userId) }
                val gastos = async { firestoreSync.descargarGastos(userId) }
                val presupuesto = async { firestoreSync.descargarPresupuesto(userId) }
                val recurrentes = async { firestoreSync.descargarRecurrentes(userId) }
                val metodosPago = async { firestoreSync.descargarMetodosPago(userId) }
                val categorias = async { firestoreSync.descargarCategorias(userId) }

                ingresos.await().forEach { incomeRepository.insertIncomeLocal(it) }
                gastos.await().forEach { expenseRepository.insertExpenseLocal(it) }
                presupuesto.await().forEach { budgetRepository.insertBudgetLocal(it) }
                recurrentes.await().forEach { recurringRepository.insertRecurringLocal(it) }
                metodosPago.await().forEach { paymentMethodRepository.insertLocal(it) }
                categorias.await().forEach { categoryRepository.insertCategoriaLocal(it) }

                syncDataStore.guardarUltimaSync()
                _syncState.value = SyncState.Success
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed interface SyncState {
    object Idle : SyncState
    object Loading : SyncState
    object Success : SyncState
    data class Error(val message: String) : SyncState
}