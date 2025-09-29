package com.example.clearcounts.ui.Perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.Pink40
import com.example.clearcounts.ui.theme.negro

@Composable
fun Perfil(){

    //Variables de los datos del usuario

    var nombreUsuario by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }


    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Column(modifier = Modifier.fillMaxSize()
            .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Mi perfil",
                fontSize = 30.sp,
                color = negro,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                text = "____________________",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AzulEncabezado,
                modifier = Modifier.padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            )

            Image(painter = painterResource(id = R.drawable.user),
                contentDescription = "Icono de usuario",
                modifier = Modifier.padding(bottom = 20.dp).size(130.dp))

            //Aqui debe de ir el nombre de usuario con su correo
            Text(
                text = "Mateo Gutiérrez Laverde",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = negro,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "mateo@gmail.com",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = negro,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "_________________________________________",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AzulEncabezado,
                modifier = Modifier.padding(bottom = 20.dp)
                    .align(Alignment.CenterHorizontally),
            )

            Button(onClick = {""},
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulEncabezado)
            )
            {
                Text("Editar perfil")
            }
        }
    }
}