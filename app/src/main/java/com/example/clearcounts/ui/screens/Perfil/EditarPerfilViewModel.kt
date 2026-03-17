package com.example.clearcounts.ui.screens.Perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.repository.usuario.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
import com.example.clearcounts.data.repository.categoria.CategoryRepository
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.repository.pago.PaymentMethodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val totalTransacciones: StateFlow<Int> = combine(
        incomeRepository.getAllIncomes(),
        expenseRepository.getAllExpenses()
    ) { ingresos, gastos -> ingresos.size + gastos.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMetodosPago: StateFlow<Int> = paymentMethodRepository
        .getAllPaymentMethods()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCategorias: StateFlow<Int> = categoryRepository
        .getAllCategories()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val currentUser: StateFlow<UserEntity?> = userRepository.getCurrentLoggedInUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateAvatar(nuevoIcono: String) {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                userRepository.updateUser(user.copy(avatar = nuevoIcono))
            }
        }
    }

    fun updateUser(nombre: String, telefono: String) {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                userRepository.updateUser(
                    user.copy(
                        nombre = nombre,
                        numero = telefono
                    )
                )
            }
        }
    }
}