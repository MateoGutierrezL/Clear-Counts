package com.example.clearcounts.ui.screens.Metas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.database.entities.BudgetEntity
import com.example.clearcounts.data.repository.presupuesto.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MetasViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    val allBudgets: StateFlow<List<BudgetEntity>> = repository.getAllBudgets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val metas: StateFlow<List<BudgetEntity>> = allBudgets
        .map { list -> list.filter { it.tipo == "Meta" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deudas: StateFlow<List<BudgetEntity>> = allBudgets
        .map { list -> list.filter { it.tipo == "Deuda" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val prestamos: StateFlow<List<BudgetEntity>> = allBudgets
        .map { list -> list.filter { it.tipo == "Te deben" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cantidadMetas: StateFlow<Int> = metas
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalDeudas: StateFlow<Double> = deudas
        .map { list -> list.sumOf { it.cantidadRequerida - it.cantidadAcumulada } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalTeDeben: StateFlow<Double> = prestamos
        .map { list -> list.sumOf { it.cantidadRequerida - it.cantidadAcumulada } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun eliminarBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }

    fun marcarComoHecho(budget: BudgetEntity) {
        viewModelScope.launch {
            repository.insertBudget(
                budget.copy(cantidadAcumulada = budget.cantidadRequerida)
            )
        }
    }
    fun guardarPrestamo(
        nombre: String,
        cantidadRequerida: String,
        cantidadAcumulada: String,
        prestador: String,
        fechaInicio: String,
        fechaLimite: String,
        nota: String,
        tipo: String // El "titulo" que recibes en el Composable
    ) {
        viewModelScope.launch {
            val budget = BudgetEntity(
                // Ajusta estos campos según tu BudgetEntity real
                nombre = nombre,
                cantidadRequerida = cantidadRequerida.toDoubleOrNull() ?: 0.0,
                cantidadAcumulada = cantidadAcumulada.toDoubleOrNull() ?: 0.0,
                prestador = prestador,
                fechaInicio = fechaInicio,
                fechaLimite = fechaLimite,
                nota = nota,
                tipo = tipo // Aquí guardas si es "Prestamo", "Meta", etc.
            )
            repository.insertBudget(budget)
        }
    }
}