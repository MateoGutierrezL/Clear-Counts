package com.example.clearcounts.ui.screens.Categorias

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.repository.categoria.CategoryRepository
import com.example.clearcounts.data.database.entities.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CategoriasViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            categoryRepository.insertDefaultCategories()
        }
    }
    val gastos: StateFlow<List<CategoryEntity>> = categoryRepository
        .getCategoriesByType("gasto")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val ingresos: StateFlow<List<CategoryEntity>> = categoryRepository
        .getCategoriesByType("ingreso")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun insertCategoria(nombre: String, icono: String, tipo: String) {
        viewModelScope.launch {
            categoryRepository.insertCategoria(
                CategoryEntity(nombre = nombre, icono = icono, tipo = tipo)
            )
        }
    }

}