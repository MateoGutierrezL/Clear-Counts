package com.example.clearcounts.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController

@Composable
fun ingresos(navController: NavController, icono: Int, nombre: String){

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Icon(
            painter = painterResource(icono),
            contentDescription = nombre,
            tint = Color.Unspecified
        )
        Text(
            text = nombre
        )
    }

}
