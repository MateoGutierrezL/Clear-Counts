package com.example.clearcounts.ui.screens.Categorias

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.repository.categoria.CategoryRepository
import com.example.clearcounts.data.database.entities.CategoryEntity
import com.example.clearcounts.data.repository.notificacion.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CategoriasViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val notificationRepository: NotificationRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userId get() = auth.currentUser?.uid ?: ""

    init {
        viewModelScope.launch {
            categoryRepository.insertDefaultCategories(userId)
        }
    }

    val gastos: StateFlow<List<CategoryEntity>> = categoryRepository
        .getCategoriesByType("gasto", userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ingresos: StateFlow<List<CategoryEntity>> = categoryRepository
        .getCategoriesByType("ingreso", userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertCategoria(nombre: String, icono: String, tipo: String) {
        viewModelScope.launch {
            categoryRepository.insertCategoria(
                CategoryEntity(nombre = nombre, icono = icono, tipo = tipo, userId = userId)
            )
            notificationRepository.insertNotification(
                titulo = "Nueva categoría creada",
                mensaje = "La categoría \"$nombre\" fue agregada a tus ${if (tipo == "gasto") "gastos" else "ingresos"}."
            )
        }
    }
}