package com.example.clearcounts.ui.screens.Barras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.clearcounts.R

enum class DrawerItem (
    val icon: Int,
    val text: String
){
    EXPORT_PDF(icon = R.drawable.documento, "Exportar PDF/CSV"),
    PERSONALIZACION(icon = R.drawable.equalizer, text = "Personalización"),
    CONTACT(icon = R.drawable.phone_call, text = "Contactanos"),
    TUTORIAL(icon = R.drawable.question, text = "Tutorial"),
    LOG_OUT(icon = R.drawable.log_out, text = "Cerrar sesión")

}