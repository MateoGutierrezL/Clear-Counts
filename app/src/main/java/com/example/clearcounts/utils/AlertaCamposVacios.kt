package com.example.clearcounts.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

//Funcion de la alerta
@Composable
fun AlertaCamposVacios(onDismiss: () -> Unit, mensaje: String) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Llenar todos los campos") },
        text = { Text(text = "Para poder realizar el $mensaje es necesario diligenciar todos los campos.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}