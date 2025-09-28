package com.example.clearcounts.utils

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun OutlinedTextFieldColors(
    focusedBorder: Color,
    unfocusedBorder: Color,

): TextFieldColors {

    return OutlinedTextFieldDefaults.colors(
        // Colores del Borde Outline
        focusedBorderColor = focusedBorder,
        unfocusedBorderColor = unfocusedBorder,

        /*
        focusedBorderColor = focusedBorder,
        unfocusedBorderColor = unfocusedBorder,
        disabledBorderColor = disabledBorder,

        focusedLabelColor = focusedLabel,
        unfocusedLabelColor = unfocusedLabel,

         */

    )
}



