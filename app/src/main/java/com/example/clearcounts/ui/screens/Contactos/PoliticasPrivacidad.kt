package com.example.clearcounts.ui.screens.Contactos

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
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
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
fun PoliticasPrivacidad(
    botonVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                            )
                        )
                    )
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
                            text = stringResource(R.string.politicas_privacidad),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.ultima_actualizacion_politicas),
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
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.politicas_intro),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            item { SeccionLegal(numero = "1", titulo = stringResource(R.string.politicas_s1_titulo), icono = Icons.Default.CollectionsBookmark) {
                SubseccionLegal(titulo = stringResource(R.string.politicas_s1_1_titulo)) {
                    ItemLista(stringResource(R.string.politicas_s1_1_item1))
                    ItemLista(stringResource(R.string.politicas_s1_1_item2))
                    ItemLista(stringResource(R.string.politicas_s1_1_item3))
                    ItemLista(stringResource(R.string.politicas_s1_1_item4))
                    ItemLista(stringResource(R.string.politicas_s1_1_item5))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SubseccionLegal(titulo = stringResource(R.string.politicas_s1_2_titulo)) {
                    ItemLista(stringResource(R.string.politicas_s1_2_item1))
                    ItemLista(stringResource(R.string.politicas_s1_2_item2))
                    ItemLista(stringResource(R.string.politicas_s1_2_item3))
                    ItemLista(stringResource(R.string.politicas_s1_2_item4))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SubseccionLegal(titulo = stringResource(R.string.politicas_s1_3_titulo)) {
                    ItemLista(stringResource(R.string.politicas_s1_3_item1))
                    ItemLista(stringResource(R.string.politicas_s1_3_item2))
                    ItemLista(stringResource(R.string.politicas_s1_3_item3))
                    ItemLista(stringResource(R.string.politicas_s1_3_item4))
                    ItemLista(stringResource(R.string.politicas_s1_3_item5))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SubseccionLegal(titulo = stringResource(R.string.politicas_s1_4_titulo)) {
                    Text(stringResource(R.string.politicas_s1_4_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }}

            item { SeccionLegal(numero = "2", titulo = stringResource(R.string.politicas_s2_titulo), icono = Icons.Default.ManageSearch) {
                ItemLista(stringResource(R.string.politicas_s2_item1))
                ItemLista(stringResource(R.string.politicas_s2_item2))
                ItemLista(stringResource(R.string.politicas_s2_item3))
                ItemLista(stringResource(R.string.politicas_s2_item4))
                ItemLista(stringResource(R.string.politicas_s2_item5))
                ItemLista(stringResource(R.string.politicas_s2_item6))
                ItemLista(stringResource(R.string.politicas_s2_item7))
                ItemLista(stringResource(R.string.politicas_s2_item8))
                ItemLista(stringResource(R.string.politicas_s2_item9))
                Spacer(modifier = Modifier.height(8.dp))
                NotaLegal(texto = stringResource(R.string.politicas_s2_nota), icono = "✅")
            }}

            item { SeccionLegal(numero = "3", titulo = stringResource(R.string.politicas_s3_titulo), icono = Icons.Default.Share) {
                SubseccionLegal(titulo = stringResource(R.string.politicas_s3_1_titulo)) {
                    ItemLista(stringResource(R.string.politicas_s3_1_item1))
                    ItemLista(stringResource(R.string.politicas_s3_1_item2))
                    ItemLista(stringResource(R.string.politicas_s3_1_item3))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SubseccionLegal(titulo = stringResource(R.string.politicas_s3_2_titulo)) {
                    Text(stringResource(R.string.politicas_s3_2_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SubseccionLegal(titulo = stringResource(R.string.politicas_s3_3_titulo)) {
                    Text(stringResource(R.string.politicas_s3_3_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }}

            item { SeccionLegal(numero = "4", titulo = stringResource(R.string.politicas_s4_titulo), icono = Icons.Default.Storage) {
                ItemLista(stringResource(R.string.politicas_s4_item1))
                ItemLista(stringResource(R.string.politicas_s4_item2))
                ItemLista(stringResource(R.string.politicas_s4_item3))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.politicas_s4_pie), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

            item { SeccionLegal(numero = "5", titulo = stringResource(R.string.politicas_s5_titulo), icono = Icons.Default.Lock) {
                ItemLista(stringResource(R.string.politicas_s5_item1))
                ItemLista(stringResource(R.string.politicas_s5_item2))
                ItemLista(stringResource(R.string.politicas_s5_item3))
                ItemLista(stringResource(R.string.politicas_s5_item4))
                ItemLista(stringResource(R.string.politicas_s5_item5))
                ItemLista(stringResource(R.string.politicas_s5_item6))
                Spacer(modifier = Modifier.height(8.dp))
                NotaLegal(texto = stringResource(R.string.politicas_s5_nota), icono = "⚠️")
            }}

            item { SeccionLegal(numero = "6", titulo = stringResource(R.string.politicas_s6_titulo), icono = Icons.Default.AdminPanelSettings) {
                ItemLista(stringResource(R.string.politicas_s6_item1))
                ItemLista(stringResource(R.string.politicas_s6_item2))
                ItemLista(stringResource(R.string.politicas_s6_item3))
                ItemLista(stringResource(R.string.politicas_s6_item4))
                ItemLista(stringResource(R.string.politicas_s6_item5))
                ItemLista(stringResource(R.string.politicas_s6_item6))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.politicas_s6_pie), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

            item { SeccionLegal(numero = "7", titulo = stringResource(R.string.politicas_s7_titulo), icono = Icons.Default.Schedule) {
                ItemLista(stringResource(R.string.politicas_s7_item1))
                ItemLista(stringResource(R.string.politicas_s7_item2))
                ItemLista(stringResource(R.string.politicas_s7_item3))
                ItemLista(stringResource(R.string.politicas_s7_item4))
                ItemLista(stringResource(R.string.politicas_s7_item5))
            }}

            item { SeccionLegal(numero = "8", titulo = stringResource(R.string.politicas_s8_titulo), icono = Icons.Default.ChildCare) {
                Text(stringResource(R.string.politicas_s8_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

            item { SeccionLegal(numero = "9", titulo = stringResource(R.string.politicas_s9_titulo), icono = Icons.Default.EditNote) {
                Text(stringResource(R.string.politicas_s9_contenido), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

            item { SeccionLegal(numero = "10", titulo = stringResource(R.string.politicas_s10_titulo), icono = Icons.Default.ContactMail) {
                ContactoLegal(
                    email = "clearcounts1@gmail.com",
                    web = "privacy.clearcounts.app",
                    direccion = "SENA – CTMA, Colombia"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.politicas_s10_pie), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }}

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