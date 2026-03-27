package com.example.clearcounts.ui.screens.Metas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.database.entities.BudgetEntity
import com.example.clearcounts.data.local.repository.presupuesto.BudgetRepository
import com.example.clearcounts.ui.screens.userIdFlow
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MetasViewModel @Inject constructor(
    private val repository: BudgetRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userIdFlow = auth.userIdFlow()
    private val userId get() = auth.currentUser?.uid ?: ""

    val allBudgets: StateFlow<List<BudgetEntity>> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(emptyList())
            else repository.getAllBudgets(uid)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val metas = allBudgets.map { list -> list.filter { it.tipo == "Meta" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deudas = allBudgets.map { list -> list.filter { it.tipo == "Deuda" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val prestamos = allBudgets.map { list -> list.filter { it.tipo == "Te deben" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cantidadMetas = metas.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalDeudas = deudas
        .map { list -> list.sumOf { it.cantidadRequerida - it.cantidadAcumulada } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalTeDeben = prestamos
        .map { list -> list.sumOf { it.cantidadRequerida - it.cantidadAcumulada } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun eliminarBudget(budget: BudgetEntity) {
        viewModelScope.launch { repository.deleteBudget(budget) }
    }

    fun marcarComoHecho(budget: BudgetEntity) {
        viewModelScope.launch {
            repository.updateBudget(budget.copy(cantidadAcumulada = budget.cantidadRequerida))
        }
    }

    fun actualizarCantidadAcumulada(budget: BudgetEntity, cantidad: Double, sumar: Boolean) {
        viewModelScope.launch {
            val nuevaCantidad = if (sumar) budget.cantidadAcumulada + cantidad
            else (budget.cantidadAcumulada - cantidad).coerceAtLeast(0.0)
            repository.updateBudget(budget.copy(cantidadAcumulada = nuevaCantidad))
        }
    }

    fun actualizarCantidadRequerida(budget: BudgetEntity, cantidad: Double, sumar: Boolean) {
        viewModelScope.launch {
            val nuevaCantidad = if (sumar) budget.cantidadRequerida + cantidad
            else (budget.cantidadRequerida - cantidad).coerceAtLeast(0.0)
            repository.updateBudget(budget.copy(cantidadRequerida = nuevaCantidad))
        }
    }

    fun actualizarBudget(budget: BudgetEntity) {
        viewModelScope.launch { repository.updateBudget(budget) }
    }

    fun guardarPrestamo(
        nombre: String, cantidadRequerida: String, cantidadAcumulada: String,
        prestador: String, fechaInicio: String, fechaLimite: String,
        nota: String, tipo: String
    ) {
        viewModelScope.launch {
            repository.insertBudget(
                BudgetEntity(
                    userId = userId,
                    nombre = nombre,
                    cantidadRequerida = cantidadRequerida.toDoubleOrNull() ?: 0.0,
                    cantidadAcumulada = cantidadAcumulada.toDoubleOrNull() ?: 0.0,
                    prestador = prestador,
                    fechaInicio = fechaInicio,
                    fechaLimite = fechaLimite,
                    nota = nota,
                    tipo = tipo
                )
            )
        }
    }
}