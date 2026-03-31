package com.example.clearcounts.ui.screens.RecuperarContrasena

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.Barras.TopBar
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.gris
import com.example.clearcounts.utils.OutlinedTextFieldColors
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasena(
    botonVolver: () -> Unit,
    onBotonSiguiente: (String) -> Unit,
    viewModel: RecuperarContrasenaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var errorCorreo by remember { mutableStateOf("") }
    val proveedorSocial by viewModel.proveedorSocial.collectAsState()
    val puedeNavegar by viewModel.puedeNavegar.collectAsState()

    val correoEnviado by viewModel.correoEnviado.collectAsState()

    LaunchedEffect(puedeNavegar) {
        if (puedeNavegar) {
            viewModel.limpiarEstado()
            onBotonSiguiente(correoEnviado)
        }
    }

    LaunchedEffect(correo) {
        viewModel.limpiarEstado()
        errorCorreo = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp, end = 14.dp),
                            text = "ClearCounts",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 30.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = botonVolver,
                        modifier = Modifier.padding(top = 10.dp, start = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.recuperar_contrasena),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp),
                fontSize = 18.sp
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = stringResource(R.string.busca_tu_correo),
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 15.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 24.dp, bottom = 24.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(R.string.introduce_tu_direccion_de_correo_electronico_de_recuperacion),
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 24.dp),
                fontSize = 16.sp
            )

            // Campo correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                placeholder = { Text(stringResource(R.string.ingrese_su_correo_electr_nico)) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                isError = errorCorreo.isNotEmpty(),
                colors = OutlinedTextFieldColors(
                    unfocusedBorder = gris,
                    focusedBorder = gris
                ),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .fillMaxWidth()
            )
            if (errorCorreo.isNotEmpty()) {
                Text(
                    text = errorCorreo,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 20.dp, top = 4.dp)
                )
            }

            // Aviso cuenta social
            AnimatedVisibility(visible = proveedorSocial != null) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (proveedorSocial) {
                                "google" -> context.getString(R.string.esta_cuenta_usa_google_para_iniciar_sesi_n_usa_el_bot_n_de_google_en_la_pantalla_de_inicio)
                                "facebook" -> context.getString(R.string.esta_cuenta_usa_facebook_para_iniciar_sesi_n_usa_el_bot_n_de_facebook_en_la_pantalla_de_inicio)
                                else -> ""
                            },
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Botón enviar
            Button(
                onClick = {
                    when {
                        correo.isBlank() -> {
                            errorCorreo = context.getString(R.string.el_correo_es_requerido)
                        }
                        !correo.contains("@") -> {
                            errorCorreo = context.getString(R.string.ingresa_un_correo_valido)
                        }
                        else -> {
                            viewModel.verificarProveedorYEnviar(correo)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.enviar_correo),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
