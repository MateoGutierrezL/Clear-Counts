package com.example.clearcounts.ui.screens.Categorias

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clearcounts.data.IncomeRepository
import com.example.clearcounts.data.database.entities.IncomeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngresosViewModel @Inject constructor(

    private val incomeRepository: IncomeRepository
): ViewModel(){

    fun insertIncome(
        incomeEntity: IncomeEntity
    ){
        viewModelScope.launch {

            try {
                incomeRepository.insertIncome(incomeEntity)

                Log.e("ingreso", "Funciona")

            }catch (e: Exception){

                Log.e("ingreso", "Fallo insertando el ingreso: ${e.message}")
            }
        }
    }
}