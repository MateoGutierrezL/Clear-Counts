package com.example.clearcounts.ui.Barras

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.blanco
import com.example.clearcounts.ui.theme.negro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMenuClick: () -> Unit
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
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.customer_service),
                    contentDescription = "Mensajes",
                    modifier = Modifier.size(30.dp),
                    tint = negro
                )
            }

            // Icono de notificaciones
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notificaciones",
                    modifier = Modifier.size(30.dp),
                    tint = negro
                )
            }
        }
    )
}

