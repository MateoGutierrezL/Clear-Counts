package com.example.clearcounts.ui.InicioSesion

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.clearcounts.ui.theme.blanco

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioSesion(navController: NavController){

    //Variables que almacenan los datos que son proporcionados en cada uno de los textfield

    var contrasena by remember { mutableStateOf("") }

    var correoUsuario by remember { mutableStateOf("") }

    //Variable para manejar la alerta de campos vacios
    var showDialog by remember { mutableStateOf(false) }

    //Contenedor box que abarca toda la pantalla del celular y centra el contenido

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center

        )
    {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)){

            Image(painter = painterResource(id = R.drawable.logo_clear_counts),
                contentDescription = "Logo de Clear Counts")

            Text("Ingrese a su cuenta", fontSize = 30.sp)

            TextField(value = correoUsuario, onValueChange = { correoUsuario= it },
                placeholder = {Text("Correo electronico")},
                singleLine = true
            )

            TextField(value = contrasena, onValueChange = { contrasena= it },
                placeholder = {Text("Contraseña")},
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            //Verficacion de que este debidamente diligenciado
            Button(onClick = {
                if (correoUsuario.isBlank() || contrasena.isBlank()) {
                    showDialog = true // Muestra la alerta
                } else {
                    navController.navigate("Inicio")
                }
            }) {
                Text("Ingresar")
            }

            //Texto para navegar hacia la pantalla de registro
            val annotatedString = buildAnnotatedString {
                append("¿No tienes una cuenta? ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append("Regístrate aquí")
                }
            }

            Text(
                text = annotatedString,
                modifier = Modifier.clickable {
                    navController.navigate("RegistroUsuario")
                }
            )

        }
    }

    // Llama a la Alerta
    if (showDialog) {
        AlertaCamposVacios(onDismiss = { showDialog = false }, "inicio de sesión")
    }

}


