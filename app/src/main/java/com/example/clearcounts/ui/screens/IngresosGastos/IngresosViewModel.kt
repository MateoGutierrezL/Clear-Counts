package com.example.clearcounts.ui.screens.IngresosGastos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.ExpenseRepository
import com.example.clearcounts.data.IncomeRepository
import com.example.clearcounts.data.OfflineExpenseRepository
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IngresosGastosViewModel @Inject constructor(

    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel(){

    fun insertIncome(
        incomeEntity: IncomeEntity,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {

            try {
                incomeRepository.insertIncome(incomeEntity)
                onSuccess()


                Log.e("gasto", "Funciona")

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
                onSuccess()

                Log.e("ingreso", "Funciona")

            }catch (e: Exception){

                Log.e("ingreso", "Fallo insertando el ingreso: ${e.message}")
            }
        }
    }
}