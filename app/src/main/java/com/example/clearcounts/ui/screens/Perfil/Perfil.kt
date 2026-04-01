package com.example.clearcounts.ui.screens.Perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.UserSessionViewModel
import com.example.clearcounts.ui.theme.ClearCountTheme


@Composable
fun Perfil(
    navegarCambiarContrasena: () -> Unit,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val totalTransacciones by viewModel.totalTransacciones.collectAsState()
    val totalMetodosPago by viewModel.totalMetodosPago.collectAsState()
    val totalCategorias by viewModel.totalCategorias.collectAsState()
    val context = LocalContext.current
    var modoEdicion by remember { mutableStateOf(false) }
    val esUsuarioSocial by viewModel.esUsuarioSocial.collectAsState()
    var nombreEdit by remember { mutableStateOf("") }
    var telefonoEdit by remember { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        nombreEdit = currentUser?.nombre ?: ""
        telefonoEdit = currentUser?.numero ?: ""
    }
    var showAvatarSelector by remember { mutableStateOf(false) }

    val avatarActual = currentUser?.avatar ?: "cacatuaninfa"
    val avatarId = remember(avatarActual) {
        context.resources.getIdentifier(avatarActual, "drawable", context.packageName)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── HEADER AZUL ──
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
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            // Botón Editar
            TextButton(
                onClick = {
                    if (modoEdicion) {
                        viewModel.updateUser(nombreEdit, telefonoEdit)
                    }
                    modoEdicion = !modoEdicion
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Text(
                    text = if (modoEdicion) stringResource(R.string.aceptar) else stringResource(R.string.editar),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Avatar con botón de cámara
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .border(3.dp, Color.White, CircleShape)
                            .clickable { showAvatarSelector = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (avatarId != 0) {
                            Image(
                                painter = painterResource(id = avatarId),
                                contentDescription = null,
                                modifier = Modifier.size(320.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = Color.White
                            )
                        }
                    }

                    // Icono cámara
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { showAvatarSelector = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentUser?.nombre ?: "Usuario",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = currentUser?.correo ?: "",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // ── TARJETA DE STATS ──
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    valor = "$totalTransacciones",
                    etiqueta = stringResource(R.string.transacciones),
                    color = MaterialTheme.colorScheme.primary
                )
                VerticalDivider(modifier = Modifier.height(40.dp))
                StatItem(
                    valor = "$totalMetodosPago",
                    etiqueta = stringResource(R.string.m_todo_de_pago),
                    color = Color(0xFF2ECC71)
                )
                VerticalDivider(modifier = Modifier.height(40.dp))
                StatItem(
                    valor = "$totalCategorias",
                    etiqueta = stringResource(R.string.categorias),
                    color = Color(0xFF9B59B6)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-12).dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── INFORMACIÓN PERSONAL ──
            item {
                Text(
                    text = stringResource(R.string.informacion_personal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    InfoItem(
                        icono = Icons.Default.Person,
                        colorIcono = Color(0xFF3498DB),
                        etiqueta = stringResource(R.string.nombre_completo),
                        valor = nombreEdit,
                        editable = modoEdicion,
                        onValorChange = { nombreEdit = it }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    InfoItem(
                        icono = Icons.Default.Email,
                        colorIcono = Color(0xFF9B59B6),
                        etiqueta = stringResource(R.string.correo),
                        valor = currentUser?.correo ?: "-",
                        editable = false
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    InfoItem(
                        icono = Icons.Default.Phone,
                        colorIcono = Color(0xFF2ECC71),
                        etiqueta = stringResource(R.string.numero_de_celular),
                        valor = telefonoEdit,
                        editable = modoEdicion,
                        onValorChange = { nuevoValor ->
                            if (nuevoValor.all { it.isDigit() || it == '+' || it == ' ' || it == '-' }) {
                                telefonoEdit = nuevoValor
                            }
                        },
                        keyboardType = KeyboardType.Phone
                    )
                }
            }

            // ── SEGURIDAD ──
            item {
                Text(
                    text = stringResource(R.string.seguridad_y_cuenta),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    if (esUsuarioSocial) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE67E22).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.cambiar_contrasena),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                                Text(
                                    text = if (currentUser?.proveedor == "google")
                                        stringResource(R.string.cuenta_vinculada_google)
                                    else
                                        stringResource(R.string.cuenta_vinculada_facebook),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                        }
                    } else {
                        AccionItem(
                            icono = Icons.Default.Lock,
                            colorIcono = Color(0xFFE67E22),
                            titulo = stringResource(R.string.cambiar_contrasena),
                            subtitulo = stringResource(R.string.actualiza_tu_contrasena),
                            onClick = navegarCambiarContrasena
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // ── SELECTOR DE AVATAR ──
    if (showAvatarSelector) {
        SelectorAvatarDialog(
            avatarActual = avatarActual,
            onAvatarSeleccionado = { nuevoAvatar ->
                viewModel.updateAvatar(nuevoAvatar)
                showAvatarSelector = false
            },
            onDismiss = { showAvatarSelector = false }
        )
    }
}

@Composable
fun StatItem(valor: String, etiqueta: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = valor,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            text = etiqueta,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InfoItem(
    icono: ImageVector,
    colorIcono: Color,
    etiqueta: String,
    valor: String,
    editable: Boolean = false,
    onValorChange: (String) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colorIcono.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            if (editable) {
                OutlinedTextField(
                    value = valor,
                    onValueChange = onValorChange,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
                )
            } else {
                Text(
                    text = valor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AccionItem(
    icono: ImageVector,
    colorIcono: Color,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colorIcono.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitulo,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun SelectorAvatarDialog(
    avatarActual: String,
    onAvatarSeleccionado: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val avatares = listOf(
        "cacatua","cacatuaninfa", "guacamaya", "loroverde","dragonrojo","bulldog",
        "perropastoraleman",  "capibara", "gallina", "gallo", "cocodrilo","caballo", "poni",
         "leon","leona", "osopardo", "perrohusky", "tigre","jaguar", "cerdo", "conejo",
        "condor", "delfinrosado",  "serpienteverde", "osopolar", "tortuga" ,"vacaholstein",
        "perrolabrador", "gatobicolor","osopanda", "lagartijaverde", "tiburonblanco"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.selecciona_tu_avatar),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(300.dp)
            ) {
                items(avatares) { avatar ->
                    val iconoId = context.resources.getIdentifier(
                        avatar, "drawable", context.packageName
                    )
                    val isSelected = avatar == avatarActual

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onAvatarSeleccionado(avatar) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (iconoId != 0) {
                            Image(
                                painter = painterResource(id = iconoId),
                                contentDescription = avatar,
                                modifier = Modifier.size(44.dp)
                            )
                        } else {
                            Text(
                                text = avatar.first().uppercase(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}