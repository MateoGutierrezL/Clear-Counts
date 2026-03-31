package com.example.clearcounts.ui.screens.Categorias

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.R
import com.example.clearcounts.data.local.database.entities.CategoryEntity
import com.example.clearcounts.data.local.repository.categoria.CategoryRepository
import com.example.clearcounts.data.local.repository.notificacion.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CategoriasViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val notificationRepository: NotificationRepository,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
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
                titulo = context.getString(R.string.nueva_categor_a_creada),
                mensaje = context.getString(
                    R.string.la_categor_a_fue_agregada_a_tus,
                    nombre,
                    if (tipo == "gasto") context.getString(R.string.gasto) else context.getString(R.string.ingreso)
                )
            )
        }
    }


    fun deleteCategoria(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.deleteCategoria(category)
        }
    }
}