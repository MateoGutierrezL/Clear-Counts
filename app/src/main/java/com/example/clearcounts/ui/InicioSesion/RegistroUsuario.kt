package com.example.clearcounts.ui.InicioSesion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
@Composable
fun PantallaRegistro(navController: NavController) {

    // Variables para los campos de texto
    var nombreUsuario by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }

    // Visibilidad de la alerta
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Registro", fontSize = 30.sp)

            TextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                placeholder = { Text("Nombre completo") },
                singleLine = true
            )
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                placeholder = { Text("Telefono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
            TextField(
                value = correoUsuario,
                onValueChange = { correoUsuario = it },
                placeholder = { Text("Correo electronico") },
                singleLine = true
            )
            TextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                placeholder = { Text("Contraseña") },
                singleLine = true
            )

            //Verficacion de que todo este debidamente diligenciado
            Button(onClick = {
                if (nombreUsuario.isBlank() || telefono.isBlank() || correoUsuario.isBlank() || contrasena.isBlank()) {
                    showDialog = true // Muestra la alerta
                } else {
                    navController.navigate("InicioSesion")
                }
            }) {
                Text("Registrarse")
            }

            //Texto para la navegación hacia el inicio de sesión
            val annotatedString = buildAnnotatedString {
                append("¿Ya tienes una cuenta? ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append("Inicia sesión")
                }
            }

            Text(
                text = annotatedString,
                modifier = Modifier.clickable {
                    navController.navigate("InicioSesion")
                }
            )

        }
    }

    // Llama a la Alerta solo si la variable de estado es true
    if (showDialog) {
        AlertaCamposVacios(onDismiss = { showDialog = false }, "Registro")
    }
}

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

