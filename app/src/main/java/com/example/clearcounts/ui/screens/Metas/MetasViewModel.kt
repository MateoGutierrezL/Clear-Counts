package com.example.clearcounts.ui.screens.Metas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.database.entities.BudgetEntity
import com.example.clearcounts.data.repository.presupuesto.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MetasViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

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