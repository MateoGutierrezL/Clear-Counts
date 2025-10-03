package com.example.clearcounts.ui.Barras

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material3.MaterialTheme
import com.example.clearcounts.R
import com.example.clearcounts.ui.Pantallas
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.blanco
import com.example.clearcounts.ui.theme.negro

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMenuClick: () -> Unit,
    selectedIcon: MutableState<String>,
    navigationController: NavController
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AzulEncabezado,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = blanco
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = negro)) {
                            append("Clear")
                        }
                        withStyle(style = SpanStyle(color = blanco)) {
                            append("Counts")
                        }
                    },
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
                    tint = negro
                )
            }
        },
        actions = {
            // Icono de mensajes
            NavigationBarItem(
                selected = selectedIcon.value == "pqr",
                onClick = {
                    selectedIcon.value = "pqr"
                    navigationController.navigate(Pantallas.Perfil.pantalla) {
                        popUpTo(0)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.customer_service),
                        contentDescription = "preguntas y comentarios",
                        modifier = Modifier.size(30.dp),
                        tint = if (selectedIcon.value == "pqr") Color.White else Color.Black
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Black,
                    indicatorColor = Color.Transparent
                )
            )

            // Icono de notificaciones
            NavigationBarItem(
                selected = selectedIcon.value == "notification",
                onClick = {
                    selectedIcon.value = "notification"
                    navigationController.navigate(Pantallas.Perfil.pantalla) {
                        popUpTo(0)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Notificaciones",
                        modifier = Modifier.size(30.dp),
                        tint = if (selectedIcon.value == "notification") Color.White else Color.Black
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Black,
                    indicatorColor = Color.Transparent
                )
            )
        }
    )
}

 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMenuClick: () -> Unit,
    selectedIcon: MutableState<String>,
    navigationController: NavController
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AzulEncabezado,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = blanco
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = negro)) {
                            append("Clear")
                        }
                        withStyle(style = SpanStyle(color = blanco)) {
                            append("Counts")
                        }
                    },
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
                    tint = negro
                )
            }
        },
        // Los iconos de acciones ya están a la derecha por defecto.
        actions = {
            // CAMBIO 1: Reemplazar NavigationBarItem por IconButton para el icono de mensajes
            IconButton(
                onClick = {
                    selectedIcon.value = "pqr"
                    navigationController.navigate(Pantallas.Perfil.pantalla) {
                        popUpTo(0)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.customer_service),
                    contentDescription = "preguntas y comentarios",
                    modifier = Modifier.size(30.dp),
                    // Usamos el estado para el color de tint
                    tint = if (selectedIcon.value == "pqr") Color.White else Color.Black
                )
            }

            // CAMBIO 2: Reemplazar NavigationBarItem por IconButton para el icono de notificaciones
            IconButton(
                onClick = {
                    selectedIcon.value = "notification"
                    navigationController.navigate(Pantallas.Perfil.pantalla) {
                        popUpTo(0)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notificaciones",
                    modifier = Modifier.size(30.dp),
                    // Usamos el estado para el color de tint
                    tint = if (selectedIcon.value == "notification") Color.White else Color.Black
                )
            }
        }
    )
}