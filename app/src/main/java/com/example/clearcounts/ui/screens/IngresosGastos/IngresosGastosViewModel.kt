package com.example.clearcounts.ui.screens.IngresosGastos

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.R
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.data.repository.notificacion.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngresosGastosViewModel @Inject constructor(

    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val notificationRepository: NotificationRepository,
    @ApplicationContext private val context: Context
): ViewModel(){

    fun insertIncome(
        incomeEntity: IncomeEntity,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {

            try {
                incomeRepository.insertIncome(incomeEntity)
                notificationRepository.insertNotification( // 👈
                    titulo = context.getString(R.string.ingreso_registrado),
                    mensaje = context.getString(
                        R.string.se_registr_un_ingreso_de_en,
                        incomeEntity.cantidad.toString(),
                        incomeEntity.categoria
                    )
                )
                onSuccess()


                Log.e("ingreso", "Funciona")

            }catch (e: Exception){

                Log.e("gasto", "Fallo insertando el ingreso: ${e.message}")
            }
        }
    }

    fun insertExpense(
        expenseEntity: ExpenseEntity,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {

            try {
                expenseRepository.insertExpense(expenseEntity)
                notificationRepository.insertNotification( // 👈
                    titulo = context.getString(R.string.gasto_registrado),
                    mensaje = context.getString(
                        R.string.se_registr_un_gasto_de_en,
                        expenseEntity.cantidad.toString(),
                        expenseEntity.categoria
                    )
                )
                onSuccess()

                Log.e("gasto", "Funciona")

            }catch (e: Exception){

                Log.e("gasto", "Fallo insertando el gasto: ${e.message}")
            }
        }
    }
}