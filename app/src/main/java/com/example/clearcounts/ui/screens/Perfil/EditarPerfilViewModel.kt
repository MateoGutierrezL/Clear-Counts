package com.example.clearcounts.ui.screens.Perfil

import android.util.Log
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    val currentUser: StateFlow<UserEntity?> = userRepository.getCurrentLoggedInUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    //  Usa el UID de Firebase, no el correo
    private val userIdFlow: StateFlow<String> = MutableStateFlow(
        auth.currentUser?.uid ?: ""
    ).also { flow ->
        auth.addAuthStateListener { firebaseAuth ->
            (flow as MutableStateFlow).value = firebaseAuth.currentUser?.uid ?: ""
        }
    }

    val totalTransacciones: StateFlow<Int> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(0)
            else combine(
                incomeRepository.getAllIncomes(uid),
                expenseRepository.getAllExpenses(uid)
            ) { ingresos, gastos -> ingresos.size + gastos.size }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMetodosPago: StateFlow<Int> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(0)
            else paymentMethodRepository.getAllPaymentMethods(uid).map { it.size }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCategorias: StateFlow<Int> = userIdFlow
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(0)
            else categoryRepository.getAllCategories(uid).map { it.size }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

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
                userRepository.updateUser(user.copy(nombre = nombre, numero = telefono))
            }
        }
    }

    val esUsuarioSocial: StateFlow<Boolean> = currentUser
        .map { user ->
            Log.d("PERFIL", "proveedor: ${user?.proveedor}, correo: ${user?.correo}")
            user?.proveedor == "google" ||
                    user?.proveedor == "facebook" ||
                    user?.correo?.contains("@") == false
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}