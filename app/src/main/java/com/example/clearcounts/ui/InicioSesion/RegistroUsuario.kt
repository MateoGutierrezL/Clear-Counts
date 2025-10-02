package com.example.clearcounts.ui.InicioSesion

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.gris
import com.example.clearcounts.ui.theme.negro
import com.example.clearcounts.utils.AlertaCamposVacios
import com.example.clearcounts.utils.OutlinedTextFieldColors

@Composable
fun PantallaRegistro(navController: NavController) {

    // Variables para los campos de texto
    var nombreUsuario by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }

    // Visibilidad de la alerta
    var showDialog by remember { mutableStateOf(false) }

    //Variable para manejar los colores del outlinedText, viene de la carpeta utils
    val coloresOutlined = OutlinedTextFieldColors(
        focusedBorder = AzulEncabezado,
        unfocusedBorder = AzulEncabezado
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.logo_clear_counts),
                contentDescription = "Logo de Clear Counts")

            Text(
                text = "___________________________________________",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AzulEncabezado,
                modifier = Modifier.padding(bottom = 2.dp)
                    .align(Alignment.Start),
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text("Registrar nueva cuenta", fontSize = 25.sp)

            Column {
                Text(
                    text = "Nombre",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = negro,
                    modifier = Modifier.padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = { nombreUsuario = it },
                    placeholder = { Text("Ingrese su nombre completo") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )
            }

            Column {

                Text(
                    text = "Número de celular",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = negro,
                    modifier = Modifier.padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    placeholder = { Text("Ingrese su número") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )

            }

            Column {

                Text(
                    text = "Correo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = negro,
                    modifier = Modifier.padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = correoUsuario,
                    onValueChange = { correoUsuario = it },
                    placeholder = { Text("Ingrese su correo electrónico") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )
            }

            Column {

                Text(
                    text = "Contraseña",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = negro,
                    modifier = Modifier.padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    placeholder = { Text("Ingrese su contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )

            }



            //Verficacion de que todo este debidamente diligenciado
            Button(onClick = {
                if (nombreUsuario.isBlank() || telefono.isBlank() || correoUsuario.isBlank() || contrasena.isBlank()) {
                    showDialog = true // Muestra la alerta
                } else {
                    navController.navigate("InicioSesion")
                }
            },colors = ButtonDefaults.buttonColors(
                containerColor = AzulEncabezado
            )

                ) {
                Text("Registrarme")
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



