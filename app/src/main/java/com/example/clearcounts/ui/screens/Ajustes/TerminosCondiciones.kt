package com.example.clearcounts.ui.screens.Ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.clearcounts.R
import com.example.clearcounts.utils.ContactoLegal
import com.example.clearcounts.utils.ItemLista
import com.example.clearcounts.utils.NotaLegal
import com.example.clearcounts.utils.SeccionLegal
import com.example.clearcounts.utils.SubseccionLegal

@Composable
fun TerminosCondiciones(
    botonVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(vertical = 12.dp, horizontal = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = botonVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.terminos_y_condiciones),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.ultima_actualizacion_terminos),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Intro
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.terminos_intro),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Secciones
            item { SeccionLegal(numero = "1", titulo = stringResource(R.string.terminos_s1_titulo), icono = Icons.Default.Gavel) {
                Text(stringResource(R.string.terminos_s1_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

            item { SeccionLegal(numero = "2", titulo = stringResource(R.string.terminos_s2_titulo), icono = Icons.Default.Apps) {
                ItemLista(stringResource(R.string.terminos_s2_item1))
                ItemLista(stringResource(R.string.terminos_s2_item2))
                ItemLista(stringResource(R.string.terminos_s2_item3))
                ItemLista(stringResource(R.string.terminos_s2_item4))
                ItemLista(stringResource(R.string.terminos_s2_item5))
                ItemLista(stringResource(R.string.terminos_s2_item6))
                ItemLista(stringResource(R.string.terminos_s2_item7))
            }}

            item { SeccionLegal(numero = "3", titulo = stringResource(R.string.terminos_s3_titulo), icono = Icons.Default.Block) {
                ItemLista(stringResource(R.string.terminos_s3_item1))
                ItemLista(stringResource(R.string.terminos_s3_item2))
                ItemLista(stringResource(R.string.terminos_s3_item3))
                ItemLista(stringResource(R.string.terminos_s3_item4))
                ItemLista(stringResource(R.string.terminos_s3_item5))
                ItemLista(stringResource(R.string.terminos_s3_item6))
                ItemLista(stringResource(R.string.terminos_s3_item7))
            }}

            item { SeccionLegal(numero = "4", titulo = stringResource(R.string.terminos_s4_titulo), icono = Icons.Default.People) {
                SubseccionLegal(titulo = stringResource(R.string.terminos_s4_1_titulo)) {
                    Text(stringResource(R.string.terminos_s4_1_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SubseccionLegal(titulo = stringResource(R.string.terminos_s4_2_titulo)) {
                    Text(stringResource(R.string.terminos_s4_2_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SubseccionLegal(titulo = stringResource(R.string.terminos_s4_3_titulo)) {
                    Text(stringResource(R.string.terminos_s4_3_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }}

            item { SeccionLegal(numero = "5", titulo = stringResource(R.string.terminos_s5_titulo), icono = Icons.Default.Security) {
                ItemLista(stringResource(R.string.terminos_s5_item1))
                ItemLista(stringResource(R.string.terminos_s5_item2))
                ItemLista(stringResource(R.string.terminos_s5_item3))
                ItemLista(stringResource(R.string.terminos_s5_item4))
                Spacer(modifier = Modifier.height(8.dp))
                NotaLegal(texto = stringResource(R.string.terminos_s5_nota), icono = "🔒")
            }}

            item { SeccionLegal(numero = "6", titulo = stringResource(R.string.terminos_s6_titulo), icono = Icons.Default.PrivacyTip) {
                Text(stringResource(R.string.terminos_s6_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(8.dp))
                NotaLegal(texto = stringResource(R.string.terminos_s6_nota), icono = "📋")
            }}

            item { SeccionLegal(numero = "7", titulo = stringResource(R.string.terminos_s7_titulo), icono = Icons.Default.Copyright) {
                Text(stringResource(R.string.terminos_s7_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

            item { SeccionLegal(numero = "8", titulo = stringResource(R.string.terminos_s8_titulo), icono = Icons.Default.FileDownload) {
                ItemLista(stringResource(R.string.terminos_s8_item1))
                ItemLista(stringResource(R.string.terminos_s8_item2))
                ItemLista(stringResource(R.string.terminos_s8_item3))
            }}

            item { SeccionLegal(numero = "9", titulo = stringResource(R.string.terminos_s9_titulo), icono = Icons.Default.Info) {
                ItemLista(stringResource(R.string.terminos_s9_item1))
                ItemLista(stringResource(R.string.terminos_s9_item2))
                ItemLista(stringResource(R.string.terminos_s9_item3))
                ItemLista(stringResource(R.string.terminos_s9_item4))
            }}

            item { SeccionLegal(numero = "10", titulo = stringResource(R.string.terminos_s10_titulo), icono = Icons.Default.DeleteForever) {
                ItemLista(stringResource(R.string.terminos_s10_item1))
                ItemLista(stringResource(R.string.terminos_s10_item2))
                ItemLista(stringResource(R.string.terminos_s10_item3))
            }}

            item { SeccionLegal(numero = "11", titulo = stringResource(R.string.terminos_s11_titulo), icono = Icons.Default.Update) {
                Text(stringResource(R.string.terminos_s11_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(8.dp))
                NotaLegal(texto = stringResource(R.string.terminos_s11_nota), icono = "⚠️")
            }}

            item { SeccionLegal(numero = "12", titulo = stringResource(R.string.terminos_s12_titulo), icono = Icons.Default.Balance) {
                Text(stringResource(R.string.terminos_s12_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

            item { SeccionLegal(numero = "13", titulo = stringResource(R.string.terminos_s13_titulo), icono = Icons.Default.ContactMail) {
                ContactoLegal(
                    email = "clearcounts1@gmail.com",
                    web = "help.clearcounts.app",
                    direccion = "SENA – CTMA, Colombia"
                )
            }}

            // Footer
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                ) {
                    Text(
                        text = "ClearCounts © 2025 – Proyecto SENA CTMA | Todos los derechos reservados | Versión 1.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}