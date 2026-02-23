package com.example.clearcounts.ui.screens.IngresosGastos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.ExpenseRepository
import com.example.clearcounts.data.OfflineExpenseRepository
import com.example.clearcounts.data.database.entities.ExpenseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastosViewModel @Inject constructor(

    private val expenseRepository: ExpenseRepository
): ViewModel() {

    fun insertExpense(expenseEntity: ExpenseEntity){

        viewModelScope.launch {

            try {
                expenseRepository.insertExpense(expenseEntity)

                Log.e("gasto", "Funciona")

            }catch (e: Exception){

                Log.e("gasto", "Fallo insertando el gasto: ${e.message}")
            }
        }
    }
}