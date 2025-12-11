package com.example.clearcounts.ui.screens.Notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun Notificaciones() {
    Box(modifier = Modifier.fillMaxSize()) {
        var todo by rememberSaveable {
            mutableStateOf(true)
        }
        var mensajes by rememberSaveable {
            mutableStateOf(false)
        }
        var alertas by rememberSaveable {
            mutableStateOf(false)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            todo = true
                            mensajes = false
                            alertas = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = "Todo",
                            textDecoration = TextDecoration.Underline,
                            color = if(todo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Button(
                        onClick = {
                            mensajes = true
                            todo = false
                            alertas = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = "Mensajes",
                            textDecoration = TextDecoration.Underline,
                            color = if(mensajes) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Button(
                        onClick = {
                            alertas = true
                            todo = false
                            mensajes = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = "Alertas",
                            textDecoration = TextDecoration.Underline,
                            color = if(alertas) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )

                if(alertas){
                    DataSource.alertas.forEach { alerta ->
                        Alertas(alerta)
                    }
                }
            }


        }

    }
}