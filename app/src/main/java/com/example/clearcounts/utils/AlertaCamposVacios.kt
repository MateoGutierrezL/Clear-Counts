package com.example.clearcounts.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.clearcounts.R

//Funcion de la alerta
@Composable
fun AlertaCamposVacios(onDismiss: () -> Unit, titulo: String, mensaje: String) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titulo) },
        text = { Text(text = mensaje) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.aceptar))
            }
        }
    )
}