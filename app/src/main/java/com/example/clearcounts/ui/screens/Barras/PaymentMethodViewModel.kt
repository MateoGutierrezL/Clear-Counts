package com.example.clearcounts.ui.screens.Barras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.database.entities.PaymentMethodEntity
import com.example.clearcounts.data.repository.pago.PaymentMethodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMethodViewModel @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) : ViewModel() {

    val metodosPago: StateFlow<List<PaymentMethodEntity>> = paymentMethodRepository
        .getAllPaymentMethods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            paymentMethodRepository.insertDefaultMethods()
        }
    }

    fun insertMetodoPago(nombre: String, icono: String) {
        viewModelScope.launch {
            paymentMethodRepository.insert(
                PaymentMethodEntity(nombre = nombre, icono = icono, esDefault = false)
            )
        }
    }

    fun deleteMetodoPago(method: PaymentMethodEntity) {
        viewModelScope.launch {
            if (!method.esDefault) {
                paymentMethodRepository.delete(method)
            }
        }
    }
}