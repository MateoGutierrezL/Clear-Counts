package com.example.clearcounts.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


//Ejemplo de como funcionan las alertas dentro de compose
@Composable
fun SimpleAlertDialog() {
    // 1. Variable de estado para controlar la visibilidad de la alerta
    var showDialog by remember { mutableStateOf(false) }

    // 2. Un botón que cambia el estado para mostrar la alerta
    TextButton(onClick = { showDialog = true }) {
        Text("Mostrar Alerta")
    }

    // 3. El composable AlertDialog que se muestra si showDialog es true
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Se ejecuta al hacer clic fuera de la alerta
                showDialog = false
            },
            title = {
                Text(text = "Título de la Alerta")
            },
            text = {
                Text(text = "Este es un mensaje de ejemplo para la alerta.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Se ejecuta al hacer clic en el botón de confirmar
                        showDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Se ejecuta al hacer clic en el botón de cancelar
                        showDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}