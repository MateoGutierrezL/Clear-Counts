package com.example.clearcounts.ui.screens.Recurrentes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import com.example.clearcounts.data.local.repository.recurrente.RecurringRepository
import com.example.clearcounts.ui.screens.userIdFlow
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RecurringViewModel @Inject constructor(
    private val repository: RecurringRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userIdFlow = auth.userIdFlow()
    private val userId get() = auth.currentUser?.uid ?: ""

    val ingresos: StateFlow<List<RecurringEntity>> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(emptyList())
            else repository.getAllRecurring(uid).map { list -> list.filter { it.tipo == "ingreso" } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gastos: StateFlow<List<RecurringEntity>> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(emptyList())
            else repository.getAllRecurring(uid).map { list -> list.filter { it.tipo == "gasto" } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardar(entity: RecurringEntity) {
        viewModelScope.launch { repository.insertRecurring(entity.copy(userId = userId)) }
    }

    fun toggleActivo(entity: RecurringEntity) {
        viewModelScope.launch { repository.updateRecurring(entity.copy(activo = !entity.activo)) }
    }

    fun eliminar(entity: RecurringEntity) {
        viewModelScope.launch { repository.deleteRecurring(entity) }
    }

    fun actualizar(entity: RecurringEntity) {
        viewModelScope.launch { repository.updateRecurring(entity) }
    }
}