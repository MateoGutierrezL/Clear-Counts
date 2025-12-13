package com.example.clearcounts.ui.screens.Perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.UserSessionViewModel
import com.example.clearcounts.ui.theme.ClearCountTheme


@Composable
fun Perfil(
    navegarPantallaEditarPerfil: () -> Unit,
    viewModel: UserSessionViewModel = hiltViewModel()
){

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val nombreUsuario = currentUser?.nombre ?: "Usuario invitado"
    val correoUsuario = currentUser?.correo ?: "No disponible"

    ClearCountTheme {

        Box(modifier = Modifier
            .fillMaxSize().
            background(color = MaterialTheme.colorScheme.background),
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
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 100.dp)
                        .padding(top = 20.dp)
                        .padding(bottom = 40.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )

                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Icono de usuario",
                    modifier = Modifier.padding(bottom = 20.dp).size(130.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )

                //Aqui debe de ir el nombre de usuario con su correo
                Text(
                    text = nombreUsuario,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = correoUsuario,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 60.dp)
                        .padding(bottom = 30.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )

                Button(onClick = {
                    navegarPantallaEditarPerfil()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary)
                )
                {
                    Text("Editar perfil",
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

    }

}