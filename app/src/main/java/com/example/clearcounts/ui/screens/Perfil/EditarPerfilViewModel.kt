package com.example.clearcounts.ui.screens.Perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.database.entities.UserEntity
import com.example.clearcounts.data.local.repository.categoria.CategoryRepository
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.local.repository.pago.PaymentMethodRepository
import com.example.clearcounts.data.local.repository.usuario.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userId get() = auth.currentUser?.uid ?: ""

    val totalTransacciones: StateFlow<Int> = combine(
        incomeRepository.getAllIncomes(userId),
        expenseRepository.getAllExpenses(userId)
    ) { ingresos, gastos -> ingresos.size + gastos.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMetodosPago: StateFlow<Int> = paymentMethodRepository
        .getAllPaymentMethods(userId)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCategorias: StateFlow<Int> = categoryRepository
        .getAllCategories(userId)
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