package com.example.clearcounts.ui.screens.IngresosGastos

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.R
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.local.repository.notificacion.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngresosGastosViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val notificationRepository: NotificationRepository,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val userId get() = auth.currentUser?.uid ?: ""

    fun insertIncome(incomeEntity: IncomeEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                incomeRepository.insertIncome(incomeEntity.copy(userId = userId))
                notificationRepository.insertNotification(
                    titulo = context.getString(R.string.ingreso_registrado),
                    mensaje = context.getString(R.string.se_registr_un_ingreso_de_en,
                        incomeEntity.cantidad.toString(), incomeEntity.categoria)
                )
                onSuccess()
            } catch (e: Exception) { Log.e("ingreso", "Fallo: ${e.message}") }
        }
    }

    fun insertExpense(expenseEntity: ExpenseEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                expenseRepository.insertExpense(expenseEntity.copy(userId = userId))
                notificationRepository.insertNotification(
                    titulo = context.getString(R.string.gasto_registrado),
                    mensaje = context.getString(R.string.se_registr_un_gasto_de_en,
                        expenseEntity.cantidad.toString(), expenseEntity.categoria)
                )
                onSuccess()
            } catch (e: Exception) { Log.e("gasto", "Fallo: ${e.message}") }
        }
    }
}