package com.example.clearcounts.ui.screens.Barras

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMenuClick: () -> Unit,
    selectedIcon: MutableState<String>,
    navegarPantallaNotificaciones: () -> Unit,
    navegarPantallaPreguntasComentarios: () -> Unit,
    tieneNotificaciones: Boolean = false
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = null,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = "Clear Counts",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Drawer",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        // Los iconos de acciones ya están a la derecha por defecto.
        actions = {
            //Icono de las preguntas y comentarios
            IconButton(
                onClick = {
                    selectedIcon.value = "pqr"
                    navegarPantallaPreguntasComentarios()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.customer_service),
                    contentDescription = "preguntas y comentarios",
                    modifier = Modifier.size(30.dp),
                    tint = if (selectedIcon.value == "pqr") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            //Icono de notificaciones
            IconButton(
                onClick = {
                    selectedIcon.value = "notification"
                    navegarPantallaNotificaciones()
                }
            ) {

                BadgedBox(
                    badge = {
                        if (tieneNotificaciones) {
                            Badge() // 👈 Círculo rojo
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Notificaciones",
                        modifier = Modifier.size(30.dp),
                        tint = if (selectedIcon.value == "notification") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    )

}

