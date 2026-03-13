package com.example.clearcounts.ui.screens.Ajustes

import android.util.Log
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R

data class IdiomaItem(
    val codigo: String,
    val nombre: String,
    val nombreNativo: String
)

val idiomasDisponibles = listOf(
    IdiomaItem("es", "Español", "Spanish"),
    IdiomaItem("en", "English", "English"),
    IdiomaItem("pt", "Português", "Portuguese"),
    IdiomaItem("fr", "Français",   "French")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionarIdioma(
    idiomaSeleccionado: String = "ES",
    onIdiomaSeleccionado: (IdiomaItem) -> Unit = {},
    botonVolver: () -> Unit = {}
) {
    var seleccionado by remember { mutableStateOf(idiomaSeleccionado) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.seleccionar_idioma),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = botonVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.height(80.dp),
                windowInsets = WindowInsets(top = 20.dp)
            )
        }
    ) { innerPadding ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            idiomasDisponibles.forEachIndexed { index, idioma ->
                IdiomaRow(
                    idioma = idioma,
                    isSelected = seleccionado == idioma.codigo,
                    onClick = {
                        seleccionado = idioma.codigo
                        Log.d("IDIOMA", "Presionado: ${idioma.codigo}")
                        LocaleManager.setLocale(context, idioma.codigo)
                        onIdiomaSeleccionado(idioma)
                    }
                )
                if (index < idiomasDisponibles.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = DividerGray,
                        thickness = 0.3.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun IdiomaRow(
    idioma: IdiomaItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Código del idioma (ES, US, etc.)
        Box(
            modifier = Modifier.width(42.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = idioma.codigo,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.width(10.dp))

        // Nombre nativo + nombre en inglés
        Column(Modifier.weight(1f)) {
            Text(
                text = idioma.nombreNativo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = idioma.nombre,
                fontSize = 13.sp,
                color = SubtitleGray
            )
        }

        // Checkmark si está seleccionado
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}