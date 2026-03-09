package com.example.clearcounts.ui.screens.Ajustes

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clearcounts.ui.screens.UserSessionViewModel
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.runtime.DisposableEffect
import androidx.core.app.ActivityCompat

private val LogoutRed  = Color(0xFFDC2626)
private val SubtitleGray = Color(0xFF6B7280)
private val DividerGray  = Color(0xFFE5E7EB)

@Composable
fun Ajustes(
    navegarPerfil: () -> Unit = {},
    navegarExportar: () -> Unit = {},
    onLogOut: () -> Unit = {},
    viewModel: UserSessionViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle()
    var showPermissionRationaleDialog by remember { mutableStateOf(false) }

    fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android < 13: el permiso es concedido en la instalación
            true
        }
    }

    var notificationsGranted by remember { mutableStateOf(checkNotificationPermission()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationsGranted = true
        } else {
            val canAsk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                false
            }

            if (!canAsk) {
                showPermissionRationaleDialog = true
            }
            notificationsGranted = false
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                notificationsGranted = checkNotificationPermission()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text  = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogOut()
                }) {
                    Text("Cerrar sesión", color = LogoutRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showPermissionRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionRationaleDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.NotificationsOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("Activar notificaciones") },
            text  = {
                Text(
                    "Para recibir alertas importantes necesitas activar " +
                            "las notificaciones manualmente desde los ajustes de tu dispositivo." +
                            "\n\n¿Deseas ir ahora?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionRationaleDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Ir a Ajustes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationaleDialog = false }) {
                    Text("Ahora no")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        ProfileCard(
            nombre = currentUser?.nombre ?: "Usuario",
            correo = currentUser?.correo ?: "",
            onClick = navegarPerfil
        )

        Spacer(Modifier.height(24.dp))

        SectionHeader("GENERAL")
        Spacer(Modifier.height(8.dp))
        SettingsCard {
            RowNavigable(
                icon = Icons.Default.Language,
                title = "Idioma",
                subtitle = "Español"
            )
            RowDivider()

            RowToggle(
                icon = Icons.Default.DarkMode,
                title = "Modo Oscuro",
                subtitle = "Apariencia oscura para la app",
                checked = isDarkMode,
                onChecked = { themeViewModel.toggleDarkMode(it) }
            )
        }

        Spacer(Modifier.height(24.dp))

        SectionHeader("NOTIFICACIONES")
        Spacer(Modifier.height(8.dp))
        SettingsCard {
            RowToggle(
                icon = Icons.Default.NotificationsNone,
                title = "Notificaciones Push",
                subtitle = if (notificationsGranted)
                    "Activadas - Toca para gestionar"
                else
                    "Desactivadas - Toca para activar",
                checked   = notificationsGranted,
                onChecked = {
                    when {
                        // Permiso activo → abrir ajustes del sistema para desactivar
                        notificationsGranted -> {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        }
                        // Sin permiso → pedir (primera vez: diálogo del sistema,
                        // denegado permanentemente: Android redirige a ajustes automáticamente)
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        // Android < 13 → siempre activo, ir a ajustes
                        else -> {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        SectionHeader("DATOS Y SOPORTE")
        Spacer(Modifier.height(8.dp))
        SettingsCard {
            RowNavigable(
                icon = Icons.Default.FileDownload,
                title = "Exportar Datos",
                subtitle = "CSV, PDF",
                onClick = navegarExportar
            )
            RowDivider()
            RowNavigable(
                icon  = Icons.AutoMirrored.Filled.HelpOutline,
                title = "Contactar"
            )
            RowDivider()
            RowNavigable(
                icon  = Icons.Default.Shield,
                title = "Términos y Condiciones"
            )
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { showLogoutDialog = true },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = LogoutRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    color = LogoutRed,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(36.dp))
    }
}

@Composable
private fun ProfileCard(
    nombre: String,
    correo: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }           // ← navega a Perfil
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = nombre,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = correo,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}


@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = SubtitleGray,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.9.sp,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        content = content
    )
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 54.dp),
        color = DividerGray,
        thickness = 0.3.dp
    )
}

@Composable
private fun RowNavigable(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.primaryContainer,
            modifier           = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(text = subtitle, color = SubtitleGray, fontSize = 13.sp)
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = SubtitleGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun RowToggle(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(text = subtitle, color = SubtitleGray, fontSize = 13.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onChecked,
            colors = SwitchDefaults.colors(
                checkedTrackColor   = MaterialTheme.colorScheme.primaryContainer,
                checkedThumbColor   = Color.White,
                uncheckedTrackColor = Color(0xFFD1D5DB),
                uncheckedThumbColor = Color.White
            )
        )
    }
}