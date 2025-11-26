package com.example.clearcounts.ui.screens.InicioSesion

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.ClearCountTheme
import com.example.clearcounts.ui.theme.blanco
import com.example.clearcounts.ui.theme.negro
import com.example.clearcounts.utils.AlertaCamposVacios
import com.example.clearcounts.utils.OutlinedTextFieldColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors


@Composable
fun PantallaInicioSesion(navController: NavController) {

    //Variables que almacenan los datos que son proporcionados en cada uno de los textfield
    var contrasena by remember { mutableStateOf("") }

    var correoUsuario by remember { mutableStateOf("") }

    //Variable para manejar la alerta de campos vacios
    var showDialog by remember { mutableStateOf(false) }

    //Instanciar la clase de LocalFocusManager
    val focusManager = LocalFocusManager.current

    //Variable para manejar los colores del outlinedText, viene de la carpeta utils
    val coloresOutlined = OutlinedTextFieldColors(
        focusedBorder = AzulEncabezado,
        unfocusedBorder = AzulEncabezado
    )
        //Contenedor box que abarca toda la pantalla del celular y centra el contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {// Usar Unit para que se ejecute una sola vez, se implementa en el contenedor principal

                    detectTapGestures(onTap = {
                        // Al detectar un toque (tap), limpiar el foco
                        focusManager.clearFocus()
                    })
                }
            ,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),

            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_clear_counts),
                    contentDescription = "Logo de Clear Counts"
                )

                Text(
                    text = "___________________________________________",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Ingrese a su cuenta",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Column {
                    Text(
                        text = "Correo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .align(Alignment.Start)
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
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                            .align(Alignment.Start)
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

                Row {
                    //Texto para navegar hacia la pantalla de olvido su contraseña
                    Text(
                        text = "¿Olvidó su clave?",
                        modifier = Modifier.clickable {
                            navController.navigate("RecuperarClave") // Ajusta si tienes esta ruta
                        },
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(45.dp))

                    //Texto para navegar hacia la pantalla de registro
                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("¿No tiene cuenta? ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("\n  Regístrese aquí")
                        }
                    }

                    Text(
                        text = annotatedString,
                        modifier = Modifier.clickable {
                            navController.navigate("RegistroUsuario")
                        }
                    )
                }
                //Verficacion de que este debidamente diligenciado
                Button(
                    onClick = {
                        if (correoUsuario.isBlank() || contrasena.isBlank()) {
                            showDialog = true
                        } else {
                            navController.navigate("Inicio")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Ingresar", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
        // Llama a la Alerta
        if (showDialog) {
            AlertaCamposVacios(onDismiss = { showDialog = false }, "inicio de sesión")
        }
}





