package com.example.clearcounts.ui.Perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material3.Icon
import com.example.clearcounts.R
import com.example.clearcounts.ui.Pantallas
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.negro
import com.example.clearcounts.utils.OutlinedTextFieldColors

@Composable
fun EditarPerfil(navController: NavController){

    //Variables de los datos del usuario

    var nombreUsuario by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    //Variable para manejar los colores del outlinedText, viene de la carpeta utils
    val coloresOutlined = OutlinedTextFieldColors(
        focusedBorder = AzulEncabezado,
        unfocusedBorder = AzulEncabezado
    )


    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center,

    )
    {
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopStart) // Lo alinea arriba y a la izquierda
                .padding(top = 10.dp, start = 5.dp) // Añade un poco de margen para que no esté pegado
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = negro, // O el color que desees para el icono
                modifier = Modifier.size(30.dp)
            )
        }

        Column(modifier = Modifier.fillMaxSize()
            .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Mi perfil",
                fontSize = 30.sp,
                color = negro,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 15.dp)
            )

            Text(
                text = "____________________",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AzulEncabezado,
                modifier = Modifier.padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Image(painter = painterResource(id = R.drawable.user),
                contentDescription = "Icono de usuario",
                modifier = Modifier.size(120.dp))

            Column {
                Text(
                    text = "Nombre",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = negro,
                    modifier = Modifier.padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(value = nombreUsuario,
                    onValueChange = { nombreUsuario= it },
                    placeholder = {Text("Ingrese su nombre completo")},
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

            Button(onClick = {



            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulEncabezado)
            )
            {
                Text("Guardar")
            }
        }
    }

}