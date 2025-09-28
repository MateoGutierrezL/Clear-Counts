package com.example.clearcounts.ui.InicioSesion

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.clearcounts.ui.theme.negro
import com.example.clearcounts.utils.AlertaCamposVacios
import com.example.clearcounts.utils.OutlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioSesion(navController: NavController){

    //Variables que almacenan los datos que son proporcionados en cada uno de los textfield

    var contrasena by remember { mutableStateOf("") }

    var correoUsuario by remember { mutableStateOf("") }

    //Variable para manejar la alerta de campos vacios
    var showDialog by remember { mutableStateOf(false) }

    //Variable para manejar los colores del outlinedText, viene de la carpeta utils
    val coloresOutlined = OutlinedTextFieldColors(
        focusedBorder = AzulEncabezado,
        unfocusedBorder = AzulEncabezado
    )

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

            Text(
                text = "___________________________________________",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AzulEncabezado,
                modifier = Modifier.padding(bottom = 10.dp)
                    .align(Alignment.Start),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Ingrese a su cuenta", fontSize = 25.sp)

            Column {
                Text(
                    text = "Correo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = negro,
                    modifier = Modifier.padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(value = correoUsuario,
                    onValueChange = { correoUsuario= it },
                    placeholder = {Text("Ingrese su correo electrónico")},
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
                   modifier = Modifier.padding(bottom = 2.dp)
                       .align(Alignment.Start),
               )

               OutlinedTextField(value = contrasena,
                   onValueChange = { contrasena= it },
                   placeholder = {Text("Ingrese su contraseña")},
                   visualTransformation = PasswordVisualTransformation(),
                   singleLine = true,
                   shape = RoundedCornerShape(16.dp),
                   colors = coloresOutlined
               )
           }

            Row {
                //Texto para navegar hacia la pantalla de olvido su contraseña
                val annotatedStringContraseña = buildAnnotatedString {
                    append("¿Olvidó su clave? ")
                }

                Text(
                    text = annotatedStringContraseña,
                    modifier = Modifier.clickable {
                        navController.navigate("")
                    }
                )

                Spacer(modifier = Modifier.width(45.dp))

                //Texto para navegar hacia la pantalla de registro
                val annotatedString = buildAnnotatedString {
                    append("¿No tiene cuenta? ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
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
            Button(onClick = {
                if (correoUsuario.isBlank() || contrasena.isBlank()) {
                    showDialog = true // Muestra la alerta
                } else {
                    navController.navigate("Inicio")
                }

            },
                colors = ButtonDefaults.buttonColors(
                containerColor = AzulEncabezado)

            ) {
                Text("Ingresar")
            }



        }
    }

    // Llama a la Alerta
    if (showDialog) {
        AlertaCamposVacios(onDismiss = { showDialog = false }, "inicio de sesión")
    }

}


