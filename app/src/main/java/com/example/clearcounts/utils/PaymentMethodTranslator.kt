package com.example.clearcounts.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.clearcounts.R
import com.example.clearcounts.data.database.entities.PaymentMethodEntity

object PaymentMethodTranslator {

    fun getStringResByNombre(nombre: String): Int? {
        return when (nombre) {
            "Efectivo" -> R.string.payment_efectivo
            "Débito"   -> R.string.payment_debito
            "Crédito"  -> R.string.payment_credito
            else       -> null
        }
    }

    @Composable
    fun PaymentMethodEntity.getNombreTraducido(): String {
        val resId = getStringResByNombre(nombre)
        return if (resId != null) stringResource(resId) else nombre
    }

    fun Context.traducirMetodoPago(nombre: String): String {
        val resId = getStringResByNombre(nombre)
        return if (resId != null) getString(resId) else nombre
    }
}