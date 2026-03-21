package com.example.clearcounts.ui.screens.Barras

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import com.example.clearcounts.R

enum class DrawerItem (
    val icon: Int,
    @StringRes val text: Int
){
    PERFIL(icon = R.drawable.user, text = R.string.perfil),

    AJUSTES(icon = R.drawable.ajustes, text = R.string.ajustes),
    EXPORT_PDF(icon = R.drawable.documento, text = R.string.exportar_pdf_csv),
    CONTACT(icon = R.drawable.phone_call, text = R.string.contactanos),
    TUTORIAL(icon = R.drawable.question, text = R.string.tutorial),
    LOG_OUT(icon = R.drawable.log_out, text = R.string.cerrar_sesion)

}